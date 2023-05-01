package com.gamefocal.rivenworld.game.ai.goals.move;

import com.badlogic.gdx.math.Vector3;
import com.gamefocal.rivenworld.DedicatedServer;
import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.ai.goals.generic.MoveToLocationGoal;
import com.gamefocal.rivenworld.game.entites.generics.LivingEntity;
import com.gamefocal.rivenworld.game.util.AIUtils;
import com.gamefocal.rivenworld.game.util.Location;
import com.gamefocal.rivenworld.game.util.LocationUtil;

public class AvoidGoal extends MoveToLocationGoal {

    public HiveNetConnection avoidPlayer = null;

    public AvoidGoal(HiveNetConnection player) {
        this.avoidPlayer = player;
    }

    @Override
    public void onStart(LivingEntity livingEntity) {
        Vector3 away = AIUtils.getRunAwayLocation(livingEntity.location.toVector(), this.avoidPlayer.getPlayer().location.toVector(), 500);
        away.z = DedicatedServer.instance.getWorld().getRawHeightmap().getHeightFromLocation(Location.fromVector(away));

        this.goal = Location.fromVector(away);

        super.onStart(livingEntity);
    }

    @Override
    public void onTick(LivingEntity livingEntity) {

        /*
         * Change the speed
         * */
        float dst = this.avoidPlayer.getPlayer().location.dist(livingEntity.location);

        if (dst <= 1000) {
            livingEntity.speed = 3;
        } else {
            this.completeAndNext(livingEntity, new WanderGoal(livingEntity.location));
        }

        super.onTick(livingEntity);
    }
}
