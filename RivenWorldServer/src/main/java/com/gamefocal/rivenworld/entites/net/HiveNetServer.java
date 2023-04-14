package com.gamefocal.rivenworld.entites.net;

import lowentry.ue4.classes.sockets.SocketClient;
import lowentry.ue4.classes.sockets.SocketServer;

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
//                    SocketServer.setDebuggingEnabled();
                    server = new SocketServer(true, this.tcpPort, this.tcpPort, new HiveNetListener(this));

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
        return this.getConnectionFromHashId(client.getClientId());
    }

    public HiveNetConnection getConnectionFromHashId(int hashId) {
        for (HiveNetConnection connection : this.connections) {
            if(connection.getSocketClient().getClientId() == hashId) {
                return connection;
            }
        }
        return null;
    }

    public ConcurrentLinkedQueue<HiveNetConnection> getConnections() {
        return connections;
    }
}
