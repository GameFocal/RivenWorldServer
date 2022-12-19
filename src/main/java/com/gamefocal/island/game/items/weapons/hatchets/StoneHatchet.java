package com.gamefocal.island.game.items.weapons.hatchets;

import com.gamefocal.island.entites.net.HiveNetConnection;
import com.gamefocal.island.game.foliage.FoliageIntractable;
import com.gamefocal.island.game.interactable.EquipmentInteract;
import com.gamefocal.island.game.interactable.InteractAction;
import com.gamefocal.island.game.interactable.Intractable;
import com.gamefocal.island.game.items.generics.ToolInventoryItem;
import com.gamefocal.island.game.items.weapons.Hatchet;
import com.gamefocal.island.models.GameFoliageModel;

public class StoneHatchet extends Hatchet {

    public StoneHatchet() {
        this.isEquipable = true;
    }

    @Override
    public String slug() {
        return "Stone_Hatchet";
    }

    @Override
    public float hit() {
        return 5;
    }
}
