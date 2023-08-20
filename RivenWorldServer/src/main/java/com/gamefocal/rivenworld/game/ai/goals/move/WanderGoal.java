package com.gamefocal.rivenworld.game.ai.goals.move;

import com.gamefocal.rivenworld.DedicatedServer;
import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.ai.goals.generic.MoveToLocationGoal;
import com.gamefocal.rivenworld.game.entites.generics.LivingEntity;
import com.gamefocal.rivenworld.game.util.Location;
import com.gamefocal.rivenworld.game.util.LocationUtil;
import com.gamefocal.rivenworld.service.PlayerService;

public class WanderGoal extends MoveToLocationGoal {
    public WanderGoal(Location base) {
        this.goal = LocationUtil.getRandomLocationInRadius(5000, base);
        DedicatedServer.instance.getWorld().getRawHeightmap().getHeightLocationFromLocation(this.goal);
    }

    @Override
    public void onTick(LivingEntity livingEntity) {
        super.onTick(livingEntity);
    }
}
