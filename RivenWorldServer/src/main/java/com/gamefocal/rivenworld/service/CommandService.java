package com.gamefocal.rivenworld.service;

import com.gamefocal.rivenworld.DedicatedServer;
import com.gamefocal.rivenworld.entites.net.*;
import com.gamefocal.rivenworld.entites.service.HiveService;
import com.gamefocal.rivenworld.events.player.PlayerVoiceEvent;
import com.google.auto.service.AutoService;
import org.reflections.Reflections;

import javax.inject.Singleton;
import java.net.DatagramPacket;
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
                        } else if (a.equalsIgnoreCase("chat")) {
                            c.getAllowedSources().add(CommandSource.CHAT);
                        }
                    }

                    this.commands.put(c.getName(), c);

                    if (!hc.aliases().isEmpty()) {
                        for (String a : hc.aliases().split(",")) {
                            if (!a.equalsIgnoreCase("")) {
                                this.commands.put(a, c);
                            }
                        }
                    }

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
        HiveNetMessage m = this.stringToMsg(msg.trim());
        if (m != null) {
            this.handleCommand(m, source, connection);
        } else {
            System.out.println("Failed to find MSG");
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

                    // Change state to isTalking
                    connection.getState().lastSpeach = System.currentTimeMillis();

//                    System.out.println("sending to " + completedEvent.getRecivers().size() + " neighbors");

                    for (HiveNetConnection n : completedEvent.getRecivers()) {
                        n.sendSoundData(builder.toString());
                    }
                }
            }

        }

    }

    public void handleTelemetry(String telemetry) {
        String[] p = telemetry.split("\\|");

//        System.out.println(telemetry);

        if (p.length >= 2) {

            String auth = p[0];
            String cmd = p[1];

            String[] args = new String[p.length - 2];
            for (int i = 2; i < p.length; i++) {
                args[i - 2] = p[i];
            }

            if (DedicatedServer.get(PlayerService.class).players.containsKey(UUID.fromString(auth))) {

                HiveNetConnection netConnection = DedicatedServer.get(PlayerService.class).players.get(UUID.fromString(auth));

                HiveCommand c = this.getCommand(cmd);
                if (c != null) {
                    HiveNetMessage message = new HiveNetMessage();
                    message.cmd = cmd;
                    message.args = args;

                    c.runCommand(message, CommandSource.NET_UDP, netConnection);
                } else {
                    System.err.println("Unable to find command for telemetry packet by name of " + cmd);
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
        } else {
            System.out.println("Invalid Cmd: [" + m.cmd + "]");
        }
    }

    public HiveCommand getCommand(String name) {
        if (this.commands.containsKey(name)) {
            return this.commands.get(name);
        }

        return null;
    }
}
