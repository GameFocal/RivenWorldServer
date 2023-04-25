package com.gamefocal.rivenworld.commands.chat.admin;

import com.badlogic.gdx.math.MathUtils;
import com.gamefocal.rivenworld.DedicatedServer;
import com.gamefocal.rivenworld.entites.net.*;
import com.gamefocal.rivenworld.game.util.BufferUtil;
import com.gamefocal.rivenworld.game.util.Location;
import com.gamefocal.rivenworld.service.RayService;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;

@Command(sources = "chat", name = "hmu")
public class BuildHeightData extends HiveCommand {

    public static ByteBuffer buffer;
    public static int[][] heightData;

    @Override
    public void onCommand(HiveNetMessage message, CommandSource source, HiveNetConnection netConnection) throws Exception {
        if (netConnection.isAdmin()) {
            /*
             * Rebuild the heightmap data
             * */

            //
//            ByteBuffer buffer = ByteBuffer.allocate(151393 * 2);

            String subCmd = message.args[0];
            if (subCmd.equalsIgnoreCase("start")) {

                new Thread(() -> {
                    int cells = 201753;
                    int realStart = -25180;
                    int realEnd = 176573;

                    int size = cells / 100;

                    buffer = ByteBuffer.allocate(size * size * Float.BYTES);

                    for (int x = 0; x < cells; x += 100) {
                        for (int y = 0; y < cells; y += 100) {
                            final float xx = MathUtils.map(0, cells, realStart, realEnd, x);
                            final float yy = MathUtils.map(0, cells, realStart, realEnd, y);

                            final int finalX = x / 100;
                            final int finalY = y / 100;
                            final int index = BufferUtil.getPositionInByteBuffer(buffer, size, finalX, finalY);
                            DedicatedServer.get(RayService.class).makeRequest(new Location(xx, yy, 0), 1, request -> {
                                System.out.println("(" + request.getCheckLocation() + "): " + request.getReturnedHeight() + " | " + index + " = " + finalX + ", " + finalY + " | " + buffer.capacity());
                                buffer.putFloat(index, request.getReturnedHeight());
                            });

                            Thread.yield();
                        }
                    }

                    System.out.println("Completed.");
                }).start();
            } else if (subCmd.equalsIgnoreCase("export")) {
                /*
                 * Export the data to the world.bin file
                 * */

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

        }
    }
}
