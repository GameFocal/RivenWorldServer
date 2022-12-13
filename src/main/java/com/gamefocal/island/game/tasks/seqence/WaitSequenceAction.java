package com.gamefocal.island.game.tasks.seqence;

public class WaitSequenceAction implements SequenceAction {

    private Long ticks = 1L;

    public WaitSequenceAction(Long ticks) {
        this.ticks = ticks;
    }

    public Long getTicks() {
        return ticks;
    }
}
