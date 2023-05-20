package com.gamefocal.rivenworld.game.entites.living;

import com.badlogic.gdx.math.collision.BoundingBox;
import com.gamefocal.rivenworld.game.ai.machines.PassiveAiStateMachine;
import com.gamefocal.rivenworld.game.entites.generics.LivingEntity;
import com.gamefocal.rivenworld.game.util.ShapeUtil;

public class Deer extends LivingEntity<Deer> {
    public Deer() {
        super(100, new PassiveAiStateMachine());
        this.type = "deer";
        this.speed = 1;
    }

    @Override
    public BoundingBox getBoundingBox() {
        return ShapeUtil.makeBoundBox(this.location.toVector().add(0,0,100), 40, 50);
    }
}
