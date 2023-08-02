package com.gamefocal.rivenworld.game.items.generics;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.InteractableEntity;
import com.gamefocal.rivenworld.game.entites.crops.CropEntity;
import com.gamefocal.rivenworld.game.farming.CropType;
import com.gamefocal.rivenworld.game.interactable.InteractAction;
import com.gamefocal.rivenworld.game.interactable.Intractable;
import com.gamefocal.rivenworld.game.inventory.InventoryStack;
import com.gamefocal.rivenworld.game.inventory.enums.EquipmentSlot;
import com.gamefocal.rivenworld.game.ray.HitResult;
import com.gamefocal.rivenworld.game.ray.hit.EntityHitResult;

public abstract class SeedInventoryItem extends PlaceableInventoryItem implements UsableInventoryItem {

    protected CropType plantType = null;

    public SeedInventoryItem() {
        this.isEquipable = true;
        this.equipTo = EquipmentSlot.PRIMARY;
    }

    @Override
    public String inHandTip(HiveNetConnection connection, HitResult hitResult) {
        if (this.plantType == null) {
            return null;
        }

        if (EntityHitResult.class.isAssignableFrom(hitResult.getClass())) {
            // Looking at a entity
            GameEntity e = ((EntityHitResult) hitResult).get();
            if (CropEntity.class.isAssignableFrom(e.getClass())) {

                CropEntity cropEntity = (CropEntity) e;

                if (cropEntity.getCropType() == null && this.plantType != null) {
                    return "[e] Plant seeds";
                }
            }
        }

        return "Look at a plant plot to plant this";
    }

    @Override
    public boolean onUse(HiveNetConnection connection, HitResult hitResult, InteractAction action, InventoryStack inHand) {
        if (action == InteractAction.USE) {
            if (EntityHitResult.class.isAssignableFrom(hitResult.getClass())) {
                // Looking at a entity

                GameEntity e = ((EntityHitResult) hitResult).get();
                if (CropEntity.class.isAssignableFrom(e.getClass())) {

                    CropEntity cropEntity = (CropEntity) e;

                    if (cropEntity.getCropType() == null && this.plantType != null) {
                        // Can plant here :)
                        cropEntity.setPlantedCropType(this.plantType);
                    }

                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void onInteract(Intractable intractable, HiveNetConnection connection, InteractAction action) {
        if (action == InteractAction.USE) {
            // Check to see if they're looking at a planting plot

        }
    }
}
