package com.gamefocal.island.service;

import com.gamefocal.island.DedicatedServer;
import com.gamefocal.island.entites.net.HiveNetConnection;
import com.gamefocal.island.entites.net.HiveNetMessage;
import com.gamefocal.island.entites.net.HiveNetServer;
import com.gamefocal.island.entites.service.HiveService;
import com.google.auto.service.AutoService;
import org.apache.commons.lang3.tuple.Pair;

import javax.inject.Singleton;
import java.net.DatagramSocket;
import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedQueue;

@Singleton
@AutoService(HiveService.class)
public class NetworkService implements HiveService<NetworkService> {

    public ConcurrentLinkedQueue<Pair<HiveNetConnection, byte[]>> tcpOutbound = new ConcurrentLinkedQueue<>();
    public ConcurrentLinkedQueue<Pair<HiveNetConnection, byte[]>> udpOutbound = new ConcurrentLinkedQueue<>();
    private int mainPort;
    private int udpPort;
    private HiveNetServer server;

    @Override
    public void init() {
        this.mainPort = DedicatedServer.instance.getConfigFile().getConfig().get("port").getAsInt();
        this.udpPort = this.mainPort + 10;

        System.out.println("Starting Networking Service...");
        System.out.println("TCP: " + this.mainPort + ", UDP: " + (this.mainPort + 1));

        this.server = new HiveNetServer(this.mainPort);
    }

    public void broadcast(HiveNetMessage message, UUID from) {
        String m = DedicatedServer.get(CommandService.class).msgToString(message);

        for (HiveNetConnection connection : this.server.getConnections()) {
            if (from == null || from != connection.getUuid()) {
//                    connection.getSocket().getOutputStream().write(m.getBytes(StandardCharsets.UTF_8));
                connection.sendTcp(m);
            }
        }
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
