package com.gamefocal.island.entites.util.cli;

public abstract class CliPromise implements Runnable {

    private CliTask task;

    public CliPromise() {
        this.task = task;
    }

    public void setTask(CliTask task) {
        this.task = task;
    }

    public CliTask getTask() {
        return task;
    }
}
