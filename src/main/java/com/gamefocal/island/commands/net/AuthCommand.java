package com.gamefocal.island.commands.net;

import com.gamefocal.island.DedicatedServer;
import com.gamefocal.island.entites.net.*;
import com.gamefocal.island.models.Player;
import com.gamefocal.island.service.CommandService;
import com.gamefocal.island.service.PlayerService;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

@Command(name = "auth", sources = "tcp")
public class AuthCommand extends HiveCommand {
    @Override
    public void onCommand(HiveNetMessage message, CommandSource source, HiveNetConnection netConnection) {

        if (message.args.length > 0) {
            Player p = Player.getFromId(Player.class, message.args[0]);

            if (p != null) {

                System.out.println("Returning Player #" + p.getPkId() + " has joined");

                // Player exist
                p.setLastSeenAt((int) System.currentTimeMillis());
                p.save();
            } else {
                // No player is set...
                p = Player.newObject(Player.class);
                p.setPkIdOverride(message.args[0]);
                p.setLastSeenAt((int) System.currentTimeMillis());
                p.setFirstSeenAt((int) System.currentTimeMillis());
                p.setInventory("");
                p.setHealth(100.00f);
                p.setHunger(100.00f);
                p.setThirst(100.00f);
                p.setEnergy(100.00f);
                p.setLocation("0,0,0");
                p.save();
                p.refresh(Player.class);

                System.out.println("New Player #" + p.getPkId() + " has joined");

            }

            UUID session = UUID.randomUUID();

            netConnection.setPlayer(p);

            // Register the player with the server
            DedicatedServer.get(PlayerService.class).getPlayers().put(session.toString(), netConnection);

            try {
                netConnection.getSocket().getOutputStream().write(("reg|" + session.toString()).getBytes(StandardCharsets.UTF_8));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
