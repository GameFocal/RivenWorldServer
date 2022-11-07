package com.gamefocal.island.entites.net;

import com.gamefocal.island.entites.orm.models.Player;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class HiveNetConnection {

    private Socket socket;

    private BufferedReader bufferedReader;

    private String line;

    private Player player;

    public HiveNetConnection(Socket socket) throws IOException {
        this.socket = socket;
        this.bufferedReader = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
    }

    public boolean hasData() throws IOException {
        return this.socket.getInputStream().available() > 0;
    }

    public String readLine() throws IOException {
        while ((this.line = this.bufferedReader.readLine()) != null) {
            return this.line;
        }

        return null;
    }

    public Socket getSocket() {
        return socket;
    }

    public BufferedReader getBufferedReader() {
        return bufferedReader;
    }

    public String getLine() {
        return line;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }
}
