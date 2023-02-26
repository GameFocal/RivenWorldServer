package com.gamefocal.rivenworld.commands.cli;

import com.gamefocal.rivenworld.DedicatedServer;
import com.gamefocal.rivenworld.dev.mapbox.RivenWorldMapBox;
import com.gamefocal.rivenworld.entites.net.*;
import com.gamefocal.rivenworld.game.util.Location;
import com.gamefocal.rivenworld.service.InventoryService;
import com.gamefocal.rivenworld.service.PlayerService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import javax.swing.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

@Command(name = "dev", sources = "cli,chat")
public class DevCommands extends HiveCommand {

    public static int factor = 100;

    public static Location offset = new Location(0, 0, 0);

    @Override
    public void onCommand(HiveNetMessage message, CommandSource source, HiveNetConnection netConnection) throws Exception {
        // Sync Foliage from the players

        if (source == CommandSource.CONSOLE || (source == CommandSource.CHAT && netConnection.isAdmin())) {

            String[] args = message.args;

            String cmd = args[0];

            if (cmd.equalsIgnoreCase("clean-players")) {
                DedicatedServer.get(PlayerService.class).players.clear();
            } else if (cmd.equalsIgnoreCase("clean-entites")) {
//            DedicatedServer.instance.getWorld().entites.clear();
            } else if (cmd.equalsIgnoreCase("export-items")) {

                JsonObject a = new JsonObject();
                for (String s : DedicatedServer.get(InventoryService.class).getItemSlugs()) {
                    JsonObject o = new JsonObject();
                    o.addProperty("spawn_name", s);
                    o.add("aliases", new JsonArray());
                    a.add(s, o);
                }

                Gson g = new GsonBuilder().setPrettyPrinting().create();

                Files.writeString(Paths.get("items.json"), g.toJson(a), StandardOpenOption.CREATE);
            } else if (cmd.equalsIgnoreCase("mapbox")) {
                // Launch the Mapbox GUI
                JFrame frame = new JFrame("RivenWorld Dev Mapbox");
                frame.setContentPane(new RivenWorldMapBox().mainPanel);
                frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                frame.setTitle("RivenWorld Dev Mapbox");
                frame.pack();
                frame.setVisible(true);
            } else if (cmd.equalsIgnoreCase("mapf")) {
                factor = Integer.parseInt(message.args[1]);
                System.out.println("Factor Set To: " + factor);
            } else if (cmd.equalsIgnoreCase("mapox")) {
                offset.setX(Float.parseFloat(message.args[1]));
            } else if (cmd.equalsIgnoreCase("mapoy")) {
                offset.setY(Float.parseFloat(message.args[1]));
            }
        }
    }
}
