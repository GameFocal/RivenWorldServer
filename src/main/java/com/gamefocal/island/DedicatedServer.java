package com.gamefocal.island;

import com.gamefocal.island.entites.command.HiveConsoleCommand;
import com.gamefocal.island.entites.config.HiveConfigFile;
import com.gamefocal.island.entites.events.EventManager;
import com.gamefocal.island.entites.injection.AppInjector;
import com.gamefocal.island.entites.injection.GuiceServiceLoader;
import com.gamefocal.island.entites.injection.InjectionModule;
import com.gamefocal.island.entites.injection.InjectionRoot;
import com.gamefocal.island.entites.orm.ORM;
import com.gamefocal.island.entites.service.HiveService;
import com.gamefocal.island.service.CommandService;
import com.google.inject.Inject;
import com.google.inject.Injector;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.UUID;

public class DedicatedServer implements InjectionRoot {

    public static boolean isRunning = true;

    public static DedicatedServer instance;

    private final HiveConfigFile configFile;

    private static String worldURL;

    private static ORM orm;

    private String worldName;

    @Inject
    Injector injector;

    public DedicatedServer(String configPath) {
        instance = this;
        AppInjector.registerInjectionRoot(this);
        AppInjector.registerRootModule(new InjectionModule(this));
        AppInjector.boot();

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
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        /*
         * Load the DB
         * */
        orm = new ORM(worldName);

        GuiceServiceLoader.getGlobalInjector().injectMembers(EventManager.class);

        /*
         * Load Services
         * */
        GuiceServiceLoader.load(HiveService.class).forEach(hiveService -> {
            System.out.println("--- Starting " + hiveService.getClass().getSimpleName());
            hiveService.init();
        });

        System.out.println("System Ready.");
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

                    HiveConsoleCommand ci = DedicatedServer.get(CommandService.class).findConsoleCommand(cmdName);

                    if (ci != null) {
                        if (!ci.onCommand(cmdName, args)) {
                            System.out.println("Error Processing Command");
                        }
                    } else {
                        System.err.println("CMD Not Found");
                    }
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
}
