package com.gamefocal.rivenworld.service;

import com.gamefocal.rivenworld.DedicatedServer;
import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.entites.service.HiveService;
import com.gamefocal.rivenworld.game.ai.path.WorldGrid;
import com.gamefocal.rivenworld.game.util.BufferUtil;
import com.gamefocal.rivenworld.game.util.Location;
import com.google.auto.service.AutoService;
import org.apache.commons.io.FileUtils;

import javax.inject.Singleton;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

@AutoService(HiveService.class)
@Singleton
public class WorldHeightUtilityService implements HiveService {

    public static WorldGrid grid;
    public static HiveNetConnection useConnection;
    public static boolean processHeightData = false;
    public static ByteBuffer buffer;
    public static Location currentLocation;
    public static Location startingLocation;
    public static int x = 0;
    public static int y = 0;

    public static void start(HiveNetConnection connection) {
        grid = DedicatedServer.instance.getWorld().getGrid();

        useConnection = connection;
        processHeightData = true;
        startingLocation = grid.get(0, 0).getGameLocation();
        currentLocation = startingLocation.cpy();

        x = 0;
        y = 0;
        int size = (int) Math.ceil(201753f / 100f);
        buffer = ByteBuffer.allocate(size * size * (2 + Float.BYTES)).order(ByteOrder.BIG_ENDIAN);

        scan();
    }

    public static void scan() {
//        currentLocation = grid.get(x, y).getGameLocation();

//        System.out.println("Scanning " + currentLocation + " -- " + grid.get(x, 2017));
        useConnection.sendTcp("swhv|" + grid.get(x, 0).getGameLocation() + "|" + grid.get(x, 2017).getGameLocation());
    }

    public static void saveAndNext(float[] values, boolean[] forest) {

        System.out.println("Reading in " + values.length + " height values...");

        int size = (int) Math.ceil(201753f / 100f);

        int localIn = 0;
        for (float v : values) {

            System.out.println("READ: " + x + "," + y);

            int index = BufferUtil.getPositionInByteBuffer(size, size, x, y++);
            buffer.put(index, (byte) 0x00); // Reserved for Biome
            buffer.put(index + 1, (byte) (forest[localIn] ? 0x01 : 0x00)); // Forest
            buffer.putFloat(index + 2, v); // Height

            localIn++;
        }

        x++;
        y = 0;

        scan();
    }

    public static void exportToFile() {
        new Thread(() -> {
            try {
                System.out.println("Writing World Data to File...");
                FileUtils.writeByteArrayToFile(new File("world.bin"), buffer.array());
                System.out.println("Completed.");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    @Override
    public void init() {
    }
}
