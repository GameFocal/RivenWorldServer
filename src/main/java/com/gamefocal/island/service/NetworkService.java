package com.gamefocal.island.service;

import com.gamefocal.island.DedicatedServer;
import com.gamefocal.island.entites.net.HiveNetConnection;
import com.gamefocal.island.entites.net.HiveNetMessage;
import com.gamefocal.island.entites.net.HiveNetServer;
import com.gamefocal.island.entites.service.HiveService;
import com.google.auto.service.AutoService;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

@Singleton
@AutoService(HiveService.class)
public class NetworkService implements HiveService<NetworkService> {

    private int mainPort;
    private int udpPort;

    private HiveNetServer server;

    @Override
    public void init() {
        this.mainPort = DedicatedServer.instance.getConfigFile().getConfig().get("port").getAsInt();
        this.udpPort = this.mainPort + 10;

        System.out.println("Starting Networking Service...");
        System.out.println("TCP: " + this.mainPort + ", UDP: " + this.udpPort);

        this.server = new HiveNetServer(this.mainPort, this.udpPort);
    }

    public void broadcast(HiveNetMessage message, UUID from) {

        String m = DedicatedServer.get(CommandService.class).msgToString(message);

        for (HiveNetConnection connection : this.server.getConnections()) {
            if (from == null || from != connection.getUuid()) {
                try {
                    connection.getSocket().getOutputStream().write(m.getBytes(StandardCharsets.UTF_8));
                } catch (IOException e) {
                    this.server.getConnections().remove(connection);
                }
            }
        }
    }

    public DatagramSocket getUdpSocket() {
        return this.server.getUdpSocket();
    }

    public void broadcastUdp(HiveNetMessage message, UUID from) {
        String m = DedicatedServer.get(CommandService.class).msgToString(message);

        for (HiveNetConnection connection : this.server.getConnections()) {
            if (from == null || from != connection.getUuid()) {
                connection.sendUdp(m);
            }
        }
    }

}
