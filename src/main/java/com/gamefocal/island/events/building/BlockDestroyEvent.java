package com.gamefocal.island.events.building;

import com.gamefocal.island.entites.events.Event;
import com.gamefocal.island.game.entites.Block;

public class BlockDestroyEvent extends Event<BlockDestroyEvent> {

    private Block blockEntity;

    public BlockDestroyEvent(Block blockEntity) {
        this.blockEntity = blockEntity;
    }

    public Block getBlockEntity() {
        return blockEntity;
    }

    public void setBlockEntity(Block blockEntity) {
        this.blockEntity = blockEntity;
    }
}
