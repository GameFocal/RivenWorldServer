package com.gamefocal.rivenworld.game.entites.vfx;

import com.gamefocal.rivenworld.DedicatedServer;
import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.entites.generics.TickEntity;

import java.util.concurrent.TimeUnit;

public class BloodSplat extends GameEntity<BloodSplat> implements TickEntity {

    private long spawned;

    public BloodSplat() {
        this.type = "bloodsplat";
    }

    @Override
    public void onSpawn() {
        this.spawned = System.currentTimeMillis();
    }

    @Override
    public void onDespawn() {

    }

    @Override
    public void onTick() {
        if (System.currentTimeMillis() > (TimeUnit.SECONDS.toMillis(5) + this.spawned)) {
            DedicatedServer.instance.getWorld().despawn(this.uuid);
        }
    }
}
