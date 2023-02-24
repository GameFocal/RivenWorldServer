package com.gamefocal.rivenworld.game.ai.jobs;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Sphere;
import com.gamefocal.rivenworld.game.ai.AiJob;
import com.gamefocal.rivenworld.game.entites.generics.LivingEntity;
import com.gamefocal.rivenworld.game.util.Location;

import java.util.concurrent.TimeUnit;

public class MoveToLocationJob extends AiJob {

    private Vector3 starting;

    private Vector3 finish;

    private Sphere goal;

    private Long started;

    private float dist;

    public MoveToLocationJob(LivingEntity entity, Location targetLocation) {
        this.starting = entity.location.cpy().setZ(0).toVector();
        this.finish = targetLocation.cpy().setZ(0).toVector();
        this.goal = new Sphere(targetLocation.toVector(), 200);
        this.dist = this.starting.dst(this.finish);
    }

    @Override
    public void onStart(LivingEntity livingEntity) {
        this.started = System.currentTimeMillis();
    }

    @Override
    public void onWork(LivingEntity livingEntity) {
        Sphere loc = new Sphere(livingEntity.location.toVector(), 100);

        // TODO: Check reported location against the job
        float projectedDist = livingEntity.speed * TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - this.started);
        float percentComplete = projectedDist / this.dist;
        if (percentComplete > 1) {
            percentComplete = 1f;
        }

        Vector3 pointInTime = this.starting.lerp(this.finish, percentComplete);

        // See if we are nearby this.
        if(livingEntity.location.toVector().dst(pointInTime) > 1000) {
            System.err.println("INVALID MOVEMENT... TODO: SNAP BACK");
        }

        if (this.goal.overlaps(loc)) {
            // Has met the goal.
            this.setComplete(true);
        }
    }

    @Override
    public void onComplete(LivingEntity livingEntity) {

    }
}
