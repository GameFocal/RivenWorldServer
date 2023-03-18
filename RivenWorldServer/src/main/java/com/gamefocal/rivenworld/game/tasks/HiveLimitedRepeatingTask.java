package com.gamefocal.rivenworld.game.tasks;

public abstract class HiveLimitedRepeatingTask extends HiveTask {

    private Long delay = 0L;

    private Long period = 0L;

    private int repeatMaxTimes;

    private int runs = 0;

    public HiveLimitedRepeatingTask(String name, Long delay, Long period, int repeatNTimes, boolean isAsync) {
        super(name, isAsync);
        this.delay = delay;
        this.period = period;
        this.nextRun = System.currentTimeMillis() + (this.delay * 50);
        this.repeatMaxTimes = repeatNTimes;
    }

    @Override
    public void tick() {

        if (this.runs++ > this.repeatMaxTimes) {
            this.cancel();
            return;
        }

        super.tick();
        if (!this.isCanceled) {
            this.nextRun = System.currentTimeMillis() + (this.period * 50);
        }
    }
}
