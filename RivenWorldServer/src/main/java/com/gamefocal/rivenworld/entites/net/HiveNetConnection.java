package com.gamefocal.rivenworld.entites.net;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.math.collision.Sphere;
import com.gamefocal.rivenworld.DedicatedServer;
import com.gamefocal.rivenworld.entites.util.BufferUtil;
import com.gamefocal.rivenworld.entites.voip.VoipType;
import com.gamefocal.rivenworld.events.inv.InventoryCloseEvent;
import com.gamefocal.rivenworld.events.inv.InventoryOpenEvent;
import com.gamefocal.rivenworld.events.inv.InventoryUpdateEvent;
import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.WorldChunk;
import com.gamefocal.rivenworld.game.enviroment.player.PlayerDataState;
import com.gamefocal.rivenworld.game.exceptions.InventoryOwnedAlreadyException;
import com.gamefocal.rivenworld.game.inventory.Inventory;
import com.gamefocal.rivenworld.game.inventory.InventoryStack;
import com.gamefocal.rivenworld.game.inventory.enums.EquipmentSlot;
import com.gamefocal.rivenworld.game.player.Animation;
import com.gamefocal.rivenworld.game.player.PlayerState;
import com.gamefocal.rivenworld.game.ray.HitResult;
import com.gamefocal.rivenworld.game.ray.hit.*;
import com.gamefocal.rivenworld.game.sounds.GameSounds;
import com.gamefocal.rivenworld.game.tasks.HiveTask;
import com.gamefocal.rivenworld.game.ui.CraftingUI;
import com.gamefocal.rivenworld.game.ui.GameUI;
import com.gamefocal.rivenworld.game.ui.radialmenu.DynamicRadialMenuUI;
import com.gamefocal.rivenworld.game.ui.radialmenu.RadialMenuHandler;
import com.gamefocal.rivenworld.game.ui.radialmenu.RadialMenuOption;
import com.gamefocal.rivenworld.game.util.Location;
import com.gamefocal.rivenworld.game.util.MathUtil;
import com.gamefocal.rivenworld.game.util.ShapeUtil;
import com.gamefocal.rivenworld.game.water.WaterSource;
import com.gamefocal.rivenworld.game.weather.GameWeather;
import com.gamefocal.rivenworld.models.GameEntityModel;
import com.gamefocal.rivenworld.models.PlayerModel;
import com.gamefocal.rivenworld.service.*;
import com.google.gson.JsonObject;
import lowentry.ue4.classes.AesKey;
import lowentry.ue4.classes.ByteDataWriter;
import lowentry.ue4.classes.RsaPublicKey;
import lowentry.ue4.classes.bytedata.writer.ByteBufferDataWriter;
import lowentry.ue4.classes.sockets.SocketClient;
import lowentry.ue4.library.LowEntry;
import org.apache.commons.codec.digest.DigestUtils;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

public class HiveNetConnection {

    private String hiveId;

    private String hiveDisplayName;

    private RsaPublicKey publicKey;

    private AesKey msgToken;

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

    private Hashtable<String, String> foliageSync = new Hashtable<>();

    private ConcurrentHashMap<String, ConcurrentHashMap<UUID, String>> loadedChunks = new ConcurrentHashMap<>();

    private ConcurrentHashMap<String, LinkedList<String>> chunkLODUpdates = new ConcurrentHashMap<>();

    private ConcurrentHashMap<String, Long> chunkLODState = new ConcurrentHashMap<>();

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

    private boolean syncUpdates = true;

    private NetworkMode networkMode = NetworkMode.TCP_UDP;

    private float overrideDayPercent = -1f;

    private GameWeather overrideWeather = null;

    private JsonObject netAppearance = new JsonObject();

    private float renderDistance = (25 * 100) * 6;// 6 chunks around the player

    private boolean isFlying = false;

    private Location lookingAtTerrain = new Location(0, 0, 0);

    private float speed = 0;

    private float maxspeed = 0;

    private boolean isFalling = false;

    private boolean isInWater = false;

    private Location lastLocation = null;
    private Long lastLocationTime = 0L;
    private Location fallStartAt = null;
    private float fallSpeed = 0;
    private Long onlineSince = 0L;

    private boolean takeFallDamage = true;

    private boolean getAutoWorldSyncUpdates = false;

    private GameSounds bgSound = null;

    private Long lastVoipPacket = 0L;

    private boolean isLoaded = false;

    public HiveNetConnection(SocketClient socket) throws IOException {
        this.socketClient = socket;
//        this.socket = socket;
//        this.bufferedReader = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
    }

    public boolean isLoaded() {
        return isLoaded;
    }

    public void setLoaded(boolean loaded) {
        isLoaded = loaded;
    }

    public void enableWorldSync() {
        this.getAutoWorldSyncUpdates = true;
    }

    public void disableWorldSync() {
        this.getAutoWorldSyncUpdates = false;
    }

    public boolean isGetAutoWorldSyncUpdates() {
        return getAutoWorldSyncUpdates;
    }

    public boolean isTakeFallDamage() {
        return takeFallDamage;
    }

    public void setTakeFallDamage(boolean takeFallDamage) {
        this.takeFallDamage = takeFallDamage;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public boolean isFalling() {
        return isFalling;
    }

    public void setFalling(boolean falling) {
        isFalling = falling;
    }

    public boolean isInWater() {
        return isInWater;
    }

    public void setInWater(boolean inWater) {
        isInWater = inWater;
    }

    public Location getLookingAtTerrain() {
        return lookingAtTerrain;
    }

    public void setLookingAtTerrain(Location lookingAtTerrain) {
        this.lookingAtTerrain = lookingAtTerrain;
    }

    public boolean isFlying() {
        return isFlying;
    }

    public void setFlying(boolean flying) {
        isFlying = flying;
    }

    public ConcurrentHashMap<String, ConcurrentHashMap<UUID, String>> getLoadedChunks() {
        return loadedChunks;
    }

    public JsonObject getNetAppearance() {
        return netAppearance;
    }

    public void setNetAppearance(JsonObject netAppearance) {
        this.netAppearance = netAppearance;
    }

    public boolean isSyncUpdates() {
        return syncUpdates;
    }

    public void setSyncUpdates(boolean syncUpdates) {
        this.syncUpdates = syncUpdates;
    }

    public float getOverrideDayPercent() {
        return overrideDayPercent;
    }

    public void setOverrideDayPercent(float overrideDayPercent) {
        this.overrideDayPercent = overrideDayPercent;
    }

    public GameWeather getOverrideWeather() {
        return overrideWeather;
    }

    public void setOverrideWeather(GameWeather overrideWeather) {
        this.overrideWeather = overrideWeather;
    }

    public String getHiveDisplayName() {
        return hiveDisplayName;
    }

    public void setHiveDisplayName(String hiveDisplayName) {
        this.hiveDisplayName = hiveDisplayName;
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

    public String getHiveId() {
        return hiveId;
    }

    public void setHiveId(String hiveId) {
        this.hiveId = hiveId;
    }

    public RsaPublicKey getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(RsaPublicKey publicKey) {
        this.publicKey = publicKey;
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
        if (this.networkMode == NetworkMode.TCP_ONLY) {
            System.out.println("Reroute Data to TCP ONLY");
            this.sendTcp(msg);
        } else {
            byte[] data = LowEntry.stringToBytesUtf8(msg);
            if (this.msgToken != null) {
                // Send via AES
                byte[] eData = LowEntry.encryptAes(data, this.msgToken, true);
                this.socketClient.sendUnreliableMessage(this.makeRawPacket(1, eData));
            } else {
//                System.err.println("Invalid Msg Token UDP");
            }
        }
    }

    public byte[] makeRawPacket(int type, byte[] data) {
        ByteBuffer buffer = ByteBuffer.allocate(data.length + 4);
        buffer.putInt(type);
        BufferUtil.push(buffer, data);

        return buffer.array();
    }

    public void sendTcp(String msg) {
        byte[] data = LowEntry.stringToBytesUtf8(msg);
        if (this.msgToken != null) {
            // Send via AES
            byte[] eData = LowEntry.encryptAes(data, this.msgToken, true);
            this.socketClient.sendMessage(this.makeRawPacket(1, eData));
        } else {
//            System.err.println("Invalid MSG Token TCP");
        }
    }

    public AesKey getMsgToken() {
        return msgToken;
    }

    public void setMsgToken(AesKey msgToken) {
        this.msgToken = msgToken;
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
//        JsonObject inv = InventoryUtil.inventoryToJson(inventory);
        return inventory.toJson().toString();
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

    public void updatePlayerInventory() {
        this.sendTcp("plinv|" + this.getPlayer().inventory.toJson().toString());
    }

    public void updateInventory(Inventory inventory) {
        this.updateInventory(inventory, false);
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
//
//        if (inventory.getLinkedUI() != null) {
//            inventory.getLinkedUI().update(this);
//        }
//
//        this.sendTcp("inv|update|" + this.getCompressedInv(inventory));
//        if (syncGui) {
//            if (inventory.getLinkedUI() != null) {
//                inventory.getLinkedUI().update(this);
//            }
//        }
    }

    public void updateInventoryGUI(Inventory inventory) {
//        this.sendTcp("inv|gui|" + inventory.getUuid().toString());
    }

    public Inventory getOpenInventory() {
        return this.openedInventory;
    }

    public void syncEquipmentSlots() {
        if (this.getPlayer().equipmentSlots.inHand != null) {
            if (this.getPlayer().equipmentSlots.inHand.getAmount() <= 0) {
                this.getPlayer().equipmentSlots.inHand = null;
            }
        }

        this.sendTcp("equp|" + this.getPlayer().equipmentSlots.toJson().toString());

//        JsonArray a = new JsonArray();
//        int slotIndex = 0;
//        for (EquipmentSlot s : EquipmentSlot.values()) {
//            InventoryStack stack = this.player.equipmentSlots.getItemBySlot(s);
//            if (stack == null) {
//                a.add(new JsonObject());
//            } else {
//                a.add(InventoryUtil.itemToJson(stack, slotIndex));
//            }
//            slotIndex++;
//        }
//
//        JsonObject o = new JsonObject();
//        o.addProperty("linkedinv", this.getPlayer().inventory.getUuid().toString());
//        o.add("equipment", a);
//
//        this.sendTcp("inv|eq|" + o.toString());
    }

    public void syncHotbar() {
//        JsonArray a = new JsonArray();
//        for (UUID uuid : this.getPlayer().hotbar.items) {
//            InventoryStack stack = this.getPlayer().findStackFromUUID(uuid);
//            int i = 0;
//            if (stack != null) {
//
//                JsonObject item = InventoryUtil.itemToJson(stack, i);
//                a.add(item);
//            } else {
//                a.add(new JsonObject());
//            }
//        }
//
//        JsonObject o = new JsonObject();
//        o.add("bar", a);
//
//        this.sendTcp("inv|hotbar|" + o.toString());
    }

    public void equipFromInventory(int invSlot) {


//        HiveTaskSequence sequence = new HiveTaskSequence(false);
//
//        InventoryStack stack = this.player.inventory.get(invSlot);
//        if (stack != null) {
//            EquipmentSlot equipToSlot = stack.getItem().getEquipTo();
//
//            if (equipToSlot != null) {
//                InventoryStack currentEq = this.player.equipmentSlots.getItemBySlot(equipToSlot);
//                if (currentEq != null) {
//                    // Is already equiped
//                    if (!this.player.inventory.canAdd(currentEq)) {
//                        return;
//                    }
//
//                    sequence.exec(() -> {
//                        this.unequipTool(equipToSlot);
//                    }).await(10L);
//                }
//                sequence.exec(() -> {
//                    this.player.equipmentSlots.setBySlot(equipToSlot, stack);
//                    this.player.inventory.clear(invSlot);
//                    this.updateInventory(this.player.inventory);
//                }).await(5L).exec(this::syncEquipmentSlots).exec(() -> {
//                    this.getState().inHand = stack.getItem();
//                });
//
//                TaskService.scheduleTaskSequence(sequence);
//            }
//        }
    }

//    public void unequipTool(EquipmentSlot slot) {
//        HiveTaskSequence sequence = new HiveTaskSequence(false);
//
//        InventoryStack stack = this.player.equipmentSlots.getItemBySlot(slot);
//        if (stack != null) {
//            this.player.inventory.add(stack);
//            this.player.equipmentSlots.setBySlot(slot, null);
//
//            sequence.exec(() -> {
//                this.updateInventory(this.player.inventory);
//            }).await(5L).exec(this::syncEquipmentSlots).exec(() -> {
//                this.getState().inHand = null;
//            });
//
//            TaskService.scheduleTaskSequence(sequence);
//        } else {
//            System.out.println("STACK NULL");
//        }
//    }

    public void showFloatingTxt(String msg, Location atLocation) {

        HiveNetMessage h = new HiveNetMessage();
        h.cmd = "floattxt";
        h.args = new String[]{msg, atLocation.toString()};

        this.sendTcp(h.toString());
    }

    public void displayItemAdded(InventoryStack stack) {
        this.sendTcp("ia|" + stack.toJson().toString());
    }

    public void displayItemRemoved(InventoryStack stack) {
        this.sendTcp("ir|" + stack.toJson().toString());
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
//            InventoryStack inHand = this.player.equipmentSlots.getItemBySlot(EquipmentSlot.WEAPON);
//            if (inHand != null) {
//                this.state.inHand = inHand.getItem();
//            }
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

            } else if (type.equalsIgnoreCase("Water")) {
                WaterSource source = null;
                if (name.equalsIgnoreCase("FRESH")) {
                    source = WaterSource.FRESH_WATER;
                } else if (name.equalsIgnoreCase("SALT")) {
                    source = WaterSource.SALT_WATER;
                }

                this.lookingAt = new WaterHitResult(hitLocation, source);
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

    public void setForwardVector(Vector3 forwardVector) {
        this.forwardVector = forwardVector;
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
        this.takeFallDamage = false;
        this.getPlayer().location = location;
        this.sendSyncPackage();
        this.sendTcp("tpa|" + location.toString());
        TaskService.scheduledDelayTask(() -> {
            takeFallDamage = true;
        }, 20L, false);
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
        if (this.uuid != null) {
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
    }

    public void sendAttributes() {
        // Build the message
        HiveNetMessage message = new HiveNetMessage();
        message.cmd = "attr";
        message.args = new String[6 + this.getPlayer().playerStats.states.size()];

        message.args[0] = String.valueOf(this.getPlayer().playerStats.hunger);
        message.args[1] = String.valueOf(this.getPlayer().playerStats.thirst);
        message.args[2] = String.valueOf(this.getPlayer().playerStats.health);
        message.args[3] = String.valueOf(this.getPlayer().playerStats.energy);
        message.args[4] = (this.getState().isDead ? "t" : "f");
        message.args[5] = (this.isSpeaking() ? "t" : "f");

//        int i = 1;
//        for (PlayerDataState s : this.getPlayer().playerStats.states) {
//            message.args[3 + i++] = String.valueOf(s.getByte());
//        }

        // Emit the change to the client
        this.sendUdp(message.toString());
    }

    public NetworkMode getNetworkMode() {
        return networkMode;
    }

    public void setNetworkMode(NetworkMode networkMode) {
        System.out.println("[NET]: User " + this.player.id + " changed to " + networkMode.name());
        this.networkMode = networkMode;
    }

    public void syncEntity(GameEntityModel entityModel, WorldChunk worldChunk, boolean force) {
        this.syncEntity(entityModel, worldChunk, force, false);
    }

    public void syncEntity(GameEntityModel entityModel, WorldChunk worldChunk, boolean force, boolean useTcp) {

        boolean sync = false;
        if (this.loadedChunks.containsKey(worldChunk.getChunkCords().toString())) {
            // Has the chunk loaded
            if (this.loadedChunks.get(worldChunk.getChunkCords().toString()).containsKey(entityModel.uuid)) {
                // Has the entity loaded

                entityModel.entityData.onSync();

                if (!this.loadedChunks.get(worldChunk.getChunkCords().toString()).get(entityModel.uuid).equalsIgnoreCase(entityModel.entityHash())) {
                    // Has a diffrent hash
                    sync = true;
                }
            } else {
                sync = true;
            }
        }

        if (force) {
            sync = true;
        }

        if (sync) {
            if (useTcp) {
                this.sendTcp("esync|" + entityModel.entityData.toJsonData());
            } else {
                this.sendUdp("esync|" + entityModel.entityData.toJsonData());
            }
            this.loadedChunks.get(worldChunk.getChunkCords().toString()).put(entityModel.uuid, entityModel.entityHash());
        }
    }

    public void despawnEntity(GameEntityModel entityModel, WorldChunk worldChunk) {
        this.despawnEntity(entityModel, worldChunk, true);
    }

    public void despawnEntity(GameEntityModel entityModel, WorldChunk worldChunk, boolean useTcp) {
        if (useTcp) {
            this.sendTcp("edel|" + entityModel.uuid.toString());
        } else {
            this.sendUdp("edel|" + entityModel.uuid.toString());
        }
        this.loadedChunks.get(worldChunk.getChunkCords().toString()).remove(entityModel.uuid);
    }

    public boolean isChunkIsView(WorldChunk chunk) {
        return DedicatedServer.instance.getWorld().isChunkInView(this.player.location, this.renderDistance, chunk);
    }

    public List<WorldChunk> getChunksInRenderDistance(float distance) {
        List<WorldChunk> chunks = DedicatedServer.instance.getWorld().getChunksAroundLocation(this.player.location, distance);
        return chunks;
    }

    public void subscribeToChunk(WorldChunk chunk) {
        this.loadedChunks.put(chunk.getChunkCords().toString(), new ConcurrentHashMap<>());
//        for (GameEntityModel model : chunk.getEntites().values()) {
//            this.syncEntity(model, chunk, false, true);
//        }
    }

    public void unsubscribeToChunk(WorldChunk chunk) {

        /*
         * Unload all entities
         * */
        for (GameEntityModel e : chunk.getEntites().values()) {
            this.despawnEntity(e, chunk, true);
        }

        this.loadedChunks.remove(chunk.getChunkCords().toString());
    }

//    public void loadChunk(WorldChunk chunk) {
//        // TODO: Send Compressed Chunk Data
//        if (chunk != null) {
//
////            System.out.println("CHUNK LOADED");
//
//            this.sendUdp("chunk|" + chunk.getChunkData());
//
////            System.out.println(chunk.getChunkData());
//
//            this.loadedChunks.put(chunk.getChunkCords().toString(), chunk.getHash());
//        }
//    }
//
//    public void unLoadChunk(WorldChunk chunk) {
//        if (chunk != null) {
//            // TODO: Don't update the chunk but keep entites in the world
//            this.sendUdp("unchunk|" + chunk.getChunkCords().toString());
//        }
//    }
//
//    public void updateChunk(WorldChunk chunk) {
//        if (chunk != null) {
//            // Get the current hash for this chunk
//            String playerHash = this.loadedChunks.get(chunk.getChunkCords().toString());
//
//            if (!playerHash.equalsIgnoreCase(chunk.getHash())) {
//                LinkedList<ChunkChange> cc = chunk.getChangeListFromHash(playerHash);
//
//                JsonArray rootChange = new JsonArray();
//                for (ChunkChange c : cc) {
//                    rootChange.add(c.toJson());
//                }
//
//                if (rootChange.size() > 0) {
//
//                    JsonObject changeObj = new JsonObject();
//                    changeObj.addProperty("c", chunk.getChunkCords().toString());
//                    changeObj.addProperty("h", chunk.getHash());
//
////                        ChunkChange p = rootChange;
////                        while (p.next != null) {
////                            changes.add(p.toJson());
////                            p = p.next;
////                        }
//
//                    changeObj.add("e", rootChange);
//
//                    this.sendUdp("chunku|" + changeObj.toString());
//                    this.loadedChunks.put(chunk.getChunkCords().toString(), chunk.getHash());
//                }
//            }
//        }
//
//        // TODO: Update the chunk
////        this.sendUdp("chunk|" + LowEntry.bytesToBase64(chunk.getChunkData()));
//    }
//
//    public void setChunkHash(String chunk, String hash) {
//        this.loadedChunks.put(chunk, hash);
//    }
//
//    public void syncChunk(WorldChunk chunk) {
//        if (!this.loadedChunks.containsKey(chunk.getChunkCords().toString())) {
//            // Should update it
//            this.loadChunk(chunk);
//        }
//    }

    public void sendIsolatedEnviromentUpdate(float time, GameWeather weather) {
        DedicatedServer.get(EnvironmentService.class).emitEnvironmentChange(this, true);
    }

    public float getRenderDistance() {
        return renderDistance;
    }

    public void setRenderDistance(float renderDistance) {
        this.renderDistance = renderDistance;
    }

    public boolean isAdmin() {
        return DedicatedServer.admins.contains(this.getPlayer().id);
    }

    public void updateCraftingUI() {
        if (this.getOpenUI() != null && CraftingUI.class.isAssignableFrom(this.getOpenUI().getClass())) {
            this.getOpenUI().update(this);
        }
    }

    public void playBackgroundSound(GameSounds sound, float volume, float pitch) {
        this.sendTcp("pbgm|" + sound.name() + "|" + volume + "|" + pitch);
        this.bgSound = sound;
    }

    public void syncToAmbientWorldSound() {
        if (this.bgSound == GameSounds.BG1 || this.bgSound == GameSounds.BG2 || this.bgSound == GameSounds.Night) {
            this.playBackgroundSound(EnvironmentService.currentWorldAmbient, 1f, 1f);
        }
    }

    public GameSounds getBgSound() {
        return bgSound;
    }

    public void stopBackgroundSound() {
        this.sendTcp("sbgm|0");
    }

    public void takeDamage(float amt) {
        this.playAnimation(Animation.TAKE_HIT);
        this.broadcastState();
        DedicatedServer.instance.getWorld().playSoundAtLocation(GameSounds.TAKE_HIT, this.getPlayer().location, 500, 1f, 1f);
        this.getPlayer().playerStats.health -= amt;
    }

    public void displayLoadingScreen(String message, float percent) {
        this.sendTcp("loadings|" + message + "|" + percent);
    }

    public float getLOD(Vector3 location) {
        location.z = 0;
        Vector3 pl = this.getPlayer().location.toVector();
        pl.z = 0;
        return (float) Math.floor(pl.dst(location) / this.renderDistance);
    }

    public float getLOD(Location location) {
        return this.getLOD(location.toVector());
    }

    public void setOnlineTime() {
        this.onlineSince = System.currentTimeMillis();
    }

    public void hideLoadingScreen() {
        this.sendTcp("loadingh|");
    }

    public void syncChunkLOD(WorldChunk chunk, boolean force, boolean useTcp) {
        String chunkId = chunk.getChunkCords().toString();
        boolean shouldUpdate = false;
        long nextUpdate = 0L;

        Vector3 chunkCenter = chunk.getCenter().setZ(0).toVector();

        float lod = this.getLOD(chunkCenter);
        if (lod <= 0) {
            // LOD 0, Always Update
            shouldUpdate = true;
        } else if (lod <= 1) {
            // LOD 1, Every 15 seconds
            if (!this.chunkLODState.containsKey(chunkId) || (this.chunkLODState.get(chunkId) <= System.currentTimeMillis())) {
                shouldUpdate = true;
                nextUpdate = (System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(15));
                this.chunkLODState.put(chunkId, nextUpdate);
            }
        } else if (lod <= 2) {
            // LOD 2, Ever 30 seconds
            if (!this.chunkLODState.containsKey(chunkId) || (this.chunkLODState.get(chunkId) <= System.currentTimeMillis())) {
                shouldUpdate = true;
                nextUpdate = (System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(30));
                this.chunkLODState.put(chunkId, nextUpdate);
            }
        } else if (lod >= 3) {
            // LOD 3, Every 60 seconds
            if (!this.chunkLODState.containsKey(chunkId) || (this.chunkLODState.get(chunkId) <= System.currentTimeMillis())) {
                shouldUpdate = true;
                nextUpdate = (System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(60));
                this.chunkLODState.put(chunkId, nextUpdate);
            }
        }

        if (shouldUpdate || force) {
            // Flush the update for this chunk
            for (GameEntityModel entityModel : chunk.getEntites().values()) {

                GameEntity e = entityModel.entityData;
                if (e.useSpacialLoading) {
                    if (e.spacialLOD >= lod) {
                        this.syncEntity(entityModel, chunk, force, useTcp);
                    } else {
                        this.despawnEntity(entityModel, chunk, useTcp);
                    }
                } else {
                    this.syncEntity(entityModel, chunk, force, useTcp);
                }
            }
        }
    }

    public void syncChunkLODs(boolean force, boolean useTcp) {
        for (WorldChunk[] chunks : DedicatedServer.instance.getWorld().getChunks()) {
            for (WorldChunk chunk : chunks) {
                this.syncChunkLOD(chunk, force, useTcp);
            }
        }
    }

    public void applyFallDamage(float speed, float fellDistance, float multi) {
        float points = fellDistance / 50;
        float speedPoints = speed / 50;
        float damage = (points + speedPoints) * multi;
//        this.takeDamage(damage);
        float newDamage = MathUtil.map(this.maxspeed, 0, 10000, 0, 100);
        System.out.println("FALL DAMAGE: " + newDamage);
        this.maxspeed = 0;
    }

    public void resetFallDamage() {
        this.lastLocation = null;
        this.lastLocationTime = 0L;
        this.isFalling = false;
        this.fallSpeed = 0;
        this.fallStartAt = null;
    }

    public float noiseRadius() {
        if (this.speed <= 100) {
            // Still
            return 0;
        } else if (this.speed <= 300) {
            return 200;
        } else if (this.speed <= 600) {
            return 400;
        } else if (this.speed <= 1000) {
            return 600;
        }

        return 0;
    }

    public void showArrowTrail(Location start, Location end) {
        this.sendTcp("arroweff|" + start.toString() + "|" + end.toString());
    }

    public void sendOwnershipRequest(GameEntity entity, Location location, String goal, JsonObject data) {
        this.sendTcp("aia|" + entity.uuid.toString() + "|" + location.toString() + "|" + goal + "|" + data.toString());
    }

    public void comsumeEnergy(float amt) {
        this.player.playerStats.energy -= amt;
    }

    public boolean canUseEnergy(float amt) {
        if ((this.player.playerStats.energy - amt) >= 0) {
            return true;
        }

        return false;
    }

    public boolean inHandDurability(float amt) {
        InventoryStack inHand = this.getPlayer().equipmentSlots.inHand;
        if (inHand != null) {
            if (inHand.getItem().isHasDurability()) {
                // Has Durability
                float r = inHand.getItem().useDurability(amt);

//                this.syncHotbar();
                this.updatePlayerInventory();

                if (r <= 0) {
                    this.breakItemInSlot(EquipmentSlot.PRIMARY);
                    return false;
                }
            }
        }

        return true;
    }

    public void breakItemInSlot(EquipmentSlot slot) {
        if (slot == EquipmentSlot.PRIMARY) {
            // Is an inventory Item
            InventoryStack item = this.getPlayer().equipmentSlots.inHand;
            if (item != null) {
//                item.setAmount(0);
                item.clear();
                this.getPlayer().equipmentSlots.setWeapon(null);
//                this.getPlayer().inventory.update();
            }
        } else {
            InventoryStack item = this.getPlayer().equipmentSlots.getFromSlotName(slot);
            if (item != null) {
                item.clear();
                this.getPlayer().equipmentSlots.setBySlotName(slot, null);
            }
        }

        this.updatePlayerInventory();
        this.syncEquipmentSlots();

        this.playSoundAtPlayer(GameSounds.ToolBreak, .5f, .25f);
    }

    public void calcFallSpeed(Location location) {
        if (this.takeFallDamage) {
            /*
             * Check if falling
             * */
            float diffInZ = Math.abs(location.getZ() - this.lastLocation.getZ());

            // Calc the dist by the time

            long milliDiff = System.currentTimeMillis() - this.lastLocationTime;
//                float dist = location.toVector().dst(this.lastLocation.toVector());
            float dist = location.getZ() - this.lastLocation.getZ();

            this.fallSpeed = dist / ((float) milliDiff / 1000); // cm/s
            this.lastLocation = location;
            this.lastLocationTime = System.currentTimeMillis();
//                System.out.println("distance: "+ dist);
            if (-dist > 50) {
                System.out.println(this.fallSpeed);
                if (-this.fallSpeed > this.maxspeed) {
                    this.maxspeed = -this.fallSpeed;
                }
            }

            if (diffInZ > 30 && !this.isFalling) {
                // Is Failling?
                this.isFalling = true;
                this.fallStartAt = location;
//                    this.fallSpeed = this.speed;
            } else if (diffInZ < 5 && this.isFalling) {
                // Was falling and is not now
                float fellHeight = this.fallStartAt.getZ() - location.getZ();
                this.isFalling = false;
                this.applyFallDamage(this.fallSpeed, fellHeight, 1);
            }
        }
    }

    public void calcSpeed(Location location) {
        if (this.lastLocation == null) {
            this.lastLocation = location;
            this.lastLocationTime = System.currentTimeMillis();
            this.speed = -1;
        } else {
            long milliDiff = System.currentTimeMillis() - this.lastLocationTime;
            float dist = location.dist(this.lastLocation);
            this.speed = dist / ((float) milliDiff / 1000); // cm/s
        }
        this.calcFallSpeed(location);
    }

    public void sendVOIPData(int voipId, float volume, byte[] data) {
        ByteBuffer buffer = ByteBuffer.allocate(65507);
        ByteDataWriter dataWriter = new ByteBufferDataWriter(buffer);
        dataWriter.add(2);
        dataWriter.add(voipId);
        dataWriter.add(volume);
        dataWriter.add(data);

        this.socketClient.sendUnreliableMessage(dataWriter.getBytes());
    }

    public boolean isSpeaking() {
        return TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - this.lastVoipPacket) <= 1;
    }

    public Long getLastVoipPacket() {
        return lastVoipPacket;
    }

    public void setLastVoipPacket(Long lastVoipPacket) {
        this.lastVoipPacket = lastVoipPacket;
    }
}
