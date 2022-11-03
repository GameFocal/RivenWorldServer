package com.gamefocal.island.service;

import com.gamefocal.island.DedicatedServer;
import com.gamefocal.island.entites.net.HiveNetServer;
import com.gamefocal.island.entites.service.HiveService;
import com.google.auto.service.AutoService;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import javax.inject.Singleton;

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
}
