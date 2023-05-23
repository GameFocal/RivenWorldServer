package com.gamefocal.rivenworld.game.items.placables.items.decoration;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.entites.placable.AttackDummyEntity;
import com.gamefocal.rivenworld.game.entites.placable.Target_Entity;
import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.inventory.InventoryCraftingInterface;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.items.generics.PlaceableInventoryItem;
import com.gamefocal.rivenworld.game.recipes.placables.decoration.Dummy_Recipe;
import com.gamefocal.rivenworld.game.recipes.placables.decoration.Target_Recipe;

public class Dummy_Item extends PlaceableInventoryItem<Dummy_Item> implements InventoryCraftingInterface {

    public Dummy_Item() {
        this.name = "Attack Dummy";
        this.desc = "A bag on a stick filled with straw to practice combat with";
        this.mesh = InventoryDataRow.dummy;
        this.icon = InventoryDataRow.dummy;
        this.placable.IsPlacableEntity = true;
        this.placable.RequireTerrain = true;
        this.placable.DetectCollision = true;
    }

    @Override
    public GameEntity spawnItem() {
        return new AttackDummyEntity();
    }

    @Override
    public CraftingRecipe canCraft(HiveNetConnection connection) { return new Dummy_Recipe();}
}
