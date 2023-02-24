package com.gamefocal.rivenworld.entites.task;

import org.joda.time.DateTime;

import java.util.TimerTask;
import java.util.UUID;

public abstract class HiveTask extends TimerTask {

    private UUID uuid;

    private String friendlyName = "no-name";

    private DateTime lastRun;

    public HiveTask(String friendlyName) {
        this.friendlyName = friendlyName;
        this.uuid = UUID.randomUUID();
    }

    public HiveTask() {
    }

    public abstract void run();

    public UUID getUuid() {
        return uuid;
    }

    public String getFriendlyName() {
        return friendlyName;
    }

    public DateTime getLastRun() {
        return lastRun;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public void setFriendlyName(String friendlyName) {
        this.friendlyName = friendlyName;
    }

    public void setLastRun(DateTime lastRun) {
        this.lastRun = lastRun;
    }
}
