package com.gamefocal.rivenworld.commands.net.player;

import com.gamefocal.rivenworld.DedicatedServer;
import com.gamefocal.rivenworld.entites.net.*;
import com.gamefocal.rivenworld.events.player.PlayerMoveEvent;
import com.gamefocal.rivenworld.game.player.PlayerBlendState;
import com.gamefocal.rivenworld.game.util.Location;
import com.gamefocal.rivenworld.models.PlayerModel;
import com.gamefocal.rivenworld.service.RayService;

@Command(name = "plmv", sources = "udp")
public class NetPlayerMove extends HiveCommand {
    @Override
    public void onCommand(HiveNetMessage message, CommandSource source, HiveNetConnection netConnection) throws Exception {

//        System.out.println(message.toString());

        PlayerModel p = netConnection.getPlayer();
        if (p != null) {

            if (!netConnection.isLoaded()) {
                return;
            }

            if (netConnection.isMovementDisabled()) {
                return;
            }

            if (!netConnection.getState().isDead) {

                String plLoc = message.args[0];

                Location l = Location.fromString(plLoc);

                PlayerMoveEvent event = new PlayerMoveEvent(netConnection, l).call();

                if (event.isCanceled()) {
                    return;
                }

                p.location = l;

                netConnection.getState().location = l;

                String stateString = message.args[1];

                PlayerBlendState state = new PlayerBlendState();
                state = DedicatedServer.gson.fromJson(stateString, PlayerBlendState.class);

                netConnection.getState().blendState = state;

                // Get the looking at value
                if (message.args.length >= 3) {
//                JsonObject d = JsonParser.parseString(message.args[2]).getAsJsonObject();
                    netConnection.processHitData(message.args[2]);
                }

                if (message.args.length >= 4) {
                    // Has a state hash
                    netConnection.setSyncHash(message.args[3]);
                }

                if (message.args.length >= 5) {
                    Location loc = Location.fromString(message.args[4]);
                    if (loc != null) {
                        netConnection.setForwardVector(loc.toVector());
                    }
//                netConnection.setForwardVector(Location.fromString(message.args[4]).toVector());
                }

                if (message.args.length >= 6) {
                    // Terrain Hit Location
                    Location location = Location.fromString(message.args[5]);
                    if (location != null) {
                        netConnection.setLookingAtTerrain(location);
                    }
                }

                if (message.args.length >= 7) {
                    Location crossHairLoc = Location.fromString(message.args[6]);
                    if (crossHairLoc != null) {
                        if (netConnection.getPlayer().location.dist(crossHairLoc) <= 1000) {
                            netConnection.setCrossHairLocation(crossHairLoc);
                        }
                    }
                }

                if (message.args.length >= 8) {
                    Location rotVect = Location.fromString(message.args[7]);
                    if (rotVect != null) {
                        netConnection.setRotVector(rotVect.toVector());
                    }
                }

                netConnection.calcSpeed(l);

//                Location locationWithWorldHeight = DedicatedServer.instance.getWorld().getRawHeightmap().getHeightLocationFromLocation(netConnection.getPlayer().location.cpy());

//                /*
//                 * Draw heightmap data
//                 * */
//                netConnection.drawDebugBox(locationWithWorldHeight, new Location(5, 5, 5), 1);

//                /*
//                 * Heightmap Data
//                 * */
//                float h = DedicatedServer.instance.getWorld().getHeightmap().getHeightFromLocation(netConnection.getPlayer().location);
////                System.out.println(h);
//
//                DedicatedServer.get(RayService.class).makeRequest(netConnection.getPlayer().location, 1, request -> {
//                    System.out.println("C: " + h + ", R: " + request.getReturnedHeight() + ", D: " + (request.getReturnedHeight() - h));
//                });

//                netConnection.broadcastState();
            }
        }
    }
}
