package com.gamefocal.rivenworld.entites.net;

import com.badlogic.gdx.math.MathUtils;
import com.gamefocal.rivenworld.DedicatedServer;
import com.gamefocal.rivenworld.service.CommandService;
import com.gamefocal.rivenworld.service.DataService;
import com.gamefocal.rivenworld.service.PlayerService;
import lowentry.ue4.classes.sockets.LatentResponse;
import lowentry.ue4.classes.sockets.SocketClient;
import lowentry.ue4.classes.sockets.SocketServer;
import lowentry.ue4.classes.sockets.SocketServerListener;
import lowentry.ue4.library.LowEntry;
import org.joda.time.DateTime;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.sql.SQLException;

public class HiveNetListener implements SocketServerListener {

    private HiveNetServer server;

    public HiveNetListener(HiveNetServer server) {
        this.server = server;
    }

    @Override
    public void clientConnected(SocketServer socketServer, SocketClient socketClient) {
//        System.out.println(socketClient.hashCode());
//        System.out.println("CLIENT CONNECTED!");

        System.out.println("[NET]: New Player Connecting with Hash " + socketClient.hashCode());

        try {
            this.server.getConnections().add(new HiveNetConnection(socketClient));
            DedicatedServer.licenseManager.hb();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void clientDisconnected(SocketServer socketServer, SocketClient socketClient) {
        HiveNetConnection connection = this.server.getConnectionFromClient(socketClient);
        if (connection != null) {

            HiveNetConnection player = DedicatedServer.get(PlayerService.class).players.get(connection.getUuid());

            if (player != null && player.getPlayer() != null) {
                player.getPlayer().lastSeenAt = DateTime.now();
                    try {
                        DataService.players.update(player.getPlayer());
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                player.hide();
                DedicatedServer.get(PlayerService.class).players.remove(connection.getUuid());
                DedicatedServer.licenseManager.hb();

                /*
                 * Send a join msg
                 * */
                if (!connection.isAdmin()) {
                    DedicatedServer.sendChatMessageToAll(ChatColor.GREEN + "" + connection.getPlayer().displayName + " as left the game");
                }
            }

            this.server.getConnections().remove(connection);
        }
    }

    @Override
    public void receivedConnectionValidation(SocketServer socketServer, SocketClient socketClient) {
        HiveNetConnection connection = this.server.getConnectionFromClient(socketClient);
        if (connection != null) {
            connection.setLastTcpMsg(System.currentTimeMillis());
        }
    }

    @Override
    public boolean startReceivingUnreliableMessage(SocketServer socketServer, SocketClient socketClient, int i) {
        return true;
    }

    @Override
    public void receivedUnreliableMessage(SocketServer socketServer, SocketClient socketClient, ByteBuffer byteBuffer) {

        int type = byteBuffer.getInt();
        int clientId = byteBuffer.getInt();

        HiveNetConnection connection = this.server.getConnectionFromClient(socketClient);
        if (connection != null) {

            connection.setLastUdpMsg(System.currentTimeMillis());

            if (type == 2) {
                // VOIP Data
                connection.setLastVoipPacket(System.currentTimeMillis());

                byte[] voipData = LowEntry.getBytesFromByteBuffer(byteBuffer);

                float voiceDst = 25 * 100;
                for (HiveNetConnection peer : DedicatedServer.get(PlayerService.class).players.values()) {
                    if (!peer.getPlayer().uuid.equalsIgnoreCase(connection.getPlayer().uuid)) {
                        float playerDst = peer.getPlayer().location.dist(connection.getPlayer().location);

                        if (playerDst <= voiceDst) {
                            // Within Range
                            float volume = MathUtils.map(0, 25 * 100, 1, 0, playerDst);

                            peer.sendVOIPData(connection.getVoiceId(), volume, voipData);
                        }
                    }
                }

//                connection.sendVOIPData(1,voipData);
            } else {
                if (connection.getMsgToken() != null) {
                    byte[] data = LowEntry.decryptAes(LowEntry.getBytesFromByteBuffer(byteBuffer), connection.getMsgToken(), true);
                    String msg = LowEntry.bytesToStringUtf8(data);
                    DedicatedServer.get(CommandService.class).handleTelemetry(msg);
                }
            }
        }
    }

    @Override
    public boolean startReceivingMessage(SocketServer socketServer, SocketClient socketClient, int i) {
        return (i <= (10 * 1024)); // this will only allow packets of 10KB and less
    }

    @Override
    public void receivedMessage(SocketServer socketServer, SocketClient socketClient, byte[] bytes) {
        ByteBuffer packet = ByteBuffer.wrap(bytes);
        int type = packet.getInt();
        byte[] data = LowEntry.getBytesFromByteBuffer(packet);

        if (type == 0) {
            // INIT LOGIC

//            System.out.println("INIT-0");

            // TCP INIT (Send client their HashCode)
//            ByteBuffer send = ByteBuffer.allocate(8);
////            socketClient.setClientId(socketClient.hashCode());
//            send.putInt(0);
////            send.putInt(socketClient.getClientId());
//            socketClient.sendMessage(send.array());

        } else if (type == 1) {
            HiveNetConnection connection = this.server.getConnectionFromClient(socketClient);
            if (connection != null) {
                connection.setLastTcpMsg(System.currentTimeMillis());

//                System.out.println("[TCP-DAT]: " + LowEntry.bytesToHex(data,true));

//                byte[] data = new byte[0];
                if (connection.getMsgToken() != null) {
                    // Decipher with AES
                    data = LowEntry.decryptAes(data, connection.getMsgToken(), true);
                } else {
                    // Decipher with RSA
                    data = LowEntry.decryptRsa(data, DedicatedServer.licenseManager.getPrivateKey());
                }

//                System.out.println("[TCP-DAT-R]: " + LowEntry.bytesToHex(data,true));

                String msg = LowEntry.bytesToStringUtf8(data);

                System.out.println(msg);

                DedicatedServer.get(CommandService.class).handleCommand(msg, CommandSource.NET_TCP, connection);
            } else {
                System.err.println("Unable to Find Client...");
            }
        }
    }

    @Override
    public boolean startReceivingFunctionCall(SocketServer socketServer, SocketClient socketClient, int i) {
        System.out.println("[" + Thread.currentThread().getName() + "] Start Receiving Function Call");
        return (i <= (10 * 1024)); // this will only allow packets of 10KB and less
    }

    @Override
    public byte[] receivedFunctionCall(SocketServer socketServer, SocketClient socketClient, byte[] bytes) {
        String funcName = LowEntry.bytesToStringUtf8(bytes);
        if (funcName.equalsIgnoreCase("join")) {

            int slots = DedicatedServer.instance.getConfigFile().getConfig().get("max-players").getAsInt();
            if (slots <= DedicatedServer.get(PlayerService.class).players.size()) {
                return LowEntry.stringToBytesUtf8("Server is full");
            } else if (DedicatedServer.isLocked) {
                return LowEntry.stringToBytesUtf8(DedicatedServer.lockMessage);
            }

            // TODO: Ban Check
            // TODO:

            return LowEntry.stringToBytesUtf8("y");
        }
        return null;
    }

    @Override
    public boolean startReceivingLatentFunctionCall(SocketServer socketServer, SocketClient socketClient, int i) {
        System.out.println("[" + Thread.currentThread().getName() + "] Start Receiving Latent Function Call");
        return (i <= (10 * 1024)); // this will only allow packets of 10KB and less
    }

    @Override
    public void receivedLatentFunctionCall(SocketServer socketServer, SocketClient socketClient, byte[] bytes, LatentResponse latentResponse) {
        System.out.println("[" + Thread.currentThread().getName() + "] Received Latent Function Call: \"" + LowEntry.bytesToStringUtf8(bytes) + "\"");

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
