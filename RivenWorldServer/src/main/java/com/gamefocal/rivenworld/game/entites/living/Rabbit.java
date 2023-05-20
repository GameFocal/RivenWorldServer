package com.gamefocal.rivenworld.game.entites.living;

import com.badlogic.gdx.math.collision.BoundingBox;
import com.gamefocal.rivenworld.game.ai.machines.PassiveAiStateMachine;
import com.gamefocal.rivenworld.game.entites.generics.LivingEntity;
import com.gamefocal.rivenworld.game.util.ShapeUtil;

public class Rabbit extends LivingEntity<Rabbit> {
    public Rabbit() {
        super(100, new PassiveAiStateMachine());
        this.type = "Rabbit";
        this.speed = .5f;
    }

    @Override
    public BoundingBox getBoundingBox() {
        return ShapeUtil.makeBoundBox(this.location.toVector().add(0,0,0), 25, 25);
    }
}
