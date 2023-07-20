package com.gamefocal.rivenworld.game.entites.crops;

import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.entites.generics.TickEntity;
import com.gamefocal.rivenworld.game.farming.CropStage;

import java.util.LinkedList;

public class CropEntity<T> extends GameEntity<T> implements TickEntity {

    private LinkedList<CropStage> stages = new LinkedList<>();

    private float water = 0;
    private float fertilizer = 0;

    public CropEntity() {
        this.type = "tilled-soil";
    }

    public void addStage(long timeInSeconds, Class<? extends GameEntity> child) {
        this.stages.add(new CropStage(timeInSeconds, child));
    }

    public void addStage(CropStage stage) {
        this.stages.add(stage);
    }

    @Override
    public void onSpawn() {

    }

    @Override
    public void onDespawn() {

    }

    @Override
    public void onTick() {
        // TODO: Crop tick here for growth
    }
}
