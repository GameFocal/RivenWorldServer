package com.gamefocal.island;

import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.collision.Sphere;
import com.gamefocal.island.entites.config.HiveConfigFile;
import com.gamefocal.island.entites.events.EventManager;
import com.gamefocal.island.entites.injection.AppInjector;
import com.gamefocal.island.entites.injection.GuiceServiceLoader;
import com.gamefocal.island.entites.injection.InjectionModule;
import com.gamefocal.island.entites.injection.InjectionRoot;
import com.gamefocal.island.entites.net.CommandSource;
import com.gamefocal.island.entites.net.HiveNetConnection;
import com.gamefocal.island.entites.service.HiveService;
import com.gamefocal.island.entites.util.gson.LocationDeSerializer;
import com.gamefocal.island.entites.util.gson.LocationSerializer;
import com.gamefocal.island.game.World;
import com.gamefocal.island.game.entites.blocks.ClayBlock;
import com.gamefocal.island.game.player.PlayerState;
import com.gamefocal.island.game.util.Location;
import com.gamefocal.island.game.util.TickUtil;
import com.gamefocal.island.models.GameEntityModel;
import com.gamefocal.island.service.CommandService;
import com.gamefocal.island.service.PlayerService;
import com.gamefocal.island.service.TaskService;
import com.google.gson.*;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class DedicatedServer implements InjectionRoot {

    public static boolean isRunning = true;

    public static DedicatedServer instance;
    private static String worldURL;
    private final HiveConfigFile configFile;
    @Inject
    Injector injector;
    private World world;
    private String worldName;
    public static Gson gson;
    public static Long foliageVersion = 0L;

    public static Long serverStarted = 0L;

    public DedicatedServer(String configPath) {
        instance = this;
        AppInjector.registerInjectionRoot(this);
        AppInjector.registerRootModule(new InjectionModule(this));
        AppInjector.boot();

        /*
         * Custom Gson config
         * */
        GsonBuilder builder = new GsonBuilder();

        // Location Serialization
        builder.registerTypeAdapter(Location.class, new LocationSerializer());
        builder.registerTypeAdapter(Location.class, new LocationDeSerializer());

        gson = builder.create();

        /*
         * Load the config
         * */
        if (!Files.exists(Paths.get(configPath))) {
            System.out.println("Hive Config not found... generating...");
            ClassLoader classloader = Thread.currentThread().getContextClassLoader();

            try (InputStream in = classloader.getResourceAsStream(configPath);
                 BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {

                // Default
                String defaultConfig = IOUtils.toString(reader);

                Files.write(Paths.get(configPath), defaultConfig.getBytes(StandardCharsets.UTF_8));

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

        worldName = ((configFile.getConfig().has("world")) ? configFile.getConfig().get("world").getAsString() : "world");

        worldURL = "jdbc:sqlite:" + worldName;

        try (Connection conn = DriverManager.getConnection(worldURL)) {
            if (conn != null) {
                DatabaseMetaData meta = conn.getMetaData();
                System.out.println("The driver name is " + meta.getDriverName());
                System.out.println("A new world db has been created.");
                conn.close();
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

//        /*
//         * Load the DB
//         * */
//        orm = new ORM(worldName);

        GuiceServiceLoader.getGlobalInjector().injectMembers(EventManager.class);

        /*
         * Load Services
         * */
        GuiceServiceLoader.load(HiveService.class).forEach(hiveService -> {
            System.out.println("--- Starting " + hiveService.getClass().getSimpleName());
            hiveService.init();
        });

        world = new World();

        if (world.isFreshWorld()) {
            System.out.println("Fresh world... running world create...");
            World.generateNewWorld();
        }

        /*
         * Setup tasks
         * */

        // Game List Reporting
        TaskService.scheduleRepeatingTask(() -> {
            HiveConfigFile configFile = DedicatedServer.instance.getConfigFile();

            JsonArray players = new JsonArray();

            JsonObject obj = configFile.getConfig().deepCopy();
            obj.add("players", players);

            try {
                HttpResponse<String> s = Unirest.post("https://api.gamefocal.com/riven/servers").body(obj.toString()).asString();
                JsonObject re = JsonParser.parseString(s.getBody()).getAsJsonObject();
                if (!re.has("success") || !re.get("success").getAsBoolean()) {
                    System.err.println("Failed to register server with hive... will try again later...");
                }
            } catch (UnirestException e) {
                e.printStackTrace();
            }
        }, TickUtil.SECONDS(5), TickUtil.MINUTES(5), true);

        serverStarted = System.currentTimeMillis();

        System.out.println("Server Ready.");
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

    public Injector getInjector() {
        return injector;
    }

    public HiveConfigFile getConfigFile() {
        return configFile;
    }

    public World getWorld() {
        return world;
    }
}
