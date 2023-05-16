package com.gamefocal.rivenworld.game.ai.machines;

import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.math.collision.Sphere;
import com.gamefocal.rivenworld.DedicatedServer;
import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.ai.AiGoal;
import com.gamefocal.rivenworld.game.ai.AiStateMachine;
import com.gamefocal.rivenworld.game.ai.goals.move.AvoidGoal;
import com.gamefocal.rivenworld.game.ai.goals.move.WanderGoal;
import com.gamefocal.rivenworld.game.ai.goals.states.EatGoal;
import com.gamefocal.rivenworld.game.ai.goals.states.RestGoal;
import com.gamefocal.rivenworld.game.entites.generics.LivingEntity;
import com.gamefocal.rivenworld.game.util.ShapeUtil;
import com.gamefocal.rivenworld.service.PlayerService;

import java.util.HashMap;

public class PassiveAiStateMachine extends AiStateMachine {
    @Override
    public void onTick(LivingEntity livingEntity) {

//        System.out.println("GOAL: " + ((this.currentGoal == null) ? "NONE" : this.currentGoal.getClass().getSimpleName()));

//        /*
//         * Check if a player is near the animal and spook them to run away
//         * */
//        for (HiveNetConnection connection : DedicatedServer.get(PlayerService.class).players.values()) {
//            BoundingBox sound = ShapeUtil.makeBoundBox(connection.getPlayer().location.toVector(), connection.noiseRadius(), 400);
//            if (livingEntity.getBoundingBox().contains(sound) || livingEntity.getBoundingBox().intersects(sound)) {
//                // Can hear the player
//                if (this.currentGoal == null || !AvoidGoal.class.isAssignableFrom(this.currentGoal.getClass())) {
//                    System.out.println("Assign Avoid");
//                    this.assignGoal(livingEntity, new AvoidGoal(connection));
//                }
//            }
//        }

        if (this.currentGoal == null && this.queue.size() == 0) {
            AiGoal randomGoal = this.newRandomGoal();
            if (randomGoal == null) {
                this.queueGoal(livingEntity, new WanderGoal(livingEntity.location));
            } else {
                this.queueGoal(livingEntity, randomGoal);
            }
        }
    }

    @Override
    public void onInit(LivingEntity livingEntity) {
        /*
         * No goal yet
         * */
        this.goalTable.put(null, new HashMap<>() {{
            put(new WanderGoal(livingEntity.location), 1);
        }});

        /*
         * After Wander
         * */
        this.goalTable.put(WanderGoal.class, new HashMap<>() {{
            put(new EatGoal(), 5);
            put(new RestGoal(), 3);
        }});
    }
}
