package com.gamefocal.rivenworld.game.ai.goals.location;

import com.gamefocal.rivenworld.game.ai.goals.generic.MoveToLocationGoal;
import com.gamefocal.rivenworld.game.entites.generics.LivingEntity;
import com.gamefocal.rivenworld.game.util.Location;
import com.gamefocal.rivenworld.game.util.LocationUtil;

import java.util.LinkedList;

public class MoveToRandomLocationGoal extends MoveToLocationGoal {
    @Override
    public void onStart(LivingEntity livingEntity) {
        this.goal = LocationUtil.getRandomLocationInRadius(10000, livingEntity.location);
        super.onStart(livingEntity);
    }

    @Override
    public void findGoal() {
        this.goal = LocationUtil.getRandomLocationInRadius(10000, this.livingEntity.location);
    }
}
