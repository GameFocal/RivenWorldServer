package com.gamefocal.island.commands.net;

import com.gamefocal.island.DedicatedServer;
import com.gamefocal.island.entites.net.*;
import com.gamefocal.island.models.PlayerModel;
import com.gamefocal.island.service.DataService;
import com.gamefocal.island.service.PlayerService;
import org.joda.time.DateTime;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

@Command(name = "auth", sources = "tcp")
public class AuthCommand extends HiveCommand {
    @Override
    public void onCommand(HiveNetMessage message, CommandSource source, HiveNetConnection netConnection) throws Exception {

        if (message.args.length > 0) {
            PlayerModel p = DataService.players.queryForId(message.args[0]);

            UUID session = UUID.randomUUID();

            if (p != null) {
                System.out.println("Returning Player #" + p.id + " has joined");

                // Player exist
                p.lastSeenAt = new DateTime();
            } else {
                // No player is set...
                p = new PlayerModel();
                p.id = message.args[0];
                p.lastSeenAt = new DateTime();
                p.firstSeenAt = new DateTime();
                p.uuid =session.toString();

                DataService.players.createIfNotExists(p);

                System.out.println("New Player #" + p.id + " has joined");

            }

            netConnection.setPlayer(p);
            netConnection.setUuid(session);

            // Register the player with the server
            DedicatedServer.get(PlayerService.class).players.put(session, netConnection);

            try {
                netConnection.getSocket().getOutputStream().write(("reg|" + session.toString()).getBytes(StandardCharsets.UTF_8));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
