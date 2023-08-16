package com.gamefocal.rivenworld.commands.chat.dev;

import com.badlogic.gdx.graphics.Color;
import com.gamefocal.rivenworld.DedicatedServer;
import com.gamefocal.rivenworld.entites.net.*;
import com.gamefocal.rivenworld.game.ai.path.AStarPathfinding;
import com.gamefocal.rivenworld.game.ai.path.WorldCell;
import com.gamefocal.rivenworld.game.util.Location;

import java.util.LinkedList;

@Command(name = "wt/", sources = "chat")
public class EdgeScanCommand extends HiveCommand {
    @Override
    public void onCommand(HiveNetMessage message, CommandSource source, HiveNetConnection netConnection) throws Exception {
        if (netConnection.isAdmin()) {

            WorldCell cell = DedicatedServer.instance.getWorld().getGrid().getCellFromGameLocation(netConnection.getPlayer().location);

            LinkedList<WorldCell> cells = AStarPathfinding.getEdgesInArea(cell, 20);

            if (cells.size() > 0) {
                for (WorldCell c : cells) {
                    netConnection.drawDebugBox(Color.GREEN, c.getCenterInGameSpace(true), new Location(50, 50, 200), 2);
                }
            }

        }
    }
}
