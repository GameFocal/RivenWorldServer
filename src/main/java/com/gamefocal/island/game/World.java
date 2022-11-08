package com.gamefocal.island.game;

import com.gamefocal.island.DedicatedServer;
import com.gamefocal.island.entites.net.HiveNetConnection;
import com.gamefocal.island.entites.net.HiveNetMessage;
import com.gamefocal.island.models.GameEntityModel;
import com.gamefocal.island.service.DataService;
import com.gamefocal.island.service.NetworkService;
import com.gamefocal.island.service.PlayerService;

import java.sql.SQLException;
import java.util.Hashtable;
import java.util.List;
import java.util.UUID;

public class World {

    private Hashtable<UUID, GameEntityModel> entites = new Hashtable<>();

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
        // TODO: Load the world using TCP Net to the Player

        // Send the spawn command for the entity

        // Send move command for other players
        for (HiveNetConnection c : DedicatedServer.get(PlayerService.class).players.values()) {
            // Send move event to them for everyone else
            if (connection.getUuid() != c.getUuid()) {
                HiveNetMessage message = new HiveNetMessage();
                message.cmd = "plmv";
                message.args = new String[]{c.getUuid().toString(), c.getPlayer().location.toString()};
                connection.sendUdp(message.toString());
            }
        }
    }

    public static void generateNewWorld() {
        // Generate a new world...

        // TODO: Spawn Resource Nodes
    }

    public void spawn(GameEntity entity) {
        // TODO: Spawn the new entity
    }

    public void despawn(UUID uuid) {
        // TODO: Despawn the entity
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

}
