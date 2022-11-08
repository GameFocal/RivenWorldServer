package com.gamefocal.island.game;

import com.gamefocal.island.entites.net.HiveNetConnection;
import com.gamefocal.island.models.GameEntityModel;
import com.gamefocal.island.service.DataService;

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
