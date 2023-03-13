package com.gamefocal.rivenworld.commands.chat;

import com.gamefocal.rivenworld.DedicatedServer;
import com.gamefocal.rivenworld.entites.net.*;
import com.gamefocal.rivenworld.game.util.Location;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.HashMap;

@Command(name = "locr", sources = "chat", aliases = "lr")
public class LocationListCommand extends HiveCommand {

    public static String list = "default";
    public static HashMap<String, ArrayList<Location>> lists = new HashMap<>();

    @Override
    public void onCommand(HiveNetMessage message, CommandSource source, HiveNetConnection netConnection) throws Exception {
        if (netConnection.isAdmin()) {
            if (message.args.length >= 1) {
                if (message.args[0].equalsIgnoreCase("select")) {
                    list = message.args[1];
                } else if (message.args[0].equalsIgnoreCase("save")) {
                    String s = DedicatedServer.gson.toJson(lists, HashMap.class);
                    Files.write(Paths.get("location-save.json"), s.getBytes(StandardCharsets.UTF_8), StandardOpenOption.CREATE);
                } else if (message.args[0].equalsIgnoreCase("clear")) {
                    lists.clear();
                }
            } else {
                // Record loc to list

                if (!lists.containsKey(list)) {
                    lists.put(list, new ArrayList<>());
                }

                lists.get(list).add(netConnection.getPlayer().location.cpy());
            }
        }
    }
}
