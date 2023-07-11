package com.gamefocal.rivenworld.service;

import com.gamefocal.rivenworld.DedicatedServer;
import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.entites.net.HiveNetMessage;
import com.gamefocal.rivenworld.entites.net.HiveNetServer;
import com.gamefocal.rivenworld.entites.net.NetworkMode;
import com.gamefocal.rivenworld.entites.service.HiveService;
import com.google.auto.service.AutoService;
import org.apache.commons.lang3.tuple.Pair;

import javax.inject.Singleton;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Singleton
@AutoService(HiveService.class)
public class NetworkService implements HiveService<NetworkService> {

    private int mainPort;
    private int udpPort;
    private HiveNetServer server;
    private ConcurrentHashMap<UUID, Pair<Long, NetworkMode>> check = new ConcurrentHashMap<>();

    @Override
    public void init() {
        this.mainPort = DedicatedServer.instance.getConfigFile().getConfig().get("port").getAsInt();
        this.udpPort = this.mainPort + 1;

        System.out.println("Starting Networking Service...");
        System.out.println("TCP: " + this.mainPort + ", UDP: " + this.udpPort);

        this.server = new HiveNetServer(this.mainPort);
    }

    public void checkUdpSupportForClient(HiveNetConnection connection) {
        connection.sendUdp("nudpc|" + System.currentTimeMillis());
//        this.check.put(connection.getUuid(), Pair.of(System.currentTimeMillis(), NetworkMode.INIT));
    }

//    public void processUdpReply(HiveNetConnection connection) {
//        if (this.check.containsKey(connection.getUuid())) {
//            this.check.remove(connection.getUuid());
//        }
//    }

//    public void checkValidationRequests() {
//        for (Map.Entry<UUID, Pair<Long, NetworkMode>> m : this.check.entrySet()) {
//            long time = TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - m.getValue().getLeft());
//            if (time >= 5) {
//                DedicatedServer.get(PlayerService.class).players.get(m.getKey()).setNetworkMode(NetworkMode.TCP_ONLY);
//                DedicatedServer.get(PlayerService.class).players.get(m.getKey()).sendTcp("utcp|");
//                this.check.remove(m.getKey());
//            }
//        }
//    }


    public HiveNetServer getServer() {
        return server;
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
