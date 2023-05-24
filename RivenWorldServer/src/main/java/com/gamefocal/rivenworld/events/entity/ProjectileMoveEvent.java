package com.gamefocal.rivenworld.events.entity;

import com.gamefocal.rivenworld.entites.events.Event;
import com.gamefocal.rivenworld.game.entites.projectile.FlyingProjectile;

public class ProjectileMoveEvent extends Event<ProjectileMoveEvent> {

    private FlyingProjectile projectile;

    public ProjectileMoveEvent(FlyingProjectile projectile) {
        this.projectile = projectile;
    }

    public FlyingProjectile getProjectile() {
        return projectile;
    }
}
