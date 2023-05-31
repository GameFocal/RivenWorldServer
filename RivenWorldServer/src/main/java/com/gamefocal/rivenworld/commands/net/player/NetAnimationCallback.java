package com.gamefocal.rivenworld.commands.net.player;

import com.gamefocal.rivenworld.entites.net.*;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Command(name = "animcb", sources = "tcp")
public class NetAnimationCallback extends HiveCommand {

    public static ConcurrentHashMap<UUID, Long> lastHit = new ConcurrentHashMap<>();

    @Override
    public void onCommand(HiveNetMessage message, CommandSource source, HiveNetConnection netConnection) throws Exception {

//        if (lastHit.containsKey(netConnection.getUuid())) {
//            Long hitLast = lastHit.get(netConnection.getUuid());
//            if ((System.currentTimeMillis() - hitLast) <= 800) {
//                return;
//            }
//        }
////        float quickCnt = Float.parseFloat(message.args[0]);
//        lastHit.put(netConnection.getUuid(), System.currentTimeMillis());

//        System.out.println("CALLBACK");

        if (netConnection.getAnimationCallback() != null) {

//            System.out.println("CALLBACK CLEAR");

            netConnection.getAnimationCallback().onRun(netConnection, message.args);
            netConnection.setAnimationCallback(null);
        } else {
//            System.out.println("NO CALLBACK");
        }
    }
}
