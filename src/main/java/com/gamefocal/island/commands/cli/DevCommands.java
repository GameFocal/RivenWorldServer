package com.gamefocal.island.commands.cli;

import com.gamefocal.island.DedicatedServer;
import com.gamefocal.island.entites.net.*;
import com.gamefocal.island.game.GameEntity;
import com.gamefocal.island.game.foliage.FoliageState;
import com.gamefocal.island.models.GameFoliageModel;
import com.gamefocal.island.service.*;
import com.google.gson.*;

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

        if (cmd.equalsIgnoreCase("clean-players")) {
            DedicatedServer.get(PlayerService.class).players.clear();
        } else if (cmd.equalsIgnoreCase("clean-entites")) {
            DedicatedServer.instance.getWorld().entites.clear();
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
        }
    }
}
