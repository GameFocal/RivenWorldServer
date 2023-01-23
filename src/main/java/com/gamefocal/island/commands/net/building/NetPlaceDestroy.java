package com.gamefocal.island.commands.net.building;

import com.gamefocal.island.DedicatedServer;
import com.gamefocal.island.entites.net.*;
import com.gamefocal.island.events.building.BlockPlaceEvent;
import com.gamefocal.island.game.entites.blocks.TestBlock;
import com.gamefocal.island.game.util.Location;
import com.gamefocal.island.models.GameEntityModel;
import com.gamefocal.island.service.DataService;

import java.util.List;

@Command(name = "blockd", sources = "tcp")
public class NetPlaceDestroy extends HiveCommand {
    @Override
    public void onCommand(HiveNetMessage message, CommandSource source, HiveNetConnection netConnection) throws Exception {
//        System.out.println(message.toString());

        Location destroyLoc = Location.fromString(message.args[0]);

        List<GameEntityModel> model = DataService.gameEntities.queryForEq("location",destroyLoc);

        if(model.size() > 0) {
            System.out.println("Found something (" + model.size() + ")");
        } else {
            System.out.println("Not found.");
        }

    }
}
