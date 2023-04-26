package com.gamefocal.rivenworld.game.ai.goals;

import com.gamefocal.rivenworld.game.util.Location;
import com.gamefocal.rivenworld.game.util.LocationUtil;

public class WanderGoal extends MoveToLocationGoal {
    public WanderGoal(Location base) {
        this.goal = LocationUtil.getRandomLocationInRadius(10000, base);
    }
}
