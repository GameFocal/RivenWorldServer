package com.gamefocal.island.commands.net;

import com.gamefocal.island.DedicatedServer;
import com.gamefocal.island.entites.net.*;
import com.gamefocal.island.game.util.Location;
import com.gamefocal.island.models.GameMetaModel;
import com.gamefocal.island.models.PlayerModel;
import com.gamefocal.island.service.DataService;
import com.gamefocal.island.service.PlayerService;
import com.gamefocal.island.service.VoipService;
import org.joda.time.DateTime;

import java.util.UUID;

@Command(name = "auth", sources = "tcp")
public class NetAuth extends HiveCommand {
    @Override
    public void onCommand(HiveNetMessage message, CommandSource source, HiveNetConnection netConnection) throws Exception {

        if (message.args.length > 0) {
            PlayerModel p = DataService.players.queryForId(message.args[0]);

            if (p != null) {
                System.out.println("Returning Player #" + p.id + " has joined");

                // Player exist
                p.lastSeenAt = new DateTime();
//                if
//                p.displayName = message.args[1];
            } else {
                // No player is set...
                p = new PlayerModel();
                p.id = message.args[0];
                p.lastSeenAt = new DateTime();
                p.firstSeenAt = new DateTime();
                p.uuid = UUID.randomUUID().toString();
                p.location = new Location(0, 0, 0);
//                p.displayName = message.args[1];

                DataService.players.createIfNotExists(p);

                System.out.println("New Player #" + p.id + " has joined");
            }

            p.inventory.takeOwnership(netConnection, true);

            netConnection.setPlayer(p);
            netConnection.setUuid(UUID.fromString(p.uuid));

            // Register the player with the server
            DedicatedServer.get(PlayerService.class).players.put(UUID.fromString(p.uuid), netConnection);

            int voiceId = DedicatedServer.get(VoipService.class).registerNewVoipClient(netConnection);

            netConnection.sendTcp("reg|" + p.uuid + "|" + voiceId + "|" + p.inventory.getUuid().toString());
        }
    }
}
