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
            } else if (subCmd.equalsIgnoreCase("export")) {
                /*
                 * Export the data to the world.bin file
                 * */
                WorldHeightUtilityService.exportToFile();
            } else if (subCmd.equalsIgnoreCase("reload")) {
                /*
                 * Load from the utility to the world in run time.
                 * */
                WorldHeightUtilityService.loadIntoWorld();
            }

        }
    }
}
