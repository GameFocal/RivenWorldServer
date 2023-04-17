package com.gamefocal.rivenworld.game.entites.generics;

import com.gamefocal.rivenworld.DedicatedServer;
import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.InteractableEntity;
import com.gamefocal.rivenworld.game.entites.placable.PlaceableEntity;
import com.gamefocal.rivenworld.game.interactable.InteractAction;
import com.gamefocal.rivenworld.game.inventory.InventoryItem;
import com.gamefocal.rivenworld.game.inventory.InventoryStack;
import com.gamefocal.rivenworld.game.sounds.GameSounds;
import com.gamefocal.rivenworld.game.ui.UIIcon;
import com.gamefocal.rivenworld.game.ui.radialmenu.RadialMenuOption;

public abstract class DoorEntity<T> extends PlaceableEntity<T> implements InteractableEntity {

    protected InventoryItem lock;

    protected boolean isOpen = false;

    protected boolean isLocked = false;

    @Override
    public void onSpawn() {

    }

    @Override
    public void onSync() {
        this.setMeta("open", (this.isOpen ? "t" : "f"));
    }

    @Override
    public void onDespawn() {

    }

    @Override
    public void onTick() {

    }

    public InventoryItem getLock() {
        return lock;
    }

    public void setLock(InventoryItem lock) {
        this.lock = lock;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void setOpen(boolean open) {
        isOpen = open;
    }

    @Override
    public String onFocus(HiveNetConnection connection) {
        return "[e] Use Door";
    }

    @Override
    public void onInteract(HiveNetConnection connection, InteractAction action, InventoryStack inHand) {
        super.onInteract(connection, action, inHand);
        if (action == InteractAction.USE) {
            // Open the door
            // TODO: Do a permission check based on lock here.

            if (!this.isOpen) {
                // Close
                connection.playSoundAtPlayer(GameSounds.DOOR_OPEN, 1.0f, 1.0f);
            } else {
                // Open
                connection.playSoundAtPlayer(GameSounds.DOOR_OPEN, 1.0f, 1.0f);
            }

            this.isOpen = !this.isOpen;

            DedicatedServer.instance.getWorld().updateEntity(this);
        }
            //Remove q
//        if (action == InteractAction.RADIAL_MENU) {
//            connection.openRadialMenu(action1 -> {
//                        if (action1.equalsIgnoreCase("lock")) {
//
//                            this.isLocked = !this.isLocked;
//
//                        }
//                        connection.closeRadialMenu();
//                    },
//                    new RadialMenuOption((this.isLocked ? "Unlock" : "Lock"), "lock", (this.isLocked ? UIIcon.UNLOCK : UIIcon.LOCK)),
//                    new RadialMenuOption("Pickup", "pickup", UIIcon.PICKUP)
//            );
//        }
    }
}
