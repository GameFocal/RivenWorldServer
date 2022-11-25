package com.gamefocal.island.game.tasks;

import java.util.UUID;

public abstract class HiveTask implements Runnable {

    protected String friendlyName = null;

    protected UUID uuid;

    protected Long lastRun = 0L;

    protected Long nextRun = 0L;

    protected boolean isCanceled = false;

    protected boolean isAsync = false;

    public HiveTask(String name, boolean isAsync) {
        this.friendlyName = name;
        this.uuid = UUID.randomUUID();
        this.isAsync = isAsync;
    }

    public boolean isReady() {
        return System.currentTimeMillis() >= this.nextRun && !this.isCanceled;
    }

    public void tick() {
        this.lastRun = System.currentTimeMillis();
    }

    public boolean isAsync() {
        return isAsync;
    }

    public void setAsync(boolean async) {
        isAsync = async;
    }

    public abstract void run();

    public void cancel() {
        this.isCanceled = true;
    }

    public String getFriendlyName() {
        return friendlyName;
    }

    public UUID getUuid() {
        return uuid;
    }

    public Long getLastRun() {
        return lastRun;
    }

    public Long getNextRun() {
        return nextRun;
    }

    public boolean isCanceled() {
        return isCanceled;
    }
}
