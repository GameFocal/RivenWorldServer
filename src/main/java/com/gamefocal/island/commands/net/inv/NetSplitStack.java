package com.gamefocal.island.commands.net.inv;

import com.gamefocal.island.DedicatedServer;
import com.gamefocal.island.entites.net.*;
import com.gamefocal.island.game.inventory.Inventory;
import com.gamefocal.island.game.inventory.InventoryItem;
import com.gamefocal.island.game.inventory.InventoryStack;
import com.gamefocal.island.game.tasks.seqence.ExecSequenceAction;
import com.gamefocal.island.game.tasks.seqence.WaitSequenceAction;
import com.gamefocal.island.service.InventoryService;
import com.gamefocal.island.service.TaskService;

import java.util.UUID;

@Command(name = "invspl", sources = "tcp")
public class NetSplitStack extends HiveCommand {
    @Override
    public void onCommand(HiveNetMessage message, CommandSource source, HiveNetConnection netConnection) throws Exception {

        System.out.println(message);

        String invId = message.args[0];
        String splitAction = message.args[1];
        String objSlug = message.args[2];
        int customAmt = Integer.parseInt(message.args[3]);
        int fromSlot = Integer.parseInt(message.args[4]);

    }
}
