package com.gamefocal.island.game.ai;

import com.gamefocal.island.entites.net.HiveNetConnection;
import com.gamefocal.island.game.entites.generics.LivingEntity;
import com.gamefocal.island.game.util.Location;

import java.util.UUID;

public abstract class AiJob {

    private UUID uuid;

    private boolean inProgress = false;

    private boolean isComplete = false;

    private Long startedAt = 0L;

    public abstract void onStart(LivingEntity livingEntity);

    public abstract void onWork(LivingEntity livingEntity);

    public abstract void onComplete(LivingEntity livingEntity);

    public UUID getUuid() {
        return uuid;
    }

    public boolean isInProgress() {
        return inProgress;
    }

    public void setInProgress(boolean inProgress) {
        this.inProgress = inProgress;
    }

    public boolean isComplete() {
        return isComplete;
    }

    public void setComplete(boolean complete) {
        isComplete = complete;
    }

    public Long getStartedAt() {
        return startedAt;
    }

    public void setStartedAt(Long startedAt) {
        this.startedAt = startedAt;
    }
}
