package com.gamefocal.island.game.entites.blocks;

import com.gamefocal.island.game.entites.Block;

public class ClayBlock extends Block<ClayBlock> {

    public ClayBlock() {
        this.type = "block";
        this.setMeta("material", "clay");
    }
}
