package com.gamefocal.island.game.entites.blocks;

import com.gamefocal.island.game.entites.Block;

public class WoodBlock extends Block<WoodBlock> {

    public WoodBlock() {
        this.type = "block";
        this.setMeta("material", "wood");
    }
}
