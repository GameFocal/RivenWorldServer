package com.gamefocal.island.commands.cli;

import com.gamefocal.island.DedicatedServer;
import com.gamefocal.island.entites.net.*;
import com.gamefocal.island.game.GameEntity;
import com.gamefocal.island.game.foliage.FoliageState;
import com.gamefocal.island.models.GameFoliageModel;
import com.gamefocal.island.service.FoliageService;
import com.gamefocal.island.service.NetworkService;
import com.gamefocal.island.service.PlayerService;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Base64;
import java.util.Map;
import java.util.UUID;

@Command(name = "dev", sources = "cli")
public class DevCommands extends HiveCommand {
    @Override
    public void onCommand(HiveNetMessage message, CommandSource source, HiveNetConnection netConnection) throws Exception {
        // Sync Foliage from the players

        String[] args = message.args;

        String cmd = args[0];

        if (cmd.equalsIgnoreCase("foliage")) {

            if (args[1].equalsIgnoreCase("scan")) {
                HiveNetMessage message1 = new HiveNetMessage();
                message1.cmd = "worldsync";

                DedicatedServer.get(NetworkService.class).broadcast(message1, null);

                System.out.println("Syncing Foliage from Game to Server... Fly around to complete this.");
            } else if (args[1].equalsIgnoreCase("dump")) {
                HiveNetMessage message1 = new HiveNetMessage();
                message1.cmd = "worldsync";

                DedicatedServer.get(NetworkService.class).broadcast(message1, null);

//                BufferedWriter w = Files.newBufferedWriter(Paths.get("foliage.json"),StandardOpenOption.CREATE);
////                BufferedReader r = new BufferedReader(new StringReader(DedicatedServer.get(FoliageService.class).getFoliageCache().toString()));
//
//                w.write(DedicatedServer.get(FoliageService.class).getFoliageCache().toString());
//                w.flush();
//                w.close();

                System.out.println("Dumping " + DedicatedServer.get(FoliageService.class).getFoliageCache().size() + " Foliage Items to file...");

                Files.writeString(Paths.get("foliage.json"), DedicatedServer.get(FoliageService.class).getFoliageCache().toString(), StandardOpenOption.CREATE);

                System.out.println("Foliage written to foliage.json");
            } else if (args[1].equalsIgnoreCase("sync")) {
                for (HiveNetConnection connection : DedicatedServer.get(PlayerService.class).players.values()) {
                    DedicatedServer.instance.getWorld().loadFoliageForPlayer(connection);
                }
            }
        }
    }
}
