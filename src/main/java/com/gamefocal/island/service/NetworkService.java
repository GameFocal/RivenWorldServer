package com.gamefocal.island.service;

import com.esotericsoftware.kryonet.Server;
import com.gamefocal.island.DedicatedServer;
import com.gamefocal.island.DedicatedServerListener;
import com.gamefocal.island.entites.net.HiveNetMessage;
import com.gamefocal.island.entites.net.HiveNetSerializer;
import com.gamefocal.island.entites.service.HiveService;
import com.google.auto.service.AutoService;

import javax.inject.Singleton;
import java.io.IOException;

@Singleton
@AutoService(HiveService.class)
public class NetworkService implements HiveService<NetworkService> {

    private int mainPort;
    private int udpPort;

    @Override
    public void init() {

        this.mainPort = DedicatedServer.instance.getConfigFile().getConfig().get("port").getAsInt();
        this.udpPort = this.mainPort + 1;

        System.out.println("Starting Networking Service...");
        System.out.println("TCP: " + this.mainPort + ", UDP: " + this.udpPort);

        new Thread(() -> {
            // Startup the server
            Server server = new Server(1024, 1024, new HiveNetSerializer());

            // Register the System
            server.getKryo().register(HiveNetMessage.class, new HiveNetSerializer());

            server.start();
            try {
                server.bind(this.mainPort, this.udpPort);

                server.addListener(new DedicatedServerListener());

            } catch (IOException e) {

                System.err.println("Failed to bind to port " + this.mainPort + ", may already be in use.");

                System.exit(500);
                e.printStackTrace();
            }
        }).start();
    }
}
