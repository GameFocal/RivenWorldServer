package com.gamefocal.island.commands.cli;

import com.gamefocal.island.DedicatedServer;
import com.gamefocal.island.entites.net.*;
import com.gamefocal.island.game.GameEntity;
import com.gamefocal.island.game.foliage.FoliageState;
import com.gamefocal.island.models.GameFoliageModel;
import com.gamefocal.island.service.FoliageService;
import com.gamefocal.island.service.NetworkService;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
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

            if (args[1].equalsIgnoreCase("sync")) {
                HiveNetMessage message1 = new HiveNetMessage();
                message1.cmd = "worldsync";

                DedicatedServer.get(NetworkService.class).broadcast(message1, null);

                System.out.println("Syncing Foliage from Game to Server... Fly around to complete this.");
            } else if (args[1].equalsIgnoreCase("dump")) {
                HiveNetMessage message1 = new HiveNetMessage();
                message1.cmd = "worldsync";

                DedicatedServer.get(NetworkService.class).broadcast(message1, null);

                Gson g = new Gson();

                JsonArray arr = new JsonArray();

                for (Map.Entry<String,GameEntity> e : DedicatedServer.get(FoliageService.class).getFoliageEntites().entrySet()) {
                    GameFoliageModel f = new GameFoliageModel();
                    f.hash = e.getKey();
                    f.foliageState = FoliageState.GROWN;
                    f.foliageType = e.getValue().getClass().getName();
                    f.location = e.getValue().location;
                    f.attachedEntity = e.getValue();

                    arr.add(g.toJsonTree(f, GameFoliageModel.class));
                }

                Files.write(Paths.get("foliage.json"), arr.toString().getBytes(StandardCharsets.UTF_8), StandardOpenOption.CREATE);

                System.out.println("Foliage written to foliage.json");
            }
        }
    }
}
