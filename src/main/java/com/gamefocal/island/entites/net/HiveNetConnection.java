package com.gamefocal.island.entites.net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class HiveNetConnection {

    private Socket socket;

    private BufferedReader bufferedReader;

    private String line;

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
}
