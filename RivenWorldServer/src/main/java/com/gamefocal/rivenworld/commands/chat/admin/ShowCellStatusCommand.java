package com.gamefocal.rivenworld.commands.chat.admin;

import com.badlogic.gdx.graphics.Color;
import com.gamefocal.rivenworld.DedicatedServer;
import com.gamefocal.rivenworld.entites.net.*;
import com.gamefocal.rivenworld.game.ai.path.WorldCell;
import com.gamefocal.rivenworld.game.util.Location;

import java.util.ArrayList;

@Command(name = "celldebug", sources = "chat")
public class ShowCellStatusCommand extends HiveCommand {
    @Override
    public void onCommand(HiveNetMessage message, CommandSource source, HiveNetConnection netConnection) throws Exception {
        if (netConnection.isAdmin()) {

            WorldCell c = DedicatedServer.instance.getWorld().getGrid().getCellFromGameLocation(netConnection.getPlayer().location);

            ArrayList<WorldCell> cells = c.getRadiusCells(Integer.parseInt(message.args[0]));

            for (WorldCell cc : cells) {
                if (cc.canTravelFromCell(null, null)) {
                    netConnection.drawDebugBox(Color.GREEN, cc.getCenterInGameSpace(true), new Location(50, 50, 50), 1);
                } else {
                    netConnection.drawDebugBox(Color.RED, cc.getCenterInGameSpace(true), new Location(50, 50, 50), 1);
                }
            }

        }
    }
}
