package com.gamefocal.rivenworld;

import com.badlogic.gdx.graphics.Color;
import com.gamefocal.rivenworld.dev.mapbox.RivenWorldMapBox;
import com.gamefocal.rivenworld.entites.config.HiveConfigFile;
import com.gamefocal.rivenworld.entites.events.EventManager;
import com.gamefocal.rivenworld.entites.injection.AppInjector;
import com.gamefocal.rivenworld.entites.injection.GuiceServiceLoader;
import com.gamefocal.rivenworld.entites.injection.InjectionModule;
import com.gamefocal.rivenworld.entites.injection.InjectionRoot;
import com.gamefocal.rivenworld.entites.license.ServerLicenseManager;
import com.gamefocal.rivenworld.entites.net.ChatColor;
import com.gamefocal.rivenworld.entites.net.CommandSource;
import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.entites.service.HiveService;
import com.gamefocal.rivenworld.entites.util.gson.LocationDeSerializer;
import com.gamefocal.rivenworld.entites.util.gson.LocationSerializer;
import com.gamefocal.rivenworld.entites.util.gson.classr.GameClassDeSerializer;
import com.gamefocal.rivenworld.entites.util.gson.classr.GameClassSerializer;
import com.gamefocal.rivenworld.entites.util.gson.entity.GameEntityDeSerializer;
import com.gamefocal.rivenworld.entites.util.gson.entity.GameEntitySerializer;
import com.gamefocal.rivenworld.entites.util.gson.items.InventoryItemDeSerializer;
import com.gamefocal.rivenworld.entites.util.gson.items.InventoryItemSerializer;
import com.gamefocal.rivenworld.entites.util.gson.recipie.GameRecipeDeSerializer;
import com.gamefocal.rivenworld.entites.util.gson.recipie.GameRecipeSerializer;
import com.gamefocal.rivenworld.events.game.ServerReadyEvent;
import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.tasks.seqence.ExecSequenceAction;
import com.gamefocal.rivenworld.game.tasks.seqence.WaitSequenceAction;
import com.gamefocal.rivenworld.game.util.TickUtil;
import com.gamefocal.rivenworld.game.world.World;
import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.inventory.InventoryItem;
import com.gamefocal.rivenworld.game.settings.GameSettings;
import com.gamefocal.rivenworld.game.util.Location;
import com.gamefocal.rivenworld.game.world.WorldChunk;
import com.gamefocal.rivenworld.models.GameEntityModel;
import com.gamefocal.rivenworld.service.CommandService;
import com.gamefocal.rivenworld.service.PlayerService;
import com.gamefocal.rivenworld.service.SaveService;
import com.gamefocal.rivenworld.service.TaskService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.google.inject.Inject;
import com.google.inject.Injector;
import io.airbrake.javabrake.Airbrake;
import org.apache.commons.io.IOUtils;

import javax.swing.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class DedicatedServer implements InjectionRoot {

    public static final float serverVersion = 1.04f;
    public static final String serverMinorVersion = "rc6";
    public static boolean isRunning = true;
    public static DedicatedServer instance;
    public static Gson gson;
    public static Long foliageVersion = 0L;
    public static Long serverStarted = 0L;
    public static LinkedList<String> admins = new LinkedList<>();
    public static ServerLicenseManager licenseManager;
    public static boolean isReady = false;
    public static GameSettings settings = new GameSettings();
    private static String worldURL;
    private final HiveConfigFile configFile;
    public static boolean isLocked = false;
    public static String lockMessage = "Server is locked";

    @Inject
    Injector injector;
    private World world;
    private String worldName;

    public DedicatedServer(String configPath) {
        instance = this;
        AppInjector.registerInjectionRoot(this);
        AppInjector.registerRootModule(new InjectionModule(this));
        AppInjector.boot();

        // Build the GSON class
        GsonBuilder builder = getGsonBuilder();
        gson = builder.create();

        /*
         * Load the config
         * */
        if (!Files.exists(Paths.get(configPath))) {
            System.out.println("Hive Config not found... generating...");
            ClassLoader classloader = getClass().getClassLoader();

            try (InputStream in = classloader.getResourceAsStream(configPath);
                 BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {

                // Default
                String defaultConfig = IOUtils.toString(reader);

                Files.write(Paths.get(configPath), defaultConfig.getBytes(StandardCharsets.UTF_8));

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        /*
         * Load the admin file
         * */
        if (!Files.exists(Paths.get("admin.json"))) {
            try {
                Files.write(Paths.get("admin.json"), new String("[]").getBytes(StandardCharsets.UTF_8), StandardOpenOption.CREATE, StandardOpenOption.WRITE);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (Files.exists(Paths.get("admin.json"))) {
            try {
                JsonArray adminList = JsonParser.parseString(Files.readString(Paths.get("admin.json"))).getAsJsonArray();

                for (int i = 0; i < adminList.size(); i++) {
                    admins.add(adminList.get(i).getAsString());
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        /*
         * Load the settings file
         * */
        if (!Files.exists(Paths.get("settings.json"))) {
            GsonBuilder builder1 = getGsonBuilder();
            builder1.setPrettyPrinting();
            Gson g = builder1.create();

            try {
                Files.write(Paths.get("settings.json"), g.toJson(DedicatedServer.settings, GameSettings.class).getBytes(), StandardOpenOption.CREATE, StandardOpenOption.WRITE);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (Files.exists(Paths.get("admin.json"))) {
            try {
                DedicatedServer.settings = DedicatedServer.gson.fromJson(Files.readString(Paths.get("settings.json")), GameSettings.class);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        System.out.println("Loading Config into System...");
        configFile = new HiveConfigFile(new File(configPath));
        try {
            configFile.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (!configFile.getConfig().has("uuid")) {
            System.out.println("Config does not have UUID set... generating node id...");
            configFile.getConfig().addProperty("uuid", UUID.randomUUID().toString());
            try {
                configFile.save();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (!configFile.getConfig().has("world")) {
            System.out.println("No world set in config... setting to server");
            configFile.getConfig().addProperty("world", "world");
            try {
                configFile.save();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (!configFile.getConfig().has("license") || configFile.getConfig().get("license").getAsString().equalsIgnoreCase("CHANGE_ME")) {
            System.err.println("[Hive]: Please register your server at https://hive.rivenworld.net and then set the license in your config.json");
            System.exit(0);
        }

        licenseManager = new ServerLicenseManager(configFile.getConfig().get("license").getAsString(), configFile);
        licenseManager.register();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {

//            /*
//            * Remove matts from any furance
//            * */
//            for (UUID eUUID : DedicatedServer.instance.getWorld().entityChunkIndex.keySet()) {
//                GameEntityModel m = DedicatedServer.instance.getWorld().getEntityFromId(eUUID);
//                if(m != null) {
//                    if(CraftingStation.class.isAssignableFrom(m.entityData.getClass())) {
//
//                    }
//                }
//            }

//            SaveService.saveGame();
            SaveService.allowNewSaves = false;
            while (SaveService.saveQueue.size() > 0) {
                // Lock thread and allow for everything to flush to save file
                Thread.yield();
            }
            licenseManager.close();
        }));

        worldName = ((configFile.getConfig().has("world")) ? configFile.getConfig().get("world").getAsString() : "world");

        worldURL = "jdbc:sqlite:" + worldName;

        try (Connection conn = DriverManager.getConnection(worldURL)) {
            if (conn != null) {
                DatabaseMetaData meta = conn.getMetaData();
//                System.out.println("The driver name is " + meta.getDriverName());
                System.out.println("A new world db has been created.");
                conn.close();
            }

        } catch (SQLException e) {
            Airbrake.report(e);
            e.printStackTrace();
        }

//        /*
//         * Load the DB
//         * */
//        orm = new ORM(worldName);

        GuiceServiceLoader.getGlobalInjector().injectMembers(EventManager.class);

        /*
         * Load Services
         * */
        //            System.out.println("--- Loading " + hiveService.getClass().getSimpleName());
        GuiceServiceLoader.load(HiveService.class).forEach(HiveService::init);

//        JFrame frame = new JFrame("MainForm");
//        frame.setContentPane(new RivenWorldMapBox().mainPanel);
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        frame.setTitle("RivenWorld Mapbox Debugger");
//        frame.pack();
//        frame.setVisible(true);

        world = new World();

        if (world.isFreshWorld()) {
            System.out.println("Fresh world... running world create...");
            World.generateNewWorld();
        } else {
            System.out.println("Existing world... loading now...");
            world.prepareWorld();
        }

        /*
         * Setup tasks
         * */
        serverStarted = System.currentTimeMillis();

//        TaskService.scheduleRepeatingTask(()->{
//            int totalEntites = 0;
//            for (WorldChunk[] chunks : DedicatedServer.instance.getWorld().getChunks()) {
//                for (WorldChunk chunk : chunks) {
//                    for (GameEntityModel e : chunk.getEntites().values()) {
//                        totalEntites++;
//                        for (HiveNetConnection connection : DedicatedServer.get(PlayerService.class).players.values()) {
//                            connection.drawDebugLine(Color.GREEN,e.entityData.location.cpy().setZ(100000),e.entityData.location.cpy(),1);
//                        }
//                    }
//                }
//            }
//
//            System.out.println("Entites: " + totalEntites);
//        },120L,120L,false);

        // Emit a HB every 30 seconds to the hive using the server license and sessionId
//        TaskService.scheduleRepeatingTask(() -> {
//            DedicatedServer.licenseManager.hb();
//        }, TickUtil.SECONDS(30), TickUtil.SECONDS(30), false);

//        isReady = true;
//        System.out.println("Server Ready.");
//        new ServerReadyEvent().call();
    }

    public static long getUptimeInMilli() {
        return System.currentTimeMillis() - serverStarted;
    }

    public static long getUptimeInSeconds() {
        return TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - serverStarted);
    }

    public static void awaitConsoleInput() {
        // Enter data using BufferReader
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(System.in));

        System.out.println("Server Running and awaiting command...");

        while (isRunning) {
            // Reading data using readLine
            try {
                String name = reader.readLine();

                if (name != null) {
                    System.out.println("CONSOLE CMD: " + name);

                    String[] parts = name.split(" ");
                    String cmdName = parts[0];
                    String[] args = new String[0];
                    if (parts.length > 1) {
                        args = Arrays.copyOfRange(parts, 1, parts.length);
                    }

                    StringBuilder cmdString = new StringBuilder();
                    cmdString.append(cmdName);
                    if (args.length > 0) {
                        for (String s : args) {
                            cmdString.append("|").append(s);
                        }
                    }

                    DedicatedServer.get(CommandService.class).handleCommand(cmdString.toString(), CommandSource.CONSOLE, null);

//                    HiveConsoleCommand ci = DedicatedServer.get(CommandService.class).findConsoleCommand(cmdName);
//
//                    if (ci != null) {
//                        if (!ci.onCommand(cmdName, args)) {
//                            System.out.println("Error Processing Command");
//                        }
//                    } else {
//                        System.err.println("CMD Not Found");
//                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static <T> T get(Class<? extends T> type) {
        return instance.getInjector().getInstance(type);
    }

    public static HiveNetConnection getPlayerFromName(String name) {
        for (HiveNetConnection c : DedicatedServer.get(PlayerService.class).players.values()) {
            if (c.getPlayer().displayName.equalsIgnoreCase(name)) {
                return c;
            }
        }

        return null;
    }

    public static void kickAllPlayers(String msg) {
        for (HiveNetConnection connection : DedicatedServer.get(PlayerService.class).players.values()) {
            connection.kick(msg);
        }
    }

    public static void sendChatMessageToAll(String msg) {
        for (HiveNetConnection connection : DedicatedServer.get(PlayerService.class).players.values()) {
            connection.sendChatMessage(msg);
        }
    }

    public static void cleanStaleClients() {
        for (HiveNetConnection connection : DedicatedServer.get(PlayerService.class).players.values()) {
            // Check for dirty connections with the set timeout
            if (!connection.connectionIsAlive()) {
                connection.kick("Timeout");
            }
        }
    }

    public Injector getInjector() {
        return injector;
    }

    public HiveConfigFile getConfigFile() {
        return configFile;
    }

    public World getWorld() {
        return world;
    }

    public static boolean playerIsOnline(UUID uuid) {
        return DedicatedServer.get(PlayerService.class).players.containsKey(uuid);
    }

    public static GsonBuilder getGsonBuilder() {
        /*
         * Custom Gson config
         * */
        GsonBuilder builder = new GsonBuilder();

        // Location Serialization
        builder.registerTypeAdapter(Location.class, new LocationSerializer());
        builder.registerTypeAdapter(Location.class, new LocationDeSerializer());

        // InventoryItem Serialization
        builder.registerTypeAdapter(InventoryItem.class, new InventoryItemSerializer());
        builder.registerTypeAdapter(InventoryItem.class, new InventoryItemDeSerializer());

        // GameEntity Serialization
        builder.registerTypeAdapter(GameEntity.class, new GameEntitySerializer());
        builder.registerTypeAdapter(GameEntity.class, new GameEntityDeSerializer());

        // Crafting Serialization
        builder.registerTypeAdapter(CraftingRecipe.class, new GameRecipeSerializer());
        builder.registerTypeAdapter(CraftingRecipe.class, new GameRecipeDeSerializer());

        // Class Serialization
        builder.registerTypeAdapter(Class.class, new GameClassSerializer());
        builder.registerTypeAdapter(Class.class, new GameClassDeSerializer());

        return builder;
    }

    public static void shutdown(String msg) {

        DedicatedServer.isLocked = true;
        DedicatedServer.lockMessage = "Server shutting down";
        SaveService.allowNewSaves = false;

        TaskService.scheduleTaskSequence(false,
                new ExecSequenceAction() {
                    @Override
                    public void run() {
                        DedicatedServer.sendChatMessageToAll(ChatColor.RED + "Server Shutting Down...");
                    }
                },
                new ExecSequenceAction() {
                    @Override
                    public void run() {
                        DedicatedServer.kickAllPlayers(msg);
                    }
                },
                new WaitSequenceAction(TickUtil.MILLISECONDS(50)),
                new ExecSequenceAction() {
                    @Override
                    public void run() {
                        SaveService.syncNonEntities();
                    }
                },
                new WaitSequenceAction(TickUtil.MILLISECONDS(50)),
                new ExecSequenceAction() {
                    @Override
                    public void run() {
                        while (SaveService.saveQueue.size() > 0) {
                            try {
                                Thread.sleep(50);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        System.exit(0);
                    }
                }
        );
    }
}
