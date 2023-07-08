package com.gamefocal.rivenworld.game.recipes.blocks.Stone;

import com.gamefocal.rivenworld.game.entites.blocks.Stone.StoneBlock;
import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.items.placables.blocks.Stone.StoneBattlementCornerBlockItem;
import com.gamefocal.rivenworld.game.items.placables.blocks.Stone.StoneBlockItem;
import com.gamefocal.rivenworld.game.items.resources.minerals.raw.Stone;
import com.gamefocal.rivenworld.game.items.resources.minerals.refined.StoneBrick;

public class StoneBattlementCornerBlockRecipe extends CraftingRecipe {
    @Override
    public void config() {
        this.requires(Stone.class, 3);
        this.setProduces(new StoneBattlementCornerBlockItem(), 1);
        this.setProductionTime(35);
    }
}
