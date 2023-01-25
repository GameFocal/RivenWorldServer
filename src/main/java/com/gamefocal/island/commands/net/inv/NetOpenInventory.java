package com.gamefocal.island.commands.net.inv;

import com.gamefocal.island.DedicatedServer;
import com.gamefocal.island.entites.net.*;
import com.gamefocal.island.events.inv.InventoryOpenEvent;
import com.gamefocal.island.game.entites.storage.StorageEntity;
import com.gamefocal.island.game.items.placables.blocks.*;
import com.gamefocal.island.game.items.placables.blocks.Stone.StoneBlockItem;
import com.gamefocal.island.game.items.placables.blocks.Wood.*;
import com.gamefocal.island.game.items.placables.items.*;
import com.gamefocal.island.game.items.placables.items.TorchPlaceableItem;
import com.gamefocal.island.game.items.weapons.BuildHammer;
import com.gamefocal.island.game.items.weapons.hatchets.StoneHatchet;
import com.gamefocal.island.game.items.ammo.WoodenArrow;
import com.gamefocal.island.game.items.weapons.Bow;
import com.gamefocal.island.models.GameEntityModel;

import java.util.UUID;

@Command(name = "invopen", sources = "tcp")
public class NetOpenInventory extends HiveCommand {
    @Override
    public void onCommand(HiveNetMessage message, CommandSource source, HiveNetConnection netConnection) throws Exception {
        // Open the inventory.
        String inv = message.args[0];

        if (inv.equalsIgnoreCase("self")) {
            // The player inv

            System.out.println("[INV]: SELF, OPEN");

//            // DEBUG
//            if (!netConnection.getPlayer().inventory.hasOfType(BuildHammer.class)) {
//                netConnection.getPlayer().inventory.add(new StoneHatchet());
////                netConnection.getPlayer().inventory.add(new TestCube(), 32);
//                netConnection.getPlayer().inventory.add(new StoneBlockItem(), 32);
//                netConnection.getPlayer().inventory.add(new DirtBlockItem(), 32);
//                netConnection.getPlayer().inventory.add(new ClayBlockItem(), 32);
//                netConnection.getPlayer().inventory.add(new SandBlockItem(), 32);
//                netConnection.getPlayer().inventory.add(new CopperBlockItem(), 32);
//                netConnection.getPlayer().inventory.add(new GlassBlockItem(), 32);
//                netConnection.getPlayer().inventory.add(new GoldBlockItem(), 32);
//                netConnection.getPlayer().inventory.add(new IronBlockItem(), 32);
//                netConnection.getPlayer().inventory.add(new ThatchBlockItem(), 32);
//                netConnection.getPlayer().inventory.add(new WoodBlockItem(), 32);
//                netConnection.getPlayer().inventory.add(new PlasterBlockItem(), 32);
//                netConnection.getPlayer().inventory.add(new StoneBrickBlockItem(), 32);
//                netConnection.getPlayer().inventory.add(new WoodStairBlockItem(), 32);
//                netConnection.getPlayer().inventory.add(new WoodCornerBlockItem(), 32);
//                netConnection.getPlayer().inventory.add(new WoodBattlementBlockItem(), 32);
//                netConnection.getPlayer().inventory.add(new WoodBattlementCornerBlockItem(), 32);
//                netConnection.getPlayer().inventory.add(new WoodRampBlockItem(), 32);
//                netConnection.getPlayer().inventory.add(new WoodInvertRampBlockItem(), 32);
//                netConnection.getPlayer().inventory.add(new Bow(), 1);
//                netConnection.getPlayer().inventory.add(new WoodenArrow(), 64);
//                netConnection.getPlayer().inventory.add(new DoorPlaceableItem(), 5);
//                netConnection.getPlayer().inventory.add(new CampFirePlaceableItem(), 5);
//                netConnection.getPlayer().inventory.add(new TablePlaceableItem(), 5);
//                netConnection.getPlayer().inventory.add(new TorchPlaceableItem(),5);
//                netConnection.getPlayer().inventory.add(new ChestPlaceableItem(), 5);
//                netConnection.getPlayer().inventory.add(new RugPlaceableItem(), 5);
//                netConnection.getPlayer().inventory.add(new ChandelierPlaceableItem(), 5);
//                netConnection.getPlayer().inventory.add(new BuildHammer(), 1);
//            }

            InventoryOpenEvent event = new InventoryOpenEvent(netConnection.getPlayer().inventory, netConnection).call();

            if (event.isCanceled()) {
                return;
            }

            netConnection.openInventory(netConnection.getPlayer().inventory, true);
        } else {
            if (DedicatedServer.instance.getWorld().entites.containsKey(UUID.fromString(inv))) {

                // Is a entity
                GameEntityModel e = DedicatedServer.instance.getWorld().getEntityFromId(UUID.fromString(inv));
                if (StorageEntity.class.isAssignableFrom(e.entityData.getClass())) {
                    /*
                     * Is a storage entity
                     * */
                    StorageEntity se = (StorageEntity) e.entityData;

                    InventoryOpenEvent event = new InventoryOpenEvent(se.getInventory(), netConnection).call();

                    if (event.isCanceled()) {
                        return;
                    }

                    if (!se.getInventory().hasOwner()) {
                        netConnection.openDualInventory(se.getInventory(), true);
                    }
                }
            }
        }

    }
}
