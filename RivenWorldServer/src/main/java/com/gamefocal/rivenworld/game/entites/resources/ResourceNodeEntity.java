package com.gamefocal.rivenworld.game.entites.resources;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.InteractableEntity;
import com.gamefocal.rivenworld.game.interactable.InteractAction;
import com.gamefocal.rivenworld.game.inventory.InventoryItem;
import com.gamefocal.rivenworld.game.inventory.InventoryStack;
import com.gamefocal.rivenworld.game.player.Animation;
import com.gamefocal.rivenworld.game.sounds.GameSounds;

import java.util.ArrayList;
import java.util.LinkedList;

public abstract class ResourceNodeEntity<A> extends GameEntity<A> implements InteractableEntity {

    public float health = 100f;

    public float maxHealth = 100f;

    public boolean giveProgressiveDrops = true;

    public GameSounds hitSound = GameSounds.PICKAXE_HIT;

    public Animation hitAnimation = Animation.PICKAXE;

    public long delay = 30;

    public LinkedList<Class<? extends InventoryItem>> allowedTools = new LinkedList<>();

//    public InventoryStack[] drops = new InventoryStack[0];

    public abstract InventoryStack[] drops();

    @Override
    public boolean canInteract(HiveNetConnection netConnection) {
        return true;
    }

    @Override
    public String onFocus(HiveNetConnection connection) {
        return null;
    }

    @Override
    public void onInteract(HiveNetConnection connection, InteractAction action, InventoryStack inHand) {
    }

    public boolean isAllowedTool(Class<? extends InventoryItem> tool) {
        boolean canHarvest = false;
        for (Class<? extends InventoryItem> t : this.getAllowedTools()) {
            if (t.isAssignableFrom(tool)) {
                canHarvest = true;
                break;
            }
        }

        return canHarvest;
    }

    public LinkedList<Class<? extends InventoryItem>> getAllowedTools() {
        return allowedTools;
    }
}
