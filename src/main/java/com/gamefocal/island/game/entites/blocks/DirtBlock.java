package com.gamefocal.island.game.entites.blocks;

import com.gamefocal.island.game.entites.Block;

public class DirtBlock extends Block<DirtBlock> {

    public DirtBlock() {
        this.type = "block";
        this.setMeta("material", "dirt");
    }
}
