package com.gamefocal.island.entites.net;

import com.gamefocal.island.DedicatedServer;
import com.gamefocal.island.service.CommandService;
import lowentry.ue4.classes.sockets.SocketClient;
import lowentry.ue4.classes.sockets.SocketServer;

import java.io.IOException;
import java.net.*;
import java.util.concurrent.ConcurrentLinkedQueue;

public class HiveNetServer {

    private int tcpPort;

    private ConcurrentLinkedQueue<HiveNetConnection> connections = new ConcurrentLinkedQueue<>();

    private SocketServer server;

    public HiveNetServer(int tcpPort) {
        this.tcpPort = tcpPort;

        try {
            new Thread(() -> {
                System.out.println("Starting Net Sockets...");
                try {
                    SocketServer.setDebuggingEnabled();
                    server = new SocketServer(true, this.tcpPort, this.tcpPort+1, new HiveNetListener(this));

                    System.out.println("Listing " + server);

                    while (true) {
                        server.listen();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public HiveNetConnection getConnectionFromClient(SocketClient client) {
        return this.getConnectionFromHashId(client.hashCode());
    }

    public HiveNetConnection getConnectionFromHashId(int hashId) {
        for (HiveNetConnection connection : this.connections) {
            if(connection.getSocketClient().hashCode() == hashId) {
                return connection;
            }
        }
        return null;
    }

    public ConcurrentLinkedQueue<HiveNetConnection> getConnections() {
        return connections;
    }
}
