package com.gamefocal.rivenworld.game.ai.goals.move;

import com.gamefocal.rivenworld.DedicatedServer;
import com.gamefocal.rivenworld.game.ai.goals.generic.MoveToLocationGoal;
import com.gamefocal.rivenworld.game.util.Location;
import com.gamefocal.rivenworld.game.util.LocationUtil;

public class WanderGoal extends MoveToLocationGoal {
    public WanderGoal(Location base) {
        this.goal = LocationUtil.getRandomLocationInRadius(5000, base);
        DedicatedServer.instance.getWorld().getRawHeightmap().getHeightLocationFromLocation(this.goal);
    }
}
