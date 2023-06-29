package com.gamefocal.rivenworld.commands.net.player;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector3;
import com.gamefocal.rivenworld.DedicatedServer;
import com.gamefocal.rivenworld.entites.net.*;
import com.gamefocal.rivenworld.events.player.PlayerMoveEvent;
import com.gamefocal.rivenworld.game.ai.path.WorldCell;
import com.gamefocal.rivenworld.game.player.PlayerBlendState;
import com.gamefocal.rivenworld.game.util.Location;
import com.gamefocal.rivenworld.game.util.LocationUtil;
import com.gamefocal.rivenworld.game.util.WorldDirection;
import com.gamefocal.rivenworld.game.world.WorldMetaData;
import com.gamefocal.rivenworld.models.PlayerModel;

import java.util.Map;

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

//            if (netConnection.isMovementDisabled()) {
//                return;
//            }

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

                if (message.args.length >= 9) {
                    Location cameraLoc = Location.fromString(message.args[8]);
                    if (cameraLoc != null) {
                        netConnection.setCameraLocation(cameraLoc);
                    }
                }

                netConnection.calcSpeed(l);
//
//                WorldCell cell = DedicatedServer.instance.getWorld().getGrid().getCellFromGameLocation(netConnection.getPlayer().location.cpy());
//
//                WorldDirection direction = LocationUtil.getFacingDirection(netConnection.getForwardVector());
//
//                WorldCell newCell = cell.getNeighborFromFwdVector(netConnection.getForwardVector().cpy());
//
//                netConnection.drawDebugBox(Color.GREEN, newCell.getCenterInGameSpace(true), new Location(50, 50, 50), 1);

//                System.out.println("CELL: " + cell.getX() + ", " + cell.getY() + ", " + h + ", D: " + diff);

//                netConnection.drawDebugBox(Color.GREEN, ShapeUtil.makeBoundBox(cell.getGameLocation().setZ(h).toVector(), 10, 10), 2);

//                System.out.println("CELL: " + cell.getX() + ", " + cell.getY());
//
//                System.out.println("Center: " + cell.getGameLocation());
//
//                System.out.println("World LOC: " + cell.getCenterInGameSpace(true));

//                netConnection.drawDebugBox(Color.RED,cell.getCenterInGameSpace(true),new Location(5,5,5),2);

//                Location locationWithWorldHeight = DedicatedServer.instance.getWorld().getRawHeightmap().getHeightLocationFromLocation(netConnection.getPlayer().location.cpy());
////
////                /*
////                 * Draw heightmap data
////                 * */
//                WorldMetaData metaData = DedicatedServer.instance.getWorld().getRawHeightmap().getMetaDataFromXY(netConnection.getPlayer().location);
//
//                if (metaData.getForest() == 0x01) {
//                    netConnection.drawDebugBox(Color.GREEN, locationWithWorldHeight, new Location(5, 5, 5), 1);
//                } else {
//                    netConnection.drawDebugBox(Color.RED, locationWithWorldHeight, new Location(5, 5, 5), 1);
//                }

//                // Debug draw compass
//                Vector3 current = netConnection.getPlayer().location.toVector();
//                Vector3 fwd = current.cpy().mulAdd(netConnection.getForwardVector(), 100);
//
//                System.out.println("FWD: " + netConnection.getForwardVector().toString());
//
//                WorldCell currentCell = DedicatedServer.instance.getWorld().getGrid().getCellFromGameLocation(Location.fromVector(current));
//                WorldCell facingCell = DedicatedServer.instance.getWorld().getGrid().getCellFromGameLocation(Location.fromVector(fwd));
//
//                netConnection.drawDebugBox(Color.GREEN, currentCell.getNorth().getCenterInGameSpace(true, 0), new Location(50, 50, 50), 1);
//                netConnection.drawDebugBox(Color.YELLOW, currentCell.getEast().getCenterInGameSpace(true, 0), new Location(50, 50, 50), 1);
//                netConnection.drawDebugBox(Color.BLUE, currentCell.getSouth().getCenterInGameSpace(true, 0), new Location(50, 50, 50), 1);
//                netConnection.drawDebugBox(Color.RED, currentCell.getWest().getCenterInGameSpace(true, 0), new Location(50, 50, 50), 1);
//
//                for (Map.Entry<String, WorldCell> e : currentCell.getNeighborsWithDirections(false).entrySet()) {
//                    if (e.getValue().equals(facingCell)) {
//                        System.out.println("Facing: " + e.getKey());
//                    }
//                }

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
