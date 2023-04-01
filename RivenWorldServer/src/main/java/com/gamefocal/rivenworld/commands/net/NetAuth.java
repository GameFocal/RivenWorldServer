package com.gamefocal.rivenworld.commands.net;

import com.gamefocal.rivenworld.DedicatedServer;
import com.gamefocal.rivenworld.entites.net.*;
import com.gamefocal.rivenworld.game.inventory.Inventory;
import com.gamefocal.rivenworld.game.inventory.InventoryStack;
import com.gamefocal.rivenworld.game.inventory.crafting.CraftingQueue;
import com.gamefocal.rivenworld.game.inventory.equipment.EquipmentSlots;
import com.gamefocal.rivenworld.game.items.clothes.chest.cloth.SimpleClothShirt;
import com.gamefocal.rivenworld.game.items.clothes.feet.leather.SimpleLeatherShoes;
import com.gamefocal.rivenworld.game.items.clothes.legs.cloth.SimpleClothLegs;
import com.gamefocal.rivenworld.game.util.Location;
import com.gamefocal.rivenworld.models.PlayerModel;
import com.gamefocal.rivenworld.service.DataService;
import com.gamefocal.rivenworld.service.NetworkService;
import com.gamefocal.rivenworld.service.PlayerService;
import com.gamefocal.rivenworld.service.VoipService;
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
                p.location = null;
                p.displayName = netConnection.getHiveDisplayName();
                p.inventory = new Inventory(26);
                p.inventory.setHasHotBar(true);
                p.inventory.setHotBarSize(6);
                p.inventory.setCraftingQueue(new CraftingQueue(6));

                EquipmentSlots equipmentSlots = new EquipmentSlots();
                // Equipment Default
                equipmentSlots.chest = new InventoryStack(new SimpleClothShirt(),1);
                equipmentSlots.legs = new InventoryStack(new SimpleClothLegs(),1);
                equipmentSlots.feet = new InventoryStack(new SimpleLeatherShoes(),1);

                p.equipmentSlots = equipmentSlots;

                DataService.players.createIfNotExists(p);

                System.out.println("New Player #" + p.id + " has joined");
            }

            p.inventory.takeOwnership(netConnection, true);

            netConnection.setPlayer(p);
            netConnection.setUuid(UUID.fromString(p.uuid));

            // Register the player with the server
            DedicatedServer.get(PlayerService.class).players.put(UUID.fromString(p.uuid), netConnection);

            int voiceId = DedicatedServer.get(VoipService.class).registerNewVoipClient(netConnection);

            netConnection.sendTcp("init|");

            netConnection.sendTcp("reg|" + p.uuid + "|" + voiceId + "|" + p.inventory.getUuid().toString() + "|" + netConnection.getNetAppearance().toString());

//            netConnection.sendUdp("nudpc|");

            netConnection.setNetworkMode(NetworkMode.TCP_UDP);
            DedicatedServer.get(NetworkService.class).checkUdpSupportForClient(netConnection);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
