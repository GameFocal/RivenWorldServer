package com.gamefocal.rivenworld.entites.net;

import com.gamefocal.rivenworld.DedicatedServer;
import com.gamefocal.rivenworld.service.CommandService;
import lowentry.ue4.classes.sockets.LatentResponse;
import lowentry.ue4.classes.sockets.SocketClient;
import lowentry.ue4.classes.sockets.SocketServer;
import lowentry.ue4.classes.sockets.SocketServerListener;
import lowentry.ue4.library.LowEntry;

import java.io.IOException;
import java.nio.ByteBuffer;

public class HiveNetListener implements SocketServerListener {

    private HiveNetServer server;

    public HiveNetListener(HiveNetServer server) {
        this.server = server;
    }

    @Override
    public void clientConnected(SocketServer socketServer, SocketClient socketClient) {
        System.out.println(socketClient.hashCode());
        System.out.println("CLIENT CONNECTED!");

        System.out.println("[NET]: New Player Connecting with Hash " + socketClient.hashCode());

        try {
            this.server.getConnections().add(new HiveNetConnection(socketClient));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void clientDisconnected(SocketServer socketServer, SocketClient socketClient) {
        System.out.println("CLIENT LOST");

        // TODO: Remove player from list and then remove connection in HiveNetConnection

    }

    @Override
    public void receivedConnectionValidation(SocketServer socketServer, SocketClient socketClient) {

    }

    @Override
    public boolean startReceivingUnreliableMessage(SocketServer socketServer, SocketClient socketClient, int i) {
        return (i <= 1024);
    }

    @Override
    public void receivedUnreliableMessage(SocketServer socketServer, SocketClient socketClient, ByteBuffer byteBuffer) {
        HiveNetConnection connection = this.server.getConnectionFromClient(socketClient);
        if (connection != null) {
            if (connection.getMsgToken() != null) {
                byte[] data = LowEntry.decryptAes(LowEntry.getBytesFromByteBuffer(byteBuffer), connection.getMsgToken(), true);
                String msg = LowEntry.bytesToStringUtf8(data);
                DedicatedServer.get(CommandService.class).handleTelemetry(msg);
            }
        }
    }

    @Override
    public boolean startReceivingMessage(SocketServer socketServer, SocketClient socketClient, int i) {
        return (i <= (10 * 1024)); // this will only allow packets of 10KB and less
    }

    @Override
    public void receivedMessage(SocketServer socketServer, SocketClient socketClient, byte[] bytes) {
        HiveNetConnection connection = this.server.getConnectionFromClient(socketClient);
        if (connection != null) {

            byte[] data = new byte[0];
            if (connection.getMsgToken() != null) {
                // Decipher with AES
                data = LowEntry.decryptAes(bytes, connection.getMsgToken(), true);
            } else {
                // Decipher with RSA
                data = LowEntry.decryptRsa(bytes, DedicatedServer.licenseManager.getPrivateKey());
            }

            String msg = LowEntry.bytesToStringUtf8(data);
            DedicatedServer.get(CommandService.class).handleCommand(msg, CommandSource.NET_TCP, connection);
        }
    }

    @Override
    public boolean startReceivingFunctionCall(SocketServer socketServer, SocketClient socketClient, int i) {
        System.out.println("[" + Thread.currentThread().getName() + "] Start Receiving Function Call");
        return (i <= (10 * 1024)); // this will only allow packets of 10KB and less
    }

    @Override
    public byte[] receivedFunctionCall(SocketServer socketServer, SocketClient socketClient, byte[] bytes) {
        System.out.println("[" + Thread.currentThread().getName() + "] Received Function Call: \"" + LowEntry.bytesToStringUtf8(bytes) + "\"");
        return null;
    }

    @Override
    public boolean startReceivingLatentFunctionCall(SocketServer socketServer, SocketClient socketClient, int i) {
        System.out.println("[" + Thread.currentThread().getName() + "] Start Receiving Latent Function Call");
        return (i <= (10 * 1024)); // this will only allow packets of 10KB and less
    }

    @Override
    public void receivedLatentFunctionCall(SocketServer socketServer, SocketClient socketClient, byte[] bytes, LatentResponse latentResponse) {
//        System.out.println("[" + Thread.currentThread().getName() + "] Received Latent Function Call: \"" + LowEntry.bytesToStringUtf8(bytes) + "\"");

        /*
        * Ping reply for the server list :)
        * */
        String cmd = LowEntry.bytesToStringUtf8(bytes);
        if (cmd.equalsIgnoreCase("ping")) {
            latentResponse.done(LowEntry.stringToBytesUtf8("pong"));
        }

//        latentResponse.done(null);
    }
}
