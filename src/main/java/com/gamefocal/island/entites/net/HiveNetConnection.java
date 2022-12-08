package com.gamefocal.island.entites.net;

import com.gamefocal.island.DedicatedServer;
import com.gamefocal.island.entites.voip.VoipType;
import com.gamefocal.island.events.inv.InventoryUpdateEvent;
import com.gamefocal.island.game.exceptions.InventoryOwnedAlreadyException;
import com.gamefocal.island.game.inventory.Inventory;
import com.gamefocal.island.game.util.InventoryUtil;
import com.gamefocal.island.models.PlayerModel;
import com.gamefocal.island.service.DataService;
import com.gamefocal.island.service.InventoryService;
import com.gamefocal.island.service.NetworkService;
import com.google.gson.JsonObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.Base64;
import java.util.Hashtable;
import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedQueue;

public class HiveNetConnection {

    private UUID uuid;

    private Socket socket;

    private BufferedReader bufferedReader;

    private String line;

    private PlayerModel player;

    private DatagramPacket udpOut;

    private DatagramPacket soundOut;

    private DatagramSocket localSocket;

    private Inventory openedInventory = null;

    private int voiceId = 0;

    private VoipType voipDistance = VoipType.PROXIMITY_NORMAL;

    private Hashtable<UUID, Float> playerDistances = new Hashtable<>();

    private ConcurrentLinkedQueue<byte[]> udpQueue = new ConcurrentLinkedQueue<>();

    public HiveNetConnection(Socket socket) throws IOException {
        this.socket = socket;
        this.bufferedReader = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
    }

    public boolean hasData() throws IOException {
        return this.socket.getInputStream().available() > 0;
    }

    public String readLine() throws IOException {
        while ((this.line = this.bufferedReader.readLine()) != null) {
            return this.line;
        }

        return null;
    }

    public Socket getSocket() {
        return socket;
    }

    public BufferedReader getBufferedReader() {
        return bufferedReader;
    }

    public String getLine() {
        return line;
    }

    public PlayerModel getPlayer() {
        return player;
    }

    public void setPlayer(PlayerModel player) {
        this.player = player;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public DatagramPacket getUdpOut() {
        return udpOut;
    }

    public void setUdpOut(DatagramPacket udpOut) {
        this.udpOut = udpOut;
    }

    public DatagramSocket getLocalSocket() {
        return localSocket;
    }

    public void setLocalSocket(DatagramSocket localSocket) {
        this.localSocket = localSocket;
    }

    public int getVoiceId() {
        return voiceId;
    }

    public void setVoiceId(int voiceId) {
        this.voiceId = voiceId;
    }

    public void sendSoundData(String msg) {
        if (this.soundOut != null) {
            DatagramPacket packet = this.soundOut;
            packet.setData(msg.getBytes(StandardCharsets.UTF_8));
            packet.setLength(msg.getBytes(StandardCharsets.UTF_8).length);

            try {
                DedicatedServer.get(NetworkService.class).getRtpSocket().send(packet);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void sendUdp(String msg) {
        if (this.getUdpOut() != null) {
            this.udpQueue.add(msg.getBytes(StandardCharsets.UTF_8));
//            DatagramPacket packet = this.getUdpOut();
//            packet.setData(msg.getBytes(StandardCharsets.UTF_8));
//            packet.setLength(msg.getBytes(StandardCharsets.UTF_8).length);
//
//            try {
//                DedicatedServer.get(NetworkService.class).getUdpSocket().send(packet);
//                Thread.sleep(1);
//            } catch (IOException | InterruptedException e) {
//                e.printStackTrace();
//            }
        }
    }

    public void sendTcp(String msg) {
        if (this.getSocket() != null) {
            try {
                this.getSocket().getOutputStream().write(msg.getBytes(StandardCharsets.UTF_8));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void updatePlayerDistance(UUID otherPlayer, float dist) {
        this.playerDistances.put(otherPlayer, dist);
    }

    public DatagramPacket getSoundOut() {
        return soundOut;
    }

    public Hashtable<UUID, Float> getPlayerDistances() {
        return playerDistances;
    }

    public void setSoundOut(DatagramPacket soundOut) {
        this.soundOut = soundOut;
    }

    public VoipType getVoipDistance() {
        return voipDistance;
    }

    public void setVoipDistance(VoipType voipDistance) {
        this.voipDistance = voipDistance;
    }

    public void openInventory(Inventory inventory) throws InventoryOwnedAlreadyException {
        this.openInventory(inventory, false);
    }

    public String getCompressedInv(Inventory inventory) {
        JsonObject inv = InventoryUtil.inventoryToJson(inventory);
        return Base64.getEncoder().encodeToString(inv.toString().getBytes(StandardCharsets.UTF_8));
    }

    public void openInventory(Inventory inventory, boolean force) throws InventoryOwnedAlreadyException {
        inventory.takeOwnership(this, force);
        this.openedInventory = inventory;
        this.sendTcp("inv|open|" + this.getCompressedInv(inventory));

        DedicatedServer.get(InventoryService.class).trackInventory(this.openedInventory);

//        this.updateInventory();
    }

    public void openDualInventory(Inventory inventory, boolean force) throws InventoryOwnedAlreadyException {
        inventory.takeOwnership(this, force);
        this.getPlayer().inventory.takeOwnership(this, force);

        this.openedInventory = inventory;

        this.sendTcp("inv|open|" + this.getCompressedInv(inventory) + "|" + this.getCompressedInv(this.getPlayer().inventory));

        DedicatedServer.get(InventoryService.class).trackInventory(this.getPlayer().inventory);
        DedicatedServer.get(InventoryService.class).trackInventory(inventory);
    }

    public void closeInventory(Inventory inventory) {
        inventory.releaseOwnership();
        DedicatedServer.get(InventoryService.class).untrackInventory(inventory);
        this.openedInventory = null;

        try {
            DataService.players.createOrUpdate(this.player);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        this.sendTcp("inv|close|" + inventory.getUuid().toString());
    }

    public void updateInventory() {
        if (this.openedInventory != null) {
            this.updateInventory(this.openedInventory);
        }
    }

    public void updateInventory(Inventory inventory) {

        InventoryUpdateEvent event = new InventoryUpdateEvent(inventory).call();

        if (event.isCanceled()) {
            return;
        }

        this.sendUpdatePacket(inventory);
    }

    public void sendUpdatePacket(Inventory inventory) {
        JsonObject inv = InventoryUtil.inventoryToJson(inventory);
        this.sendTcp("inv|update|" + this.getCompressedInv(inventory));
    }

    public Inventory getOpenInventory() {
        return this.openedInventory;
    }

    public void processUdpQueue() {
        int size = this.udpQueue.size();

        if (size > 0) {
            for (int i = 0; i < size; i++) {
                byte[] b = this.udpQueue.peek();
                if (b != null && this.getUdpOut() != null) {
                    b = this.udpQueue.poll();
                    DatagramPacket packet = this.getUdpOut();
                    packet.setData(b);
                    packet.setLength(b.length);

                    try {
                        DedicatedServer.get(NetworkService.class).getUdpSocket().send(packet);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
