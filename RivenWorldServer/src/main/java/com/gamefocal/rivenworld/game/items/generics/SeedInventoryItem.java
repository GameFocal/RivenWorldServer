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

public abstract class SeedInventoryItem extends PlaceableInventoryItem implements PlantableInventoryItem {

    protected CropType plantType = null;

    public SeedInventoryItem() {
        this.isEquipable = true;
        this.equipTo = EquipmentSlot.PRIMARY;
    }

    public CropType getPlantType() {
        return plantType;
    }

    @Override
    public CropType crop() {
        return this.plantType;
    }

    @Override
    public void onInteract(Intractable intractable, HiveNetConnection connection, InteractAction action) {
    }
}
