package com.gamefocal.island.game.entites.blocks;

import com.gamefocal.island.game.entites.Block;

public class SandBlock extends Block<SandBlock> {

    public SandBlock() {
        this.type = "block";
        this.setMeta("material", "sand");
    }
}
