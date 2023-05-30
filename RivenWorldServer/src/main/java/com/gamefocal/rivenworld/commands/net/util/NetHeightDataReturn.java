package com.gamefocal.rivenworld.commands.net.util;

import com.gamefocal.rivenworld.entites.net.*;
import com.gamefocal.rivenworld.service.WorldHeightUtilityService;
import lowentry.ue4.classes.ByteDataReader;
import lowentry.ue4.classes.bytedata.reader.ByteArrayDataReader;
import lowentry.ue4.library.LowEntry;

@Command(name = "swhvr", sources = "tcp")
public class NetHeightDataReturn extends HiveCommand {
    @Override
    public void onCommand(HiveNetMessage message, CommandSource source, HiveNetConnection netConnection) throws Exception {

        System.out.println("HM Return.");

        try {
            if (WorldHeightUtilityService.processHeightData) {
                byte[] data = LowEntry.decompressLzf(LowEntry.base64ToBytes(message.args[0]));
                ByteDataReader reader = new ByteArrayDataReader(data);
                float[] floats = reader.getFloatArray();
                boolean[] bools = reader.getBooleanArray();
                WorldHeightUtilityService.saveAndNext(floats, bools);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
