package com.gamefocal.island.service;

import com.gamefocal.island.DedicatedServer;
import com.gamefocal.island.entites.net.*;
import com.gamefocal.island.entites.service.HiveService;
import com.gamefocal.island.entites.util.BufferUtil;
import com.gamefocal.island.entites.voip.VoipType;
import com.gamefocal.island.events.PlayerSpawnEvent;
import com.gamefocal.island.events.PlayerVoiceEvent;
import com.gamefocal.island.game.util.Location;
import com.google.auto.service.AutoService;
import fr.devnied.bitlib.BytesUtils;
import org.reflections.Reflections;

import javax.inject.Singleton;
import java.net.DatagramPacket;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Hashtable;
import java.util.Set;
import java.util.UUID;

@Singleton
@AutoService(HiveService.class)
public class CommandService implements HiveService<CommandService> {

    private Hashtable<String, HiveCommand> commands = new Hashtable<>();

    @Override
    public void init() {
        System.out.println("\tLoading Commands...");

        // Load the commands
        Set<Class<? extends HiveCommand>> commandClasses = new Reflections("com.gamefocal").getSubTypesOf(HiveCommand.class);

        // Load them...
        for (Class<? extends HiveCommand> cc : commandClasses) {
            Command hc = cc.getAnnotation(Command.class);
            if (hc != null) {

                try {
                    HiveCommand c = cc.newInstance();
                    c.setName(hc.name());

                    for (String a : hc.sources().split(",")) {
                        if (a.equalsIgnoreCase("tcp")) {
                            c.getAllowedSources().add(CommandSource.NET_TCP);
                        } else if (a.equalsIgnoreCase("udp")) {
                            c.getAllowedSources().add(CommandSource.NET_UDP);
                        } else if (a.equalsIgnoreCase("cli")) {
                            c.getAllowedSources().add(CommandSource.CONSOLE);
                        }
                    }

                    this.commands.put(c.getName(), c);

                } catch (InstantiationException | IllegalAccessException e) {
                    e.printStackTrace();
                }

            }
        }

    }

    public HiveNetMessage stringToMsg(String msg) {
        HiveNetMessage message = new HiveNetMessage();

        String[] parts = msg.split("\\|");

        message.cmd = parts[0];

        message.args = new String[parts.length - 1];
        for (int i = 1; i < parts.length; i++) {
            message.args[i - 1] = parts[i];
        }

        return message;
    }

    public String msgToString(HiveNetMessage msg) {
        StringBuilder b = new StringBuilder();
        b.append(msg.cmd);
        for (String a : msg.args) {
            b.append("|").append(a);
        }

        return b.toString();
    }

    public void handleCommand(String msg, CommandSource source, HiveNetConnection connection) {
        HiveNetMessage m = this.stringToMsg(msg);
        if (m != null) {
            this.handleCommand(m, source, connection);
        }
    }

    public void handleVoice(byte[] data, DatagramPacket packet) {

        String[] msgParts = new String(data, StandardCharsets.UTF_8).split("\\|");

        if (msgParts.length == 2) {

            Integer vid = Integer.parseInt(msgParts[0]);

            String dataBase64 = msgParts[1];

            VoipService voipService = DedicatedServer.get(VoipService.class);

            if (voipService.voiceClients.containsKey(vid)) {

                HiveNetConnection connection = voipService.voiceClients.get(vid);

                if (connection.getSoundOut() == null) {
                    voipService.voiceClients.get(vid).setSoundOut(packet);
                    System.out.println("Return VOIP Link Created for " + connection.getUuid());
                }

                /*
                 * Relay to others nearby
                 * */

                PlayerVoiceEvent completedEvent = new PlayerVoiceEvent(connection, (short) 0, connection.getVoipDistance(), dataBase64).call();

                StringBuilder builder = new StringBuilder();
                builder.append(vid).append("|").append(connection.getPlayer().location.toString()).append("|").append(dataBase64);

                if (!completedEvent.isCanceled()) {

//                    System.out.println("sending to " + completedEvent.getRecivers().size() + " neighbors");

                    for (HiveNetConnection n : completedEvent.getRecivers()) {
                        n.sendSoundData(builder.toString());
                    }
                }
            }

        }

    }

    public void handleTelemetry(String telemetry, DatagramPacket packet) {
        String[] p = telemetry.split("\\|");

        if (p.length >= 2) {

            String auth = p[0];
            String cmd = p[1];

            String[] args = new String[p.length - 2];
            for (int i = 2; i < p.length; i++) {
                args[i - 2] = p[i];
            }

            if (DedicatedServer.get(PlayerService.class).players.containsKey(UUID.fromString(auth))) {

                HiveNetConnection netConnection = DedicatedServer.get(PlayerService.class).players.get(UUID.fromString(auth));

                if (netConnection.getUdpOut() == null) {
                    // No outbound socket set yet.
                    netConnection.setUdpOut(packet);

                    // Spawn the world
                    new PlayerSpawnEvent(netConnection, new Location(0, 0, 0)).call();
                }

                HiveCommand c = this.getCommand(cmd);
                if (c != null) {
                    HiveNetMessage message = new HiveNetMessage();
                    message.cmd = cmd;
                    message.args = args;

                    c.runCommand(message, CommandSource.NET_UDP, netConnection);
                } else {
                    System.err.println("Unable to find command for telemetry packet...");
                }
            } else {
                System.err.println("No auth for telemetry packet...");
            }
        } else {
            System.err.println("Invalid Length of Telemetry Packet...");
        }
    }

    public void handleCommand(HiveNetMessage m, CommandSource source, HiveNetConnection netConnection) {
        HiveCommand cmd = this.getCommand(m.cmd);
        if (cmd != null) {
            cmd.runCommand(m, source, netConnection);
        }
    }

    public HiveCommand getCommand(String name) {
        if (this.commands.containsKey(name)) {
            return this.commands.get(name);
        }

        return null;
    }
}
