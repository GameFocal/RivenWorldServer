package com.gamefocal.rivenworld.game.entites.vfx;

import com.gamefocal.rivenworld.DedicatedServer;
import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.service.ShrineService;

public class ShrineFogVFX extends GameEntity<ShrineFogVFX> {

    public ShrineFogVFX() {
        this.type = "ShrineFog";
    }

    @Override
    public void onSpawn() {
        DedicatedServer.get(ShrineService.class).shrineVFX.add(this);
    }

    @Override
    public void onDespawn() {
        DedicatedServer.get(ShrineService.class).shrineVFX.remove(this);
    }

    @Override
    public void onTick() {

    }
}
