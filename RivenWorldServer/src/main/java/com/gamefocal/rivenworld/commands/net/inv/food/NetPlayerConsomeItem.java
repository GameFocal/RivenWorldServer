package com.gamefocal.rivenworld.commands.net.inv.food;

import com.gamefocal.rivenworld.DedicatedServer;
import com.gamefocal.rivenworld.entites.net.*;
import com.gamefocal.rivenworld.game.inventory.Inventory;
import com.gamefocal.rivenworld.game.inventory.InventoryStack;
import com.gamefocal.rivenworld.game.items.generics.ConsumableInventoryItem;
import com.gamefocal.rivenworld.game.player.Animation;
import com.gamefocal.rivenworld.game.sounds.GameSounds;
import com.gamefocal.rivenworld.service.InventoryService;

import java.util.UUID;

@Command(name = "npcomsume", sources = "tcp")
public class NetPlayerConsomeItem extends HiveCommand {
    @Override
    public void onCommand(HiveNetMessage message, CommandSource source, HiveNetConnection netConnection) throws Exception {

        System.out.println(message.toString());

        Inventory inventory = DedicatedServer.get(InventoryService.class).getInvFromId(UUID.fromString(message.args[0]));

        if (inventory != null) {

            InventoryStack stack = inventory.get(Integer.parseInt(message.args[1]));
            if (stack != null) {

                if (ConsumableInventoryItem.class.isAssignableFrom(stack.getItem().getClass())) {
                    // Can eat

                    if (stack.getAmount() > 0) {
                        stack.remove(1);
                    }

                    // TODO: Play Eating sound
                    netConnection.playSoundAtPlayer(GameSounds.EAT, .5f, 1f);

                    // TODO: Play eating animation
                    netConnection.playAnimation(Animation.Eat);

                    ConsumableInventoryItem consumableInventoryItem = (ConsumableInventoryItem) stack.getItem();
                    float foodVal = consumableInventoryItem.onConsume(netConnection);

//                    float f = foodVal / 100;

                    netConnection.getPlayer().playerStats.hunger += foodVal;
                    netConnection.getPlayer().playerStats.thirst += (foodVal / 2);
                    netConnection.getPlayer().playerStats.health += (foodVal / 4);
                    netConnection.sendAttributes();

                    inventory.update();
                    inventory.getLinkedUI().update(netConnection);
                }

                // Has something there, remove one and eat it.

            }

        } else {
            System.err.println("Could not find inventory for food");
        }

    }
}
