package com.gamefocal.rivenworld.commands.chat;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.gamefocal.rivenworld.DedicatedServer;
import com.gamefocal.rivenworld.entites.net.*;
import com.gamefocal.rivenworld.game.util.Location;
import com.gamefocal.rivenworld.service.AiService;

@Command(name = "ai", sources = "chat")
public class AiBoundCommand extends HiveCommand {

    public static Location[] points = new Location[2];

    @Override
    public void onCommand(HiveNetMessage message, CommandSource source, HiveNetConnection netConnection) throws Exception {
        if (netConnection.isAdmin()) {

            String cmd = message.args[0];

            if (cmd.equalsIgnoreCase("sel")) {
                if (points[0] == null) {
                    points[0] = netConnection.getPlayer().location;
                    netConnection.sendChatMessage("POS1 Saved");
                } else if (points[1] == null) {
                    points[1] = netConnection.getPlayer().location;
                    netConnection.sendChatMessage("POS2 Saved, Box Saved");
                    netConnection.drawDebugBox(Color.PINK, DedicatedServer.get(AiService.class).addAiNoEnterZone(points[0], points[1]), 2);
                    points = new Location[2];
                }
            } else if (cmd.equalsIgnoreCase("pre")) {
                // Preview the boxes
                for (BoundingBox box : DedicatedServer.get(AiService.class).noEnterZones) {
                    netConnection.drawDebugBox(Color.PINK, box, 4);
                }
            } else if (cmd.equalsIgnoreCase("save")) {
                // TODO: Dump to a file
            }
        }
    }
}
