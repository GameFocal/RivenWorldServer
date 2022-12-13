package com.gamefocal.island.game.tasks;

import com.gamefocal.island.game.tasks.seqence.ExecSequenceAction;
import com.gamefocal.island.game.tasks.seqence.SequenceAction;
import com.gamefocal.island.game.tasks.seqence.WaitSequenceAction;

import java.util.LinkedList;
import java.util.UUID;

public class HiveTaskSequence extends HiveTask {

    protected LinkedList<SequenceAction> actions = new LinkedList<>();

    public HiveTaskSequence(boolean isAsync) {
        super(UUID.randomUUID().toString(), isAsync);
    }

    public HiveTaskSequence exec(Runnable runnable) {
        this.actions.add(new ExecSequenceAction() {
            @Override
            public void run() {
                runnable.run();
            }
        });
        return this;
    }

    public HiveTaskSequence await(Long ticks) {
        this.actions.add(new WaitSequenceAction(ticks));
        return this;
    }

    public HiveTaskSequence add(SequenceAction action) {
        this.actions.add(action);
        return this;
    }

    @Override
    public void tick() {
        super.tick();
        if (this.actions.isEmpty()) {
            this.isCanceled = true;
        }
    }

    @Override
    public void run() {
        SequenceAction a = this.actions.poll();
        if (a != null) {
            if (WaitSequenceAction.class.isAssignableFrom(a.getClass())) {
                // Is a wait
                this.nextRun = System.currentTimeMillis() + (50 * ((WaitSequenceAction) a).getTicks());
            } else if (ExecSequenceAction.class.isAssignableFrom(a.getClass())) {
                // Is a exec
                ((ExecSequenceAction) a).run();
                this.nextRun = System.currentTimeMillis() + 50;
            }
        } else {
            this.isCanceled = true;
        }
    }
}
