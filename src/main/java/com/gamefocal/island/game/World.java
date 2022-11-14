package com.gamefocal.island.game;

import com.gamefocal.island.DedicatedServer;
import com.gamefocal.island.entites.net.HiveNetConnection;
import com.gamefocal.island.entites.net.HiveNetMessage;
import com.gamefocal.island.events.EntityDespawnEvent;
import com.gamefocal.island.events.EntitySpawnEvent;
import com.gamefocal.island.game.entites.resources.TreeResource;
import com.gamefocal.island.game.util.Location;
import com.gamefocal.island.models.GameEntityModel;
import com.gamefocal.island.service.DataService;
import com.gamefocal.island.service.NetworkService;
import com.gamefocal.island.service.PlayerService;

import java.sql.SQLException;
import java.util.Hashtable;
import java.util.List;
import java.util.UUID;

public class World {

    public Hashtable<UUID, GameEntityModel> entites = new Hashtable<>();

    public World() {
        /*
         * Load the world into Memory
         * */
        try {
            List<GameEntityModel> entites = DataService.gameEntities.queryForAll();

            for (GameEntityModel model : entites) {
                this.entites.put(model.uuid, model);
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void loadWorldForPlayer(HiveNetConnection connection) {
        // Send the spawn command for the entity
        try {
            for (GameEntityModel model : DataService.gameEntities.queryForAll()) {
                model.sync();
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        // Send move command for other players
        for (HiveNetConnection c : DedicatedServer.get(PlayerService.class).players.values()) {
            // Send move event to them for everyone else
            if (connection.getUuid() != c.getUuid()) {
                HiveNetMessage message = new HiveNetMessage();
                message.cmd = "plmv";
                message.args = new String[]{c.getUuid().toString(), String.valueOf(c.getVoiceId()), c.getPlayer().location.toString()};
                connection.sendUdp(message.toString());
            }
        }
    }

    public static void generateNewWorld() {
        // Generate a new world...
        World world = DedicatedServer.instance.getWorld();

        world.spawn(new TreeResource(), new Location(0, 0, 0));
    }

    public void spawn(GameEntity entity, Location location) {
        new EntitySpawnEvent(entity, location).call();
    }

    public void despawn(UUID uuid) {
        if (this.entites.containsKey(uuid)) {
            new EntityDespawnEvent(this.entites.get(uuid));
        }
    }

    public void save() {
        for (GameEntityModel model : this.entites.values()) {
            try {
                DataService.gameEntities.update(model);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }

    public boolean isFreshWorld() {
        return this.entites.size() == 0;
    }

}
