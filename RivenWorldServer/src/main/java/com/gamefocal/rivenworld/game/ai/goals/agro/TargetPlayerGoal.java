package com.gamefocal.rivenworld.game.ai.goals.agro;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.ai.goals.generic.MoveToLocationGoal;
import com.gamefocal.rivenworld.game.entites.generics.LivingEntity;

public class TargetPlayerGoal extends MoveToLocationGoal {

    protected HiveNetConnection target;

    public TargetPlayerGoal(HiveNetConnection target) {
        this.target = target;
        this.goal = target.getPlayer().location;
    }

    @Override
    public void onTick(LivingEntity livingEntity) {
        this.goal = target.getPlayer().location;
        this.reroutePath(livingEntity,this.goal);

        super.onTick(livingEntity);
    }
}
