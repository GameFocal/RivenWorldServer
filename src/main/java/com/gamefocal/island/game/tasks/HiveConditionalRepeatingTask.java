package com.gamefocal.island.game.tasks;

public abstract class HiveConditionalRepeatingTask extends HiveRepeatingTask {
    public HiveConditionalRepeatingTask(String name, Long delay, Long period, boolean isAsync) {
        super(name, delay, period, isAsync);
    }

    public abstract boolean condition();

    @Override
    public void tick() {
        if (this.condition()) {
            this.cancel();
        }

        super.tick();
    }
}
