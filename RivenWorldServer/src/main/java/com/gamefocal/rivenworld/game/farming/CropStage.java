package com.gamefocal.rivenworld.game.farming;

import com.gamefocal.rivenworld.game.GameEntity;

public class CropStage {

    private long timeInSeconds = 0;
    private Class<? extends GameEntity> stageChild = null;

    public CropStage(long timeInSeconds, Class<? extends GameEntity> stageChild) {
        this.timeInSeconds = timeInSeconds;
        this.stageChild = stageChild;
    }

    public long getTimeInSeconds() {
        return timeInSeconds;
    }

    public Class<? extends GameEntity> getStageChild() {
        return stageChild;
    }
}
