package com.gamefocal.island.commands.chat;

import com.gamefocal.island.DedicatedServer;
import com.gamefocal.island.entites.net.*;
import com.gamefocal.island.game.util.Location;
import com.gamefocal.island.service.RespawnService;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.HashMap;

@Command(name = "kill", sources = "chat", aliases = "lr")
public class KillCommand extends HiveCommand {
    @Override
    public void onCommand(HiveNetMessage message, CommandSource source, HiveNetConnection netConnection) throws Exception {
        DedicatedServer.get(RespawnService.class).killPlayer(netConnection, null);
    }
}
