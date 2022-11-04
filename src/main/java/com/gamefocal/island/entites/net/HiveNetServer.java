package com.gamefocal.island.entites.net;

import com.gamefocal.island.DedicatedServer;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ConcurrentLinkedQueue;

public class HiveNetServer {

    private int tcpPort;

    private int udpPort;

    private ServerSocket tcpServer;

    private DatagramSocket udpSocket;

    private Thread tcpConnectionListener;

    private Thread tcpReadThread;

    private Thread udpReadThread;

    private ConcurrentLinkedQueue<HiveNetConnection> connections = new ConcurrentLinkedQueue<>();

    public HiveNetServer(int tcpPort, int udpPort) {
        this.tcpPort = tcpPort;
        this.udpPort = udpPort;

        System.out.println("Starting TCP Listener...");
        this.startTcpServer();

        System.out.println("Starting Client Read Loop...");
        this.startTcpRead();

        System.out.println("Starting UDP Listener...");
        this.startUdpServer();
    }

    private void startTcpServer() {
        this.tcpConnectionListener = new Thread(() -> {

            try {
                this.tcpServer = new ServerSocket(this.tcpPort);

                while (true) {
                    Socket socket = this.tcpServer.accept();
                    socket.setKeepAlive(true);
                    socket.setSoTimeout(60000);
                    socket.setReuseAddress(true);

                    System.out.println("New Connection " + socket.getRemoteSocketAddress().toString());

                    Thread.sleep(150);

                    System.out.println(">> init");
                    socket.getOutputStream().write("init".getBytes(StandardCharsets.UTF_8));

//                    socket.getOutputStream().write(("motd|" + DedicatedServer.instance.getConfigFile().getConfig().get("motd").getAsString()).getBytes(StandardCharsets.UTF_8));

                    // Make connection here and save it.
                    this.connections.add(new HiveNetConnection(socket));
                }

            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        });
        this.tcpConnectionListener.start();
    }

    public void startTcpRead() {
        this.tcpReadThread = new Thread(() -> {
            try {
                while (true) {
                    for (HiveNetConnection s : this.connections) {
                        if (s.hasData()) {
                            String line = s.readLine();
                            // TODO: Trigger event here

                            System.out.println("TCP-IN: " + line);
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        this.tcpReadThread.start();
    }

    private void startUdpServer() {
        // TODO: Add UDP Here
    }

}
