package com.gamefocal.rivenworld.commands.chat.admin;

import com.badlogic.gdx.graphics.Color;
import com.gamefocal.rivenworld.DedicatedServer;
import com.gamefocal.rivenworld.entites.net.*;
import com.gamefocal.rivenworld.game.ai.path.WorldCell;
import com.gamefocal.rivenworld.game.ai.path.WorldGrid;
import com.gamefocal.rivenworld.game.util.Location;
import com.gamefocal.rivenworld.game.world.World;

@Command(name = "grid", sources = "chat")
public class GridCommand extends HiveCommand {
    @Override
    public void onCommand(HiveNetMessage message, CommandSource source, HiveNetConnection netConnection) throws Exception {
        WorldGrid grid = DedicatedServer.instance.getWorld().getGrid();
        WorldCell cell = grid.getCellFromGameLocation(netConnection.getLookingAtTerrain());
        Location center = cell.getCenterInGameSpace(true);
        netConnection.sendChatMessage(ChatColor.BLUE + "Current Location: " + netConnection.getPlayer().location);
        System.out.println("[Loc-cmd]: " + netConnection.getPlayer().location);
        netConnection.drawDebugBox(Color.BLUE, center, new Location(50, 50, 50), 3);
    }
}
