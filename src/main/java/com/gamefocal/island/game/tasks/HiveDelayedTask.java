package com.gamefocal.island.game.tasks;

public abstract class HiveDelayedTask extends HiveTask {
    private Long delay = 0L;

    public HiveDelayedTask(String name, Long delay, boolean isAsync) {
        super(name, isAsync);
        this.delay = delay;
        this.nextRun = System.currentTimeMillis() + (this.delay * 50);
    }
}
