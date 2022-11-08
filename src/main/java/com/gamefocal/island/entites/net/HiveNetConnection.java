package com.gamefocal.island.entites.net;

import com.gamefocal.island.DedicatedServer;
import com.gamefocal.island.models.PlayerModel;
import com.gamefocal.island.service.NetworkService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Socket;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

public class HiveNetConnection {

    private UUID uuid;

    private Socket socket;

    private BufferedReader bufferedReader;

    private String line;

    private PlayerModel player;

    private DatagramPacket udpOut;

    private DatagramSocket localSocket;

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

    public PlayerModel getPlayer() {
        return player;
    }

    public void setPlayer(PlayerModel player) {
        this.player = player;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public DatagramPacket getUdpOut() {
        return udpOut;
    }

    public void setUdpOut(DatagramPacket udpOut) {
        this.udpOut = udpOut;
    }

    public DatagramSocket getLocalSocket() {
        return localSocket;
    }

    public void setLocalSocket(DatagramSocket localSocket) {
        this.localSocket = localSocket;
    }

    public void sendUdp(String msg) {
        if (this.getUdpOut() != null) {
            DatagramPacket packet = this.getUdpOut();
            packet.setData(msg.getBytes(StandardCharsets.UTF_8));
            packet.setLength(msg.getBytes(StandardCharsets.UTF_8).length);

            try {
                DedicatedServer.get(NetworkService.class).getUdpSocket().send(packet);
            } catch (IOException e) {
                e.printStackTrace();
            }

//            if (this.getLocalSocket() == null) {
//                try {
//                    this.setLocalSocket(new DatagramSocket());
//                } catch (SocketException e) {
//                    e.printStackTrace();
//                }
//            }

//            if (this.getLocalSocket() != null) {
//                try {
//                    this.getLocalSocket().send(packet);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
        }
    }
}
