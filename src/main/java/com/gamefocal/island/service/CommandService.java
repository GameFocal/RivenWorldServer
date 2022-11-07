package com.gamefocal.island.service;

import com.gamefocal.island.DedicatedServer;
import com.gamefocal.island.entites.net.*;
import com.gamefocal.island.entites.orm.models.Player;
import com.gamefocal.island.entites.service.HiveService;
import com.google.auto.service.AutoService;
import org.reflections.Reflections;

import javax.inject.Singleton;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.Set;

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

        message.args = Arrays.copyOfRange(parts, 1, parts.length - 1);

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

    public void handleTelemetry(String telemetry, DatagramPacket packet) {
        String[] p = telemetry.split("\\|");

        if (p.length >= 2) {
            String auth = p[0];
            String cmd = p[1];
            String[] args = Arrays.copyOfRange(p, 2, p.length - 1);

            PlayerService playerService = DedicatedServer.get(PlayerService.class);

            if (playerService.getPlayers().containsKey(auth)) {
                HiveCommand c = this.getCommand(cmd);
                if (c != null) {

                    HiveNetConnection netConnection = playerService.getPlayers().get(auth);

                    HiveNetMessage message = new HiveNetMessage();
                    message.cmd = cmd;
                    message.args = args;

                    c.runCommand(message, CommandSource.NET_UDP, netConnection);
                }
            }
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
