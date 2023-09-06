package com.gamefocal.rivenworld.service;

import com.badlogic.gdx.graphics.Color;
import com.gamefocal.rivenworld.DedicatedServer;
import com.gamefocal.rivenworld.entites.events.EventHandler;
import com.gamefocal.rivenworld.entites.events.EventInterface;
import com.gamefocal.rivenworld.entites.events.EventPriority;
import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.entites.prefab.Prefab;
import com.gamefocal.rivenworld.entites.prefab.PrefabRecord;
import com.gamefocal.rivenworld.entites.service.HiveService;
import com.gamefocal.rivenworld.entites.util.JarUtil;
import com.gamefocal.rivenworld.events.game.ServerReadyEvent;
import com.gamefocal.rivenworld.events.game.ServerWorldSyncEvent;
import com.gamefocal.rivenworld.events.world.WorldGenerateEvent;
import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.entites.crops.CropEntity;
import com.gamefocal.rivenworld.game.util.Location;
import com.gamefocal.rivenworld.game.util.RectangleProperties;
import com.google.auto.service.AutoService;
import com.google.common.io.Files;
import com.google.gson.JsonElement;
import lowentry.ue4.library.LowEntry;
import org.apache.commons.io.FileUtils;

import javax.inject.Singleton;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.*;

@AutoService(HiveService.class)
@Singleton
public class PrefabService implements HiveService<PrefabService>, EventInterface {

    public static Location a = null;
    public static Location b = null;
    public static LinkedList<UUID> inSelectMode = new LinkedList<>();

    private static File prefabFolder = new File("prefabs");

    public static void saveEntitesToPrefab(Prefab prefab, String name) {

        JsonElement a = DedicatedServer.gson.toJsonTree(prefab, Prefab.class);

        String jsonString = a.toString();

        byte[] compressed = LowEntry.compressLzf(jsonString.getBytes(StandardCharsets.UTF_8));

        try {

            System.out.println(prefabFolder.getPath() + "/" + name + ".prefab");

            File prefabFile = new File(prefabFolder.getPath() + "/" + name + ".prefab");

            Files.createParentDirs(prefabFile);

            Files.write(compressed, prefabFile);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void loadPrefabsFromJar() throws URISyntaxException, IOException {
        FileUtils.forceMkdir(prefabFolder);

        JarUtil.extractResourcesFromJar("prefabs", prefabFolder.getName());

//        URL jarFolderURL = PrefabService.class.getClassLoader().getResource("prefabs/");
//        File jarFolder = new File(jarFolderURL.toURI());
//        if (jarFolder.exists()) {
//            FileUtils.copyDirectory(jarFolder, prefabFolder);
//        }
    }

    public static List<File> getPrefabs(File dir) {
        ArrayList<File> p = new ArrayList<>();

        for (Iterator i = FileUtils.iterateFiles(dir, new String[]{"prefab"}, true); i.hasNext(); ) {
            File f = (File) i.next();
            if (f != null) {
                p.add(f);
            }
        }

        return p;
    }

    public static void loadEntitesFromPrefab(String name) {

        File prefabFile = new File(prefabFolder.getPath() + "/" + name + ".prefab");

        if (prefabFile.exists()) {

            try {
                byte[] raw = Files.toByteArray(prefabFile);

                byte[] compressed = LowEntry.decompressLzf(raw);

                // Load it :)
                Prefab prefab = DedicatedServer.gson.fromJson(new String(compressed), Prefab.class);

                for (PrefabRecord r : prefab.getEntities()) {

                    GameEntity e = r.getGameEntity();
                    e.location = prefab.getBaseLocation().toWorldSpace(r.getRelative());

                    if (prefab.getVersion() < 1.051) {
                        e.location.addZ(-3000);
                    }

                    boolean spawn = true;

                    // Check is we can spawn this, and if another entity has a similar location.
                    List<GameEntity> nearby = DedicatedServer.instance.getWorld().getCollisionManager().getNearbyEntities(e);
                    if (nearby.size() > 0) {
                        for (GameEntity n : nearby) {
                            if (e.getClass().isAssignableFrom(n.getClass())) {
                                // Same class type
                                if (e.location.equals(n.location)) {
                                    // Same location
                                    spawn = false;
                                    break;
                                }
                            }
                        }
                    }

                    if (CropEntity.class.isAssignableFrom(e.getClass())) {
                        spawn = false;
                    }

                    if (spawn) {
                        e.uuid = null;
                        DedicatedServer.instance.getWorld().spawn(e, e.location);
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public void init() {
    }

    @EventHandler(priority = EventPriority.FIRST)
    public void onWorldSyncEvent(ServerWorldSyncEvent event) {
        HiveNetConnection connection = event.getConnection();

        if (a != null || b != null) {
            if (!inSelectMode.contains(connection.getUuid())) {
                inSelectMode.add(connection.getUuid());
            }
        }

        // Render the preview
        if (a != null && b != null) {
            RectangleProperties sphere = RectangleProperties.computeProperties(a.toVector(), b.toVector());

            Location location = Location.fromVector(sphere.getCenter());
            location.setRotation(0, 0, sphere.getRotation());

            connection.showClaimRegion(
                    location,
                    Math.max(sphere.getWidth(), sphere.getHeight()),
                    Color.GREEN
            );
            return;
        }

        if (a != null) {
            RectangleProperties sphere = RectangleProperties.computeProperties(a.toVector(), connection.getPlayer().location.toVector());

            Location location = Location.fromVector(sphere.getCenter());
            location.setRotation(0, 0, sphere.getRotation());

            connection.showClaimRegion(
                    location,
                    Math.max(sphere.getWidth(), sphere.getHeight()),
                    Color.ORANGE
            );
            return;
        }

        if (inSelectMode.contains(connection.getUuid())) {
            connection.hideClaimRegions();
            inSelectMode.remove(connection.getUuid());
            return;
        }
    }

    @EventHandler
    public void onServerReady(ServerReadyEvent event) {
    }

    @EventHandler
    public void onWorldGenerate(WorldGenerateEvent event) {
        try {
            loadPrefabsFromJar();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        /*
         * Load the prefabs when the world generates
         * */
        for (File f : getPrefabs(prefabFolder)) {
            loadEntitesFromPrefab(Files.getNameWithoutExtension(f.getName()));
        }
    }

}
