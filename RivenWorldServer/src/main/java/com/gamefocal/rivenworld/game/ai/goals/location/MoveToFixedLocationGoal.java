package com.gamefocal.rivenworld.game.ai.goals.location;

import com.gamefocal.rivenworld.game.ai.goals.generic.MoveToLocationGoal;
import com.gamefocal.rivenworld.game.entites.generics.LivingEntity;
import com.gamefocal.rivenworld.game.util.Location;

public class MoveToFixedLocationGoal extends MoveToLocationGoal {

    protected Location location;

    public MoveToFixedLocationGoal(Location location) {
        this.location = location;
    }

    @Override
    public void onStart(LivingEntity livingEntity) {
        this.goal = this.location;
        super.onStart(livingEntity);
    }

    @Override
    public void findGoal() {
        // Do Nothing
    }
}
