package com.gamefocal.rivenworld.game.entites.resources;

import com.gamefocal.rivenworld.DedicatedServer;
import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.InteractableEntity;
import com.gamefocal.rivenworld.game.interactable.InteractAction;
import com.gamefocal.rivenworld.game.inventory.InventoryStack;
import com.gamefocal.rivenworld.game.items.weapons.Spade;
import com.gamefocal.rivenworld.game.items.weapons.Wood_Spade;
import com.gamefocal.rivenworld.game.player.Animation;

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
                connection.playAnimation(Animation.Digging, "DefaultSlot", 1, 0, -1, 0.25f, 0.25f, true);
                DedicatedServer.instance.getWorld().despawn(this.uuid);
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
