package com.gamefocal.rivenworld.entites.net;

import com.gamefocal.rivenworld.DedicatedServer;
import com.gamefocal.rivenworld.service.PlayerService;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import lowentry.ue4.classes.sockets.SocketClient;
import lowentry.ue4.classes.sockets.SocketServer;
import org.takes.Request;
import org.takes.Response;
import org.takes.Take;
import org.takes.facets.fork.FkRegex;
import org.takes.facets.fork.TkFork;
import org.takes.http.Exit;
import org.takes.http.FtBasic;
import org.takes.rs.RsText;

import java.io.IOException;
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
//                    server = new SocketServer(true, this.tcpPort, this.tcpPort, new HiveNetListener(this));

                    String addr = "0.0.0.0";
                    if (DedicatedServer.instance.getConfigFile().getConfig().has("ip")) {
                        addr = DedicatedServer.instance.getConfigFile().getConfig().get("ip").getAsString();
                    }

                    System.out.println("Binding to " + addr);

                    server = new SocketServer(addr, tcpPort, tcpPort, new HiveNetListener(this));
//                    server = new SocketServer(true, tcpPort, tcpPort, new HiveNetListener(this));

                    System.out.println("Listing " + server);

                    while (true) {
                        try {
                            server.listen();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
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
            if (connection.getSocketClient().hashCode() == hashId) {
                return connection;
            }
        }
        return null;
    }

    public ConcurrentLinkedQueue<HiveNetConnection> getConnections() {
        return connections;
    }
}
