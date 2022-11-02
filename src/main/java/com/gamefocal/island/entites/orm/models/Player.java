package com.gamefocal.island.entites.orm.models;

import com.esotericsoftware.kryonet.Connection;
import com.gamefocal.island.entites.orm.models.auto._Player;
import com.google.gson.JsonElement;

public class Player extends _Player {

    private static final long serialVersionUID = 1L;

    private Connection connection;


    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    @Override
    public JsonElement toJson() {
        return null;
    }
}
