package com.gamefocal.rivenworld.commands.chat;

import com.badlogic.gdx.graphics.Color;
import com.gamefocal.rivenworld.DedicatedServer;
import com.gamefocal.rivenworld.entites.net.*;
import com.gamefocal.rivenworld.game.WorldChunk;
import com.gamefocal.rivenworld.game.util.Location;
import com.gamefocal.rivenworld.models.GameChunkModel;
import com.gamefocal.rivenworld.service.ClaimService;
import com.gamefocal.rivenworld.service.DataService;

@Command(name = "claimd", sources = "chat")
public class ClaimShowCommand extends HiveCommand {
    @Override
    public void onCommand(HiveNetMessage message, CommandSource source, HiveNetConnection netConnection) throws Exception {
        Location checkLocation = netConnection.getPlayer().location;
        if (checkLocation == null) {
//            System.out.println("no location");
            return;
        }
        WorldChunk inChunk = DedicatedServer.instance.getWorld().getChunk(checkLocation);
        if (inChunk != null) {
            netConnection.showClaimRegion(inChunk.getCenter(), DedicatedServer.instance.getWorld().getChunkSize() * 100, Color.BLUE);

//            System.out.println("Send Claim help command");
        }
    }
}
