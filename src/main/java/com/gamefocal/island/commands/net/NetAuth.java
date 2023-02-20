package com.gamefocal.island.commands.net;

import com.gamefocal.island.DedicatedServer;
import com.gamefocal.island.entites.net.*;
import com.gamefocal.island.game.util.Location;
import com.gamefocal.island.models.PlayerModel;
import com.gamefocal.island.service.DataService;
import com.gamefocal.island.service.PlayerService;
import com.gamefocal.island.service.VoipService;
import lowentry.ue4.library.LowEntry;
import org.joda.time.DateTime;

import java.util.UUID;

@Command(name = "auth", sources = "tcp")
public class NetAuth extends HiveCommand {
    @Override
    public void onCommand(HiveNetMessage message, CommandSource source, HiveNetConnection netConnection) throws Exception {

        System.out.println(message);
        // TODO: Send request to hive for the player data, including the public key for data sending.
        if (!DedicatedServer.licenseManager.getPlayerData(message.args[0], netConnection)) {
            System.err.println("Failed to authenticate player... disconnecting them.");
            netConnection.getSocketClient().disconnect();
            // TODO: Kick
            return;
        }

        if (message.args.length != 2) {
            System.err.println("Invalid Auth Packet... disconnecting them.");
            netConnection.getSocketClient().disconnect();
            return;
        }

        netConnection.setMsgToken(LowEntry.createAesKey(LowEntry.stringToBytesUtf8(message.args[1])));

        try {

            String playerHiveId = netConnection.getHiveId();

            PlayerModel p = DataService.players.queryForId(playerHiveId);

            if (p != null) {
                System.out.println("Returning Player #" + p.id + " has joined");

                // Player exist
                p.lastSeenAt = new DateTime();
//                if
                p.displayName = netConnection.getHiveDisplayName();
            } else {
                // No player is set...
                p = new PlayerModel();
                p.id = playerHiveId;
                p.lastSeenAt = new DateTime();
                p.firstSeenAt = new DateTime();
                p.uuid = UUID.randomUUID().toString();
                p.location = new Location(0, 0, 0);
                p.displayName = netConnection.getHiveDisplayName();

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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
