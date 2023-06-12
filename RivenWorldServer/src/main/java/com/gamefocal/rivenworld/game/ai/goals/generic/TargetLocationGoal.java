package com.gamefocal.rivenworld.game.ai.goals.generic;

import com.badlogic.gdx.math.Vector3;
import com.gamefocal.rivenworld.game.ai.AiGoal;
import com.gamefocal.rivenworld.game.entites.generics.LivingEntity;
import com.gamefocal.rivenworld.game.util.Location;

public class TargetLocationGoal extends AiGoal {

    protected Location target;

    @Override
    public void onStart(LivingEntity livingEntity) {

    }

    @Override
    public void onComplete(LivingEntity livingEntity) {

    }

    @Override
    public void onTick(LivingEntity livingEntity) {
        Vector3 currentPosition = livingEntity.location.toVector();
        Vector3 targetPosition = this.target.toVector();

        Vector3 dir = targetPosition.cpy().sub(currentPosition).nor();
        livingEntity.setVelocity(dir);
        livingEntity.setLocationGoal(this.target.toVector());
    }
}
