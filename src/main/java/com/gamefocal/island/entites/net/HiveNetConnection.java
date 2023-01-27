package com.gamefocal.island.entites.net;

import com.badlogic.gdx.math.collision.Sphere;
import com.gamefocal.island.DedicatedServer;
import com.gamefocal.island.entites.voip.VoipType;
import com.gamefocal.island.events.inv.InventoryUpdateEvent;
import com.gamefocal.island.game.exceptions.InventoryOwnedAlreadyException;
import com.gamefocal.island.game.inventory.Inventory;
import com.gamefocal.island.game.inventory.InventoryStack;
import com.gamefocal.island.game.inventory.equipment.EquipmentSlot;
import com.gamefocal.island.game.player.PlayerState;
import com.gamefocal.island.game.sounds.GameSounds;
import com.gamefocal.island.game.tasks.HiveTaskSequence;
import com.gamefocal.island.game.util.InventoryUtil;
import com.gamefocal.island.game.util.Location;
import com.gamefocal.island.models.GameEntityModel;
import com.gamefocal.island.models.PlayerModel;
import com.gamefocal.island.service.DataService;
import com.gamefocal.island.service.InventoryService;
import com.gamefocal.island.service.NetworkService;
import com.gamefocal.island.service.TaskService;
import com.google.gson.JsonArray;
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

    private Hashtable<UUID, String> loadedEntites = new Hashtable<>();

    private Hashtable<UUID, String> subStates = new Hashtable<>();

    private Hashtable<String,String> foliageSync = new Hashtable<>();

    private Sphere viewSphere = null;

    private PlayerState state = new PlayerState();

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

    public Hashtable<String, String> getFoliageSync() {
        return foliageSync;
    }

    public Hashtable<UUID, String> getSubStates() {
        return subStates;
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
//            this.udpQueue.add(msg.getBytes(StandardCharsets.UTF_8));
            DatagramPacket packet = this.getUdpOut();
            packet.setData(msg.getBytes(StandardCharsets.UTF_8));
            packet.setLength(msg.getBytes(StandardCharsets.UTF_8).length);

            try {
                DedicatedServer.get(NetworkService.class).getUdpSocket().send(packet);
//                System.out.println("[UDP]: " + msg);
                Thread.sleep(5);
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void sendTcp(String msg) {
//        this.tcpQueue.add(msg.getBytes(StandardCharsets.UTF_8));
        if (this.getSocket() != null) {

            msg = msg + "\n";

            try {
                this.getSocket().getOutputStream().write(msg.getBytes(StandardCharsets.UTF_8));
//                System.out.println("[TCP]: " + msg);
                Thread.sleep(5);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void sendChatMessage(String msg) {
        ChatFormatter formatter = new ChatFormatter();
        this.sendTcp("chat|" + formatter.formatChatString(msg));
    }

    public void updatePlayerDistance(UUID otherPlayer, float dist) {
        this.playerDistances.put(otherPlayer, dist);
    }

    public DatagramPacket getSoundOut() {
        return soundOut;
    }

    public void setSoundOut(DatagramPacket soundOut) {
        this.soundOut = soundOut;
    }

    public Hashtable<UUID, Float> getPlayerDistances() {
        return playerDistances;
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
        this.updateInventory(inventory, true);
    }

    public void updateInventory(Inventory inventory, boolean syncGui) {

        InventoryUpdateEvent event = new InventoryUpdateEvent(inventory).call();

        if (event.isCanceled()) {
            return;
        }

        this.sendUpdatePacket(inventory, syncGui);
    }

    public void sendUpdatePacket(Inventory inventory) {
        this.sendUpdatePacket(inventory, true);
    }

    public void sendUpdatePacket(Inventory inventory, boolean syncGui) {
        this.sendTcp("inv|update|" + this.getCompressedInv(inventory));
//        if (syncGui) {
//            this.updateInventoryGUI(inventory);
//        }
    }

    public void updateInventoryGUI(Inventory inventory) {
//        this.sendTcp("inv|gui|" + inventory.getUuid().toString());
    }

    public Inventory getOpenInventory() {
        return this.openedInventory;
    }

    public void syncEquipmentSlots() {
        JsonArray a = new JsonArray();
        int slotIndex = 0;
        for (EquipmentSlot s : EquipmentSlot.values()) {
            InventoryStack stack = this.player.equipmentSlots.getItemBySlot(s);
            if (stack == null) {
                a.add(new JsonObject());
            } else {
                a.add(InventoryUtil.itemToJson(stack, slotIndex));
            }
            slotIndex++;
        }

        JsonObject o = new JsonObject();
        o.add("equipment", a);

        this.sendTcp("inv|eq|" + Base64.getEncoder().encodeToString(o.toString().getBytes(StandardCharsets.UTF_8)));
    }

    public void trackEntity(GameEntityModel model) {
        this.loadedEntites.put(model.uuid, model.entityHash());
        if (!model.playersSubscribed.contains(this.uuid)) {
            model.playersSubscribed.add(this.uuid);
        }
    }

    public void untrackEntity(GameEntityModel model) {
        model.playersSubscribed.remove(this.uuid);
        this.loadedEntites.remove(model.uuid);
    }

    public Hashtable<UUID, String> getLoadedEntites() {
        return loadedEntites;
    }

    public void syncHotbar() {
        JsonArray a = new JsonArray();
        for (UUID uuid : this.getPlayer().hotbar.items) {
            InventoryStack stack = this.getPlayer().findStackFromUUID(uuid);
            int i = 0;
            if (stack != null) {

                JsonObject item = InventoryUtil.itemToJson(stack, i);
                a.add(item);
            } else {
                a.add(new JsonObject());
            }
        }

        JsonObject o = new JsonObject();
        o.add("bar", a);

        this.sendTcp("inv|hotbar|" + Base64.getEncoder().encodeToString(o.toString().getBytes(StandardCharsets.UTF_8)));
    }

    public void equipFromInventory(int invSlot) {

        HiveTaskSequence sequence = new HiveTaskSequence(false);

        InventoryStack stack = this.player.inventory.get(invSlot);
        if (stack != null) {
            EquipmentSlot equipToSlot = stack.getItem().getEquipTo();

            if (equipToSlot != null) {
                InventoryStack currentEq = this.player.equipmentSlots.getItemBySlot(equipToSlot);
                if (currentEq != null) {
                    // Is already equiped
                    if (!this.player.inventory.canAdd(currentEq)) {
                        return;
                    }

                    sequence.exec(() -> {
                        this.unequipTool(equipToSlot);
                    }).await(10L);
                }
                sequence.exec(() -> {
                    this.player.equipmentSlots.setBySlot(equipToSlot, stack);
                    this.player.inventory.clear(invSlot);
                    this.updateInventory(this.player.inventory);
                }).await(5L).exec(this::syncEquipmentSlots).exec(() -> {
                    this.getState().inHand = stack.getItem();
                });

                TaskService.scheduleTaskSequence(sequence);
            }
        }
    }

    public void unequipTool(EquipmentSlot slot) {
        HiveTaskSequence sequence = new HiveTaskSequence(false);

        InventoryStack stack = this.player.equipmentSlots.getItemBySlot(slot);
        if (stack != null) {
            this.player.inventory.add(stack);
            this.player.equipmentSlots.setBySlot(slot, null);

            sequence.exec(() -> {
                this.updateInventory(this.player.inventory);
            }).await(5L).exec(this::syncEquipmentSlots).exec(() -> {
                this.getState().inHand = null;
            });

            TaskService.scheduleTaskSequence(sequence);
        } else {
            System.out.println("STACK NULL");
        }
    }

    public void showFloatingTxt(String msg, Location atLocation) {

        HiveNetMessage h = new HiveNetMessage();
        h.cmd = "floattxt";
        h.args = new String[]{msg, atLocation.toString()};

        this.sendTcp(h.toString());
    }

    public void displayItemAdded(InventoryStack stack) {
        this.sendTcp("inv|a|" + Base64.getEncoder().encodeToString(InventoryUtil.itemToJson(stack, 0).toString().getBytes(StandardCharsets.UTF_8)));
    }

    public void displayItemRemoved(InventoryStack stack) {
        this.sendTcp("inv|r|" + Base64.getEncoder().encodeToString(InventoryUtil.itemToJson(stack, 0).toString().getBytes(StandardCharsets.UTF_8)));
    }

    public void playLocalSoundAtLocation(GameSounds sound, Location at, float volume, float pitch) {

        System.out.println("Playing Sound!");

        this.sendUdp("sfx|" + sound.name() + "|" + at.toString() + "|" + volume + "|" + pitch);
    }

    public void playSoundAtPlayer(GameSounds sound, float volume, float pitch) {
        this.playLocalSoundAtLocation(sound, this.player.location, volume, pitch);
    }

    @Override
    public boolean equals(Object obj) {
        if (HiveNetConnection.class.isAssignableFrom(obj.getClass())) {
            return ((HiveNetConnection) obj).uuid == this.uuid;
        }

        return false;
    }

    public PlayerState getState() {
        return state;
    }

    public Sphere getViewSphere() {
        return viewSphere;
    }

    public void tick() {

    }
}
