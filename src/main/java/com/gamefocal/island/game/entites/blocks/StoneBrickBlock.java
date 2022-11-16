package com.gamefocal.island.game.entites.blocks;

import com.gamefocal.island.game.entites.Block;

public class StoneBrickBlock extends Block<StoneBrickBlock> {

    public StoneBrickBlock() {
        this.type = "block";
        this.setMeta("material", "stonebrick");
    }
}
