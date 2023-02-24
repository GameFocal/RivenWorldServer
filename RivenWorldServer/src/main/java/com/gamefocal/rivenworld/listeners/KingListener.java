package com.gamefocal.rivenworld.listeners;

import com.badlogic.gdx.math.Intersector;
import com.gamefocal.rivenworld.DedicatedServer;
import com.gamefocal.rivenworld.entites.events.EventHandler;
import com.gamefocal.rivenworld.entites.events.EventInterface;
import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.events.game.ServerReadyEvent;
import com.gamefocal.rivenworld.events.game.ServerWorldSyncEvent;
import com.gamefocal.rivenworld.events.world.WorldGenerateEvent;
import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.entites.special.KingWarChest;
import com.gamefocal.rivenworld.models.GameEntityModel;
import com.gamefocal.rivenworld.models.GameMetaModel;
import com.gamefocal.rivenworld.service.KingService;

import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedQueue;

public class KingListener implements EventInterface {

    private static ConcurrentLinkedQueue<UUID> nearThrone = new ConcurrentLinkedQueue<>();

    @EventHandler
    public void onWorldGenerate(ServerReadyEvent event) {
        /*
         * Load the inventory from the db
         * */
        if (!GameMetaModel.hasMeta("king-warchest")) {
            // Spawn the warchest
            System.out.println("Placed the King's Warchest");

            KingWarChest warChest = new KingWarChest();
            GameEntityModel model = DedicatedServer.instance.getWorld().spawn(warChest, KingService.warChestLocation);
            GameMetaModel.setMetaValue("king-warchest", model.uuid.toString());

            KingService.warChest = model.getEntity(KingWarChest.class);
        } else {
            String uuid = GameMetaModel.getMetaValue("king-warchest", null);
            if (uuid != null) {
                UUID u = UUID.fromString(uuid);
                GameEntityModel e = DedicatedServer.instance.getWorld().getEntityFromId(u);
                KingService.warChest = e.getEntity(KingWarChest.class);
            }
        }
    }

    @EventHandler
    public void onWorldSync(ServerWorldSyncEvent event) {

        HiveNetConnection connection = event.getConnection();

        if (KingService.throneBound().contains(connection.getBoundingBox()) || KingService.throneBound().intersects(connection.getBoundingBox())) {
            // In view of the throne
            connection.setHelpboxText("[e] Claim the throne");
            nearThrone.add(connection.getUuid());
        } else if (KingService.managmentBound().contains(connection.getBoundingBox()) || KingService.managmentBound().intersects(connection.getBoundingBox())) {
            if (KingService.isTheKing != null && KingService.isTheKing.uuid.equalsIgnoreCase(connection.getPlayer().uuid)) {
                connection.setHelpboxText("[e] Manage the Kingdom");
                nearThrone.add(connection.getUuid());
            } else {
                connection.setHelpboxText("Only the king can manage the kingdom");
                nearThrone.add(connection.getUuid());
            }
        } else if (nearThrone.contains(connection.getUuid())) {
            connection.setHelpboxText(null);
        }

    }

}
