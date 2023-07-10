package com.gamefocal.rivenworld.commands.chat;

import com.badlogic.gdx.graphics.Color;
import com.gamefocal.rivenworld.DedicatedServer;
import com.gamefocal.rivenworld.entites.net.*;
import com.gamefocal.rivenworld.game.util.Location;
import com.gamefocal.rivenworld.game.world.WorldChunk;

@Command(sources = "chat", name = "lod")
public class RenderDistanceCommand extends HiveCommand {
    @Override
    public void onCommand(HiveNetMessage message, CommandSource source, HiveNetConnection netConnection) throws Exception {
        if (message.args.length > 0) {
            float dist = Float.parseFloat(message.args[0]);
            netConnection.setRenderDistance((25 * 100) * dist);
            netConnection.sendChatMessage("Render Distance set to " + dist + " chunks");
        }

        for (WorldChunk[] cc : DedicatedServer.instance.getWorld().getChunks()) {
            for (WorldChunk c : cc) {
                float lod = netConnection.getLOD(c.getCenter());

                Location centerInGameSpace = DedicatedServer.instance.getWorld().getRawHeightmap().getHeightLocationFromLocation(c.getCenter());

                if (lod <= 0) {
                    // LOD0
                    netConnection.drawDebugBox(Color.GREEN, centerInGameSpace, new Location(100 * 24, 100 * 24, 5000), 6);
                } else if (lod <= 1) {
                    netConnection.drawDebugBox(Color.YELLOW, centerInGameSpace, new Location(100 * 24, 100 * 24, 5000), 6);
                } else if (lod <= 2) {
                    netConnection.drawDebugBox(Color.BLUE, centerInGameSpace, new Location(100 * 24, 100 * 24, 5000), 6);
                } else if (lod <= 3) {
                    netConnection.drawDebugBox(Color.RED, centerInGameSpace, new Location(100 * 24, 100 * 24, 5000), 6);
                }
            }
        }
    }
}
