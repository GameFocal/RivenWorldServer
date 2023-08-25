package com.gamefocal.rivenworld.commands.cli;

import com.gamefocal.rivenworld.DedicatedServer;
import com.gamefocal.rivenworld.entites.net.*;
import com.gamefocal.rivenworld.game.inventory.InventoryStack;
import com.gamefocal.rivenworld.game.inventory.equipment.EquipmentSlots;
import com.gamefocal.rivenworld.game.items.clothes.chest.cloth.SimpleClothShirt;
import com.gamefocal.rivenworld.game.items.clothes.feet.leather.SimpleLeatherShoes;
import com.gamefocal.rivenworld.game.items.clothes.legs.cloth.SimpleClothLegs;
import com.gamefocal.rivenworld.game.items.food.consumable.Apple;
import com.gamefocal.rivenworld.game.items.placables.LandClaimItem;
import com.gamefocal.rivenworld.game.items.weapons.Torch;
import com.gamefocal.rivenworld.game.items.weapons.hatchets.StoneHatchet;
import com.gamefocal.rivenworld.game.util.Location;
import com.gamefocal.rivenworld.service.RespawnService;

@Command(name = "reset",sources = "cli,chat")
public class ResetPlayerData extends HiveCommand {
    @Override
    public void onCommand(HiveNetMessage message, CommandSource source, HiveNetConnection netConnection) throws Exception {

        if(source == CommandSource.CHAT) {
            if(!netConnection.isAdmin()) {
                return;
            }
        }

        if(message.args.length == 1) {
            String playerName = message.args[0];

            HiveNetConnection connection = DedicatedServer.getPlayerFromName(playerName);
            if(connection != null) {
                /*
                 * Starting items
                 * */
                connection.getPlayer().inventory.clearInv();

                connection.getPlayer().inventory.add(new StoneHatchet());
                connection.getPlayer().inventory.add(new Torch());
                connection.getPlayer().inventory.add(new Apple(), 4);
                connection.getPlayer().inventory.add(new LandClaimItem(), 1);

                connection.getPlayer().equipmentSlots = new EquipmentSlots();
                // Equipment Default
                connection.getPlayer().equipmentSlots.chest = new InventoryStack(new SimpleClothShirt(), 1);
                connection.getPlayer().equipmentSlots.legs = new InventoryStack(new SimpleClothLegs(), 1);
                connection.getPlayer().equipmentSlots.feet = new InventoryStack(new SimpleLeatherShoes(), 1);

                connection.getPlayer().playerStats.health = 100;
                connection.getPlayer().playerStats.energy = 100;
                connection.getPlayer().playerStats.hunger = 100;
                connection.getPlayer().playerStats.thirst = 100;

                connection.sendSyncPackage(true);
                connection.syncEquipmentSlots();
                connection.updatePlayerInventory();
                connection.sendAttributes();

                Location location = DedicatedServer.get(RespawnService.class).randomSpawnLocation();

                connection.tpToLocation(location);
                connection.sendChatMessage("Your player has been reset by an admin");
            }
        }
    }
}
