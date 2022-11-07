package com.gamefocal.island.entites.orm.models;

import com.gamefocal.island.entites.orm.models.auto._Player;
import com.google.gson.JsonElement;

import java.net.DatagramSocket;
import java.net.Socket;

public class Player extends _Player {

    private static final long serialVersionUID = 1L;

    public Socket tcpSocket;

    public DatagramSocket udpSocket;

    @Override
    public JsonElement toJson() {
        return null;
    }
}
