package com.gamefocal.rivenworld.game.ai.goals.generic;

import com.gamefocal.rivenworld.game.ai.path.WorldCell;
import com.gamefocal.rivenworld.game.entites.generics.LivingEntity;

public class TargetCellGoal extends TargetLocationGoal {

    protected WorldCell cell;

    public TargetCellGoal(WorldCell cell) {
        this.cell = cell;
        this.target = cell.getCenterInGameSpace(true);
    }

    @Override
    public void onStart(LivingEntity livingEntity) {

    }

    @Override
    public void onComplete(LivingEntity livingEntity) {

    }

    @Override
    public void onTick(LivingEntity livingEntity) {
        this.target = this.cell.getCenterInGameSpace(true);
        super.onTick(livingEntity);
    }
}
