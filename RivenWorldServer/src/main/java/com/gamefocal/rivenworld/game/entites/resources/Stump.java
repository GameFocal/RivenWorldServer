package com.gamefocal.rivenworld.game.entites.resources;

import com.badlogic.gdx.graphics.Color;
import com.gamefocal.rivenworld.DedicatedServer;
import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.InteractableEntity;
import com.gamefocal.rivenworld.game.entites.loot.LargeLootChest;
import com.gamefocal.rivenworld.game.entites.loot.LootChest;
import com.gamefocal.rivenworld.game.entites.loot.MediumLootChest;
import com.gamefocal.rivenworld.game.entites.loot.SmallLootChest;
import com.gamefocal.rivenworld.game.interactable.InteractAction;
import com.gamefocal.rivenworld.game.inventory.InventoryStack;
import com.gamefocal.rivenworld.game.items.resources.econ.GoldCoin;
import com.gamefocal.rivenworld.game.items.weapons.Spade;
import com.gamefocal.rivenworld.game.items.weapons.Wood_Spade;
import com.gamefocal.rivenworld.game.player.Animation;
import com.gamefocal.rivenworld.game.sounds.GameSounds;
import com.gamefocal.rivenworld.game.util.RandomUtil;
import com.gamefocal.rivenworld.service.InventoryService;
import com.gamefocal.rivenworld.service.LootService;
import com.gamefocal.rivenworld.service.TaskService;

public class Stump extends GameEntity<Stump> implements InteractableEntity {

    private String attachedFoliageId = null;

    public Stump(String foliageId) {
        this.type = "Stump";
        this.attachedFoliageId = foliageId;
    }

    public String getAttachedFoliageId() {
        return attachedFoliageId;
    }

    public void setAttachedFoliageId(String attachedFoliageId) {
        this.attachedFoliageId = attachedFoliageId;
    }

    @Override
    public void onSpawn() {

    }

    @Override
    public void onDespawn() {

    }

    @Override
    public void onTick() {

    }

    @Override
    public void onInteract(HiveNetConnection connection, InteractAction action, InventoryStack inHand) {
        if (inHand != null) {
            if (Spade.class.isAssignableFrom(inHand.getItem().getClass()) || Wood_Spade.class.isAssignableFrom(inHand.getItem().getClass())) {
//                connection.playAnimation(Animation.Digging);

                connection.playAnimation(Animation.Digging, "DefaultSlot", .75f, 0, -1, 0.25f, 0.25f, true);
                TaskService.schedulePlayerInterruptTask(() -> {
                    /*
                     * Random chance to spawn a bag with coins or chest
                     * */

                    DedicatedServer.instance.getWorld().despawn(this.uuid);

                    int roll = RandomUtil.getRandomNumberBetween(1, 100);
                    LootChest chest = null;
                    if (roll <= 2) {
                        chest = new LargeLootChest();
                    } else if (roll <= 3) {
                        chest = new MediumLootChest();
                    } else if (roll <= 5) {
                        chest = new SmallLootChest();
                    } else if (roll <= 25) {
                        // Bag of coins
                        InventoryStack coins = new InventoryStack(new GoldCoin(), RandomUtil.getRandomNumberBetween(1, 32));
                        DedicatedServer.get(InventoryService.class).dropBagAtLocation(null, DedicatedServer.instance.getWorld().getRawHeightmap().getHeightLocationFromLocation(this.location.cpy()), coins);
                        DedicatedServer.instance.getWorld().playSoundAtLocation(GameSounds.LEVEL_UP, this.location, 1500, 1, 1);
                    }

                    if (chest != null) {
                        chest.setPlaySongOnClose(false);
                        DedicatedServer.get(LootService.class).populateLootchest(chest);
                        DedicatedServer.instance.getWorld().spawn(chest, DedicatedServer.instance.getWorld().getRawHeightmap().getHeightLocationFromLocation(this.location.cpy()));
                        DedicatedServer.instance.getWorld().playSoundAtLocation(GameSounds.LEVEL_UP, this.location, 1500, 1, 1);
                    }
                }, 5L, "Digging Up Stump", Color.GREEN, connection);
            }
        }
    }

    @Override
    public boolean canInteract(HiveNetConnection netConnection) {
        return true;
    }

    @Override
    public String onFocus(HiveNetConnection connection) {
        InventoryStack inHand = connection.getPlayer().equipmentSlots.getWeapon();
        if (inHand != null) {
            if (Spade.class.isAssignableFrom(inHand.getItem().getClass()) || Wood_Spade.class.isAssignableFrom(inHand.getItem().getClass())) {
                return "[e] Remove Stump";
            }
        }

        return "You need a shovel to dig up stumps";
    }
}
