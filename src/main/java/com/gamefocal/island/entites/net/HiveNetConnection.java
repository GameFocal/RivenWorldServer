package com.gamefocal.island.entites.net;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.math.collision.Sphere;
import com.gamefocal.island.DedicatedServer;
import com.gamefocal.island.entites.voip.VoipType;
import com.gamefocal.island.events.inv.InventoryCloseEvent;
import com.gamefocal.island.events.inv.InventoryOpenEvent;
import com.gamefocal.island.events.inv.InventoryUpdateEvent;
import com.gamefocal.island.game.GameEntity;
import com.gamefocal.island.game.enviroment.player.PlayerDataState;
import com.gamefocal.island.game.exceptions.InventoryOwnedAlreadyException;
import com.gamefocal.island.game.inventory.Inventory;
import com.gamefocal.island.game.inventory.InventoryStack;
import com.gamefocal.island.game.inventory.equipment.EquipmentSlot;
import com.gamefocal.island.game.player.Animation;
import com.gamefocal.island.game.player.PlayerState;
import com.gamefocal.island.game.ray.HitResult;
import com.gamefocal.island.game.ray.hit.EntityHitResult;
import com.gamefocal.island.game.ray.hit.FoliageHitResult;
import com.gamefocal.island.game.ray.hit.PlayerHitResult;
import com.gamefocal.island.game.ray.hit.TerrainHitResult;
import com.gamefocal.island.game.sounds.GameSounds;
import com.gamefocal.island.game.tasks.HiveTask;
import com.gamefocal.island.game.tasks.HiveTaskSequence;
import com.gamefocal.island.game.ui.GameUI;
import com.gamefocal.island.game.ui.radialmenu.DynamicRadialMenuUI;
import com.gamefocal.island.game.ui.radialmenu.RadialMenuHandler;
import com.gamefocal.island.game.ui.radialmenu.RadialMenuOption;
import com.gamefocal.island.game.util.InventoryUtil;
import com.gamefocal.island.game.util.Location;
import com.gamefocal.island.game.util.ShapeUtil;
import com.gamefocal.island.models.GameEntityModel;
import com.gamefocal.island.models.PlayerModel;
import com.gamefocal.island.service.*;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import lowentry.ue4.classes.sockets.SocketClient;
import lowentry.ue4.library.LowEntry;
import org.apache.commons.codec.digest.DigestUtils;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class HiveNetConnection {

    private SocketClient socketClient;

    private UUID uuid;

    private PlayerModel player;

    private DatagramPacket udpOut;

    private DatagramPacket soundOut;

    private DatagramSocket localSocket;

    private Inventory openedInventory = null;

    private int voiceId = 0;

    private boolean isVisible = true;

    private VoipType voipDistance = VoipType.PROXIMITY_NORMAL;

    private Hashtable<UUID, Float> playerDistances = new Hashtable<>();

    private Hashtable<UUID, String> loadedEntites = new Hashtable<>();

    private Hashtable<UUID, String> subStates = new Hashtable<>();

    private Hashtable<String, String> foliageSync = new Hashtable<>();

    private Sphere viewSphere = null;

    private PlayerState state = new PlayerState();

    private ConcurrentHashMap<PlayerDataState, HiveTask> effectTimers = new ConcurrentHashMap<>();

    private Location buildPreviewLocation = null;

    private float temprature = 85f;

    private ConcurrentHashMap<String, Object> meta = new ConcurrentHashMap<>();

    private HitResult lookingAt = null;

    private String cursurTip = null;

    private String helpbox = null;

    private String syncHash = "none";

    private Long syncVersion = 0L;

    private GameUI openUI = null;

    private Vector3 forwardVector = new Vector3();

    private DynamicRadialMenuUI radialMenu = new DynamicRadialMenuUI();

    public HiveNetConnection(SocketClient socket) throws IOException {
        this.socketClient = socket;
//        this.socket = socket;
//        this.bufferedReader = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
    }

    public <T> T getMeta(String key, Class<T> t) {
        return (T) this.meta.get(key);
    }

    public boolean hasMeta(String key) {
        return this.meta.containsKey(key);
    }

    public void setMeta(String key, Object data) {
        this.meta.put(key, data);
    }

    public void clearMeta(String key) {
        this.meta.remove(key);
    }

    public void clearMeta() {
        this.meta.clear();
    }

    public Location getBuildPreviewLocation() {
        return buildPreviewLocation;
    }

    public void setBuildPreviewLocation(Location buildPreviewLocation) {
        this.buildPreviewLocation = buildPreviewLocation;
    }

    public String getSyncHash() {
        return syncHash;
    }

    public void setSyncHash(String syncHash) {
        this.syncHash = syncHash;
    }

    public SocketClient getSocketClient() {
        return socketClient;
    }

    public Hashtable<String, String> getFoliageSync() {
        return foliageSync;
    }

    public Hashtable<UUID, String> getSubStates() {
        return subStates;
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

    public float getTemprature() {
        return temprature;
    }

    public void setTemprature(float temprature) {
        this.temprature = temprature;
    }

    public void addEffect(PlayerDataState playerDataState) {
        this.player.playerStats.states.add(playerDataState);
        if (playerDataState.getTicks() > 0) {
            HiveTask t = TaskService.scheduledDelayTask(() -> {
                this.player.playerStats.states.remove(playerDataState);
            }, (long) playerDataState.getTicks(), false);
            this.effectTimers.put(playerDataState, t);
        }
    }

    public void removeEffect(PlayerDataState state) {
        this.player.playerStats.states.remove(state);
        if (this.effectTimers.containsKey(state)) {
            this.effectTimers.get(state).cancel();
            this.effectTimers.remove(state);
        }
    }

    public void clearAllStates() {
        for (HiveTask t : this.effectTimers.values()) {
            t.cancel();
        }
        this.player.playerStats.states.clear();
    }

    public void sendSoundData(String msg) {
        if (this.soundOut != null) {
            DatagramPacket packet = this.soundOut;
            packet.setData(msg.getBytes(StandardCharsets.UTF_8));
            packet.setLength(msg.getBytes(StandardCharsets.UTF_8).length);

            // TODO: Send Sound Data Here

//            try {
//                DedicatedServer.get(NetworkService.class).getRtpSocket().send(packet);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
        }
    }

    public void sendUdp(String msg) {
        // TODO: SEND

        this.socketClient.sendUnreliableMessage(LowEntry.stringToBytesUtf8(msg));

//        if (this.getUdpOut() != null) {
//
////            DedicatedServer.get(NetworkService.class).udpOutbound.add(Pair.of(this,msg.getBytes(StandardCharsets.UTF_8)));
//
////            this.udpQueue.add(msg.getBytes(StandardCharsets.UTF_8));
//            DatagramPacket packet = this.getUdpOut();
//            packet.setData(msg.getBytes(StandardCharsets.UTF_8));
//            packet.setLength(msg.getBytes(StandardCharsets.UTF_8).length);
//
//            try {
//                DedicatedServer.get(NetworkService.class).getUdpSocket().send(packet);
//                System.out.println("[UDP]: " + msg);
//                Thread.sleep(1);
//            } catch (IOException | InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
    }

    public void sendTcp(String msg) {
        // TODO: SEND

        this.socketClient.sendMessage(LowEntry.stringToBytesUtf8(msg));

//        this.tcpQueue.add(msg.getBytes(StandardCharsets.UTF_8));
//        if (this.getSocket() != null) {
//
////            DedicatedServer.get(NetworkService.class).tcpOutbound.add(Pair.of(this,msg.getBytes(StandardCharsets.UTF_8)));
//
////            msg = msg + "\n";
////
//            try {
//                this.getSocket().getOutputStream().write(msg.getBytes(StandardCharsets.UTF_8));
//                System.out.println("[TCP]: " + msg);
//                Thread.sleep(1);
//            } catch (IOException | InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
    }

    public void sendChatMessage(String msg) {
//        ChatFormatter formatter = new ChatFormatter();
        this.sendTcp("chat|" + msg);
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

        InventoryOpenEvent event = new InventoryOpenEvent(inventory, this).call();

        if (event.isCanceled()) {
            return;
        }

        inventory.takeOwnership(this, force);
        this.openedInventory = inventory;
//        this.sendTcp("inv|open|" + this.getCompressedInv(inventory));

        DedicatedServer.get(InventoryService.class).trackInventory(this.openedInventory);

//        this.updateInventory();
    }

    public void openDualInventory(Inventory inventory, boolean force) throws InventoryOwnedAlreadyException {

        InventoryOpenEvent event = new InventoryOpenEvent(inventory, this).call();

        if (event.isCanceled()) {
            return;
        }

        inventory.takeOwnership(this, force);
        this.getPlayer().inventory.takeOwnership(this, force);

        this.openedInventory = inventory;

//        this.sendTcp("inv|open|" + this.getCompressedInv(inventory) + "|" + this.getCompressedInv(this.getPlayer().inventory));

        DedicatedServer.get(InventoryService.class).trackInventory(this.getPlayer().inventory);
        DedicatedServer.get(InventoryService.class).trackInventory(inventory);
    }

    public void closeInventory(Inventory inventory) {

        InventoryCloseEvent event = new InventoryCloseEvent(inventory, this).call();

        if (event.isCanceled()) {
            return;
        }

        inventory.releaseOwnership();
        DedicatedServer.get(InventoryService.class).untrackInventory(inventory);
        this.openedInventory = null;

        DataService.exec(() -> {
            try {
                DataService.players.createOrUpdate(this.player);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        });

//        this.sendTcp("inv|close|" + inventory.getUuid().toString());
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

        if (inventory.getLinkedUI() != null) {
            inventory.getLinkedUI().update(this);
        }

//        this.sendTcp("inv|update|" + this.getCompressedInv(inventory));
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

    public boolean isTrackingEntity(GameEntityModel model) {
        if (this.loadedEntites.containsKey(model.uuid)) {
            return model.playersSubscribed.contains(this.uuid);
        }

        return false;
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

    public void showClaimRegion(Location location, float radius, Color color) {
        this.sendTcp("claimr|" + location.toString() + "|" + radius + "|" + color.toString());
    }

    public void sendCanBuildHere(boolean canBuild) {
        this.sendUdp("ncb|" + (canBuild ? "t" : "f"));
    }

    public void hideClaimRegions() {
        this.sendTcp("claimrh|1");
    }

    @Override
    public boolean equals(Object obj) {
        if (HiveNetConnection.class.isAssignableFrom(obj.getClass())) {
            return ((HiveNetConnection) obj).uuid == this.uuid;
        }

        return false;
    }

    public PlayerState getState() {

        // Set player and inHand
        if (this.state.player == null) {
            this.state.player = this;
            InventoryStack inHand = this.player.equipmentSlots.getItemBySlot(EquipmentSlot.WEAPON);
            if (inHand != null) {
                this.state.inHand = inHand.getItem();
            }
        }

        return state;
    }

    public void processHitData(String d) {

        String[] p = d.split("\\$");

        String type = p[0];

        this.lookingAt = null;

        if (!type.equalsIgnoreCase("none")) {

            String uuidString = p[1];
            Location hitLocation = Location.fromString(p[2]);
            Location location = Location.fromString(p[3]);
            int index = Integer.parseInt(p[4]);
            String name = "none";
            if (p.length > 5) {
                name = p[5];
            }

            if (type.equalsIgnoreCase("Net Entity") || type.equalsIgnoreCase("Net Entity Object")) {

                UUID uuid = UUID.fromString(uuidString);

                if (DedicatedServer.instance.getWorld().getEntityFromId(uuid) != null) {
                    GameEntity e = DedicatedServer.instance.getWorld().getEntityFromId(uuid).entityData;

                    if (e != null) {
                        if (e.location.dist(this.getPlayer().location) <= 300) {
                            // A player exist
                            this.lookingAt = new EntityHitResult(e);
                        }
                    }
                }

            } else if (type.equalsIgnoreCase("Foliage")) {

                this.lookingAt = new FoliageHitResult(
                        hitLocation,
                        index,
                        location,
                        name
                );

            } else if (type.equalsIgnoreCase("Landscape")) {

                this.lookingAt = new TerrainHitResult(
                        hitLocation,
                        name
                );

            } else if (type.equalsIgnoreCase("Net Player")) {

                UUID uuid = UUID.fromString(uuidString);

                if (DedicatedServer.get(PlayerService.class).players.containsKey(uuid)) {

                    // Is a valid player
                    HiveNetConnection other = DedicatedServer.get(PlayerService.class).players.get(uuid);
                    if (other.getPlayer().location.dist(this.getPlayer().location) <= 300) {
                        this.lookingAt = new PlayerHitResult(uuid,
                                hitLocation);
                    }

                }

            }
        }
    }

    public void showCursorToolTipText(String text) {

        if (text == null) {
            this.hideCursorToolTipText();
            return;
        }

        this.cursurTip = text;
    }

    public void hideCursorToolTipText() {
        this.cursurTip = null;
    }

    public HitResult getLookingAt() {
        return lookingAt;
    }

    public void setHelpboxText(String msg) {
        this.helpbox = msg;
    }

    public Sphere getViewSphere() {
        return viewSphere;
    }

    public void sendSyncPackage() {
        HiveNetMessage message = new HiveNetMessage();
        message.cmd = "sync";

        message.args = new String[]{
                (this.cursurTip == null) ? "none" : this.cursurTip,
                (this.helpbox == null) ? "none" : this.helpbox,
                "none"
        };

        String hash = DigestUtils.md5Hex(message.toString());

        if (!this.syncHash.equalsIgnoreCase(hash)) {
            message.args[2] = hash;
            this.sendUdp(message.toString());
        }
    }

    public GameUI getOpenUI() {
        return openUI;
    }

    public void setOpenUI(GameUI openUI) {
        this.openUI = openUI;
    }

    public void openRadialMenu(RadialMenuHandler handler, RadialMenuOption... options) {
        this.radialMenu.setHandler(handler);
        this.radialMenu.getOptions().clear();
        this.radialMenu.getOptions().addAll(Arrays.asList(options));
        this.radialMenu.open(this, null);
    }

    public void closeRadialMenu() {
        this.radialMenu.close(this);
        this.radialMenu.getOptions().clear();
    }

    public DynamicRadialMenuUI getRadialMenu() {
        return radialMenu;
    }

    public void drawDebugLine(Location start, Location end, float thickness) {
        this.sendTcp("d-line|" + start.toString() + "|" + end.toString() + "|" + thickness);
    }

    public void drawDebugBox(Location center, Location size, float thickness) {
        this.sendTcp("d-box|" + center.toString() + "|" + size.toString() + "|" + thickness);
    }

    public void drawDebugBox(BoundingBox boundingBox, float thickness) {
        this.sendTcp("d-box|" + Location.fromVector(boundingBox.getCenter(new Vector3())).toString() + "|" + Location.fromVector(boundingBox.getDimensions(new Vector3())).toString() + "|" + thickness);
    }

//    public void drawDebugCapsual(Location center, Location size, float thickness) {
//        this.sendTcp("d-box|" + center.toString() + "|" + size.toString() + "|" + thickness);
//    }

    public void drawDebugSphere(Location center, float radius, float thickness) {
        this.sendTcp("d-sphere|" + center.toString() + "|" + radius + "|" + thickness);
    }

    public BoundingBox getBoundingBox() {
        return ShapeUtil.makeBoundBox(this.player.location.toVector(), 15f, 75f);
    }

    public Vector3 getForwardVector() {
        return forwardVector;
    }

    public void playAnimation(Animation animation) {
        this.sendTcp("pan|" + animation.getUnrealName());

        this.state.animation = animation.getUnrealName();
        this.state.animStart = System.currentTimeMillis();
        this.state.markDirty();
    }

    public void sendKillPacket() {
        this.sendTcp("pk|");
    }

    public void setForwardVector(Vector3 forwardVector) {
        this.forwardVector = forwardVector;
    }

    public void tick() {

    }

    public boolean isVisible() {
        return isVisible;
    }

    public void setVisible(boolean visible) {
        isVisible = visible;
    }

    public void clearLookingAt() {
        this.lookingAt = null;
    }

    public void tpToLocation(Location location) {
        this.getPlayer().location = location;
        this.sendSyncPackage();
        this.sendTcp("tpa|" + location.toString());
    }

    public void hide() {
        this.isVisible = false;
        this.broadcastState();
    }

    public void show() {
        this.isVisible = true;
        this.broadcastState();
    }

    public void broadcastState() {
        if (this.isVisible()) {
            DedicatedServer.get(NetworkService.class).broadcastUdp(this.getState().getNetPacket(), this.getUuid());
        } else {
            HiveNetMessage msg = new HiveNetMessage();
            msg.cmd = "nhp";
            msg.args = new String[]{
                    this.uuid.toString()
            };
            DedicatedServer.get(NetworkService.class).broadcastUdp(msg, this.uuid);
        }
    }

    public void sendAttributes() {
        // Build the message
        HiveNetMessage message = new HiveNetMessage();
        message.cmd = "attr";
        message.args = new String[5 + this.getPlayer().playerStats.states.size()];

        message.args[0] = String.valueOf(this.getPlayer().playerStats.hunger);
        message.args[1] = String.valueOf(this.getPlayer().playerStats.thirst);
        message.args[2] = String.valueOf(this.getPlayer().playerStats.health);
        message.args[3] = String.valueOf(this.getPlayer().playerStats.energy);
        message.args[4] = (this.getState().isDead ? "t" : "f");

        int i = 1;
        for (PlayerDataState s : this.getPlayer().playerStats.states) {
            message.args[3 + i++] = String.valueOf(s.getByte());
        }

        // Emit the change to the client
        this.sendUdp(message.toString());
    }
}
