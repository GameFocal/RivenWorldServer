package com.gamefocal.island.game.tasks;

public abstract class HiveRepeatingTask extends HiveTask {

    private Long delay = 0L;

    private Long period = 0L;

    public HiveRepeatingTask(String name, Long delay, Long period, boolean isAsync) {
        super(name, isAsync);
        this.delay = delay;
        this.period = period;
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.isCanceled) {
            this.nextRun = System.currentTimeMillis() + (this.period * 1000);
        }
    }
}
