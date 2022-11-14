package com.gamefocal.island.entites.net;

import com.gamefocal.island.DedicatedServer;
import com.gamefocal.island.service.CommandService;
import fr.devnied.bitlib.BytesUtils;

import java.io.IOException;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ConcurrentLinkedQueue;

public class HiveNetServer {

    private int tcpPort;

    private int udpPort;

    private ServerSocket tcpServer;

    private DatagramSocket udpSocket;

    private DatagramSocket rtpSocket;

    private Thread tcpConnectionListener;

    private Thread tcpReadThread;

    private Thread udpReadThread;

    private Thread voiceThread;

    private ConcurrentLinkedQueue<HiveNetConnection> connections = new ConcurrentLinkedQueue<>();

    public HiveNetServer(int tcpPort, int udpPort) {
        this.tcpPort = tcpPort;
        this.udpPort = udpPort;

        System.out.println("Starting TCP Listener...");
        this.startTcpServer();

        System.out.println("Starting Client Read Loop...");
        this.startTcpRead();

        System.out.println("Starting UDP Listener...");
        try {
            this.startUdpServer();
        } catch (SocketException e) {
            e.printStackTrace();
        }

        System.out.println("Starting VOIP Server...");
        try {
            this.startVoiceServer();
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    private void startTcpServer() {
        this.tcpConnectionListener = new Thread(() -> {

            try {
                this.tcpServer = new ServerSocket(this.tcpPort);

                while (DedicatedServer.isRunning) {
                    Socket socket = this.tcpServer.accept();
                    socket.setKeepAlive(true);
                    socket.setSoTimeout(60000);
                    socket.setReuseAddress(true);

                    System.out.println("New Connection " + socket.getRemoteSocketAddress().toString());

//                    Thread.sleep(150);

                    socket.getOutputStream().write("init".getBytes(StandardCharsets.UTF_8));

//                    socket.getOutputStream().write(("motd|" + DedicatedServer.instance.getConfigFile().getConfig().get("motd").getAsString()).getBytes(StandardCharsets.UTF_8));

                    // Make connection here and save it.
                    this.connections.add(new HiveNetConnection(socket));
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        this.tcpConnectionListener.start();
    }

    public void startTcpRead() {
        this.tcpReadThread = new Thread(() -> {
            try {
                while (DedicatedServer.isRunning) {
                    for (HiveNetConnection s : this.connections) {
                        if (s.hasData()) {
                            String line = s.readLine();

                            DedicatedServer.get(CommandService.class).handleCommand(line, CommandSource.NET_TCP, s);
                        }
                    }

                    Thread.sleep(5);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        this.tcpReadThread.start();
    }

    private void startUdpServer() throws SocketException {
        this.udpSocket = new DatagramSocket(this.tcpPort + 1);

        this.udpReadThread = new Thread(() -> {
            byte[] udpBuffer = new byte[65507];

            while (DedicatedServer.isRunning) {
                DatagramPacket packet
                        = new DatagramPacket(udpBuffer, udpBuffer.length);
                try {
                    udpSocket.receive(packet);

                    byte[] data = new byte[packet.getLength()];
                    for (int i = 0; i < data.length; i++) {
                        data[i] = packet.getData()[i];
                    }

                    DedicatedServer.get(CommandService.class).handleTelemetry(new String(data), packet);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                try {
                    Thread.sleep(5);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        this.udpReadThread.start();
    }

    private void startVoiceServer() throws SocketException {
        this.rtpSocket = new DatagramSocket(this.tcpPort + 2);
        this.voiceThread = new Thread(() -> {
            byte[] udpBuffer = new byte[65507];

            while (DedicatedServer.isRunning) {
                DatagramPacket packet
                        = new DatagramPacket(udpBuffer, udpBuffer.length);
                try {
                    rtpSocket.receive(packet);

                    byte[] data = new byte[packet.getLength()];
                    for (int i = 0; i < data.length; i++) {
                        data[i] = packet.getData()[i];
                    }

                    DedicatedServer.get(CommandService.class).handleVoice(data, packet);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                try {
                    Thread.sleep(5);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        this.voiceThread.start();
    }

    public ConcurrentLinkedQueue<HiveNetConnection> getConnections() {
        return connections;
    }

    public DatagramSocket getUdpSocket() {
        return udpSocket;
    }

    public DatagramSocket getRtpSocket() {
        return rtpSocket;
    }
}
