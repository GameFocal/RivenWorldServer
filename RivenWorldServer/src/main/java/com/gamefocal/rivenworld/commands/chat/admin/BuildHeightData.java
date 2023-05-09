package com.gamefocal.rivenworld.commands.chat.admin;

import com.badlogic.gdx.math.MathUtils;
import com.gamefocal.rivenworld.DedicatedServer;
import com.gamefocal.rivenworld.entites.net.*;
import com.gamefocal.rivenworld.game.util.BufferUtil;
import com.gamefocal.rivenworld.game.util.Location;
import com.gamefocal.rivenworld.service.RayService;
import com.gamefocal.rivenworld.service.WorldHeightUtilityService;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

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

                WorldHeightUtilityService.start(netConnection);
//
//                new Thread(() -> {
//                    int cells = 201753;
//                    int realStart = -25180;
//                    int realEnd = 176573;
//
//                    int size = (int) Math.ceil(cells / 100f);
//
//                    buffer = ByteBuffer.allocate(size * size * Float.BYTES).order(ByteOrder.BIG_ENDIAN);
//
//                    for (int x = 0; x < cells; x += 100) {
//                        for (int y = 0; y < cells; y += 100) {
//                            try {
//                                float xx = (x - 25180);
//                                float yy = (y - 25180);
//
//                                int finalX = (x / 100);
//                                int finalY = (y / 100);
//                                int index = BufferUtil.getPositionInByteBuffer(size, size, finalX, finalY);
//                                DedicatedServer.get(RayService.class).makeRequest(new Location(xx, yy, 0), 1, request -> {
//                                    buffer.asFloatBuffer().put(index, request.getReturnedHeight());
//                                });
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                            }
//
//                            try {
//                                Thread.sleep(1);
//                            } catch (InterruptedException e) {
//                                e.printStackTrace();
//                            }
//                        }
//
////                        try {
//////                            try {
//////                                FileUtils.writeByteArrayToFile(new File("world.bin"), buffer.array());
//////                            } catch (IOException e) {
//////                                e.printStackTrace();
//////                            }
////                            Thread.sleep(500);
////                        } catch (InterruptedException e) {
////                            e.printStackTrace();
////                        }
//                    }
//
//                    System.out.println("Writing World Data to File...");
//                    try {
//                        FileUtils.writeByteArrayToFile(new File("world.bin"), buffer.array());
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                    System.out.println("Completed.");
//
//                    System.out.println("Completed.");
//                }).start();
            } else if (subCmd.equalsIgnoreCase("export")) {
                /*
                 * Export the data to the world.bin file
                 * */

                WorldHeightUtilityService.exportToFile();

//                new Thread(() -> {
//                    try {
//                        System.out.println("Writing World Data to File...");
//                        FileUtils.writeByteArrayToFile(new File("world.bin"), buffer.array());
//                        System.out.println("Completed.");
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }).start();

            }

        }
    }
}
