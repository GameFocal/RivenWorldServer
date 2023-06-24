package com.gamefocal.rivenworld.commands.chat.dev;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector3;
import com.gamefocal.rivenworld.entites.net.*;
import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.ai.AiGoal;
import com.gamefocal.rivenworld.game.ai.goals.generic.MoveToLocationGoal;
import com.gamefocal.rivenworld.game.entites.generics.LivingEntity;
import com.gamefocal.rivenworld.game.ray.HitResult;
import com.gamefocal.rivenworld.game.ray.hit.EntityHitResult;
import com.gamefocal.rivenworld.game.util.Location;

@Command(name = "edebug", sources = "chat")
public class EntityDebugCommand extends HiveCommand {
    @Override
    public void onCommand(HiveNetMessage message, CommandSource source, HiveNetConnection netConnection) throws Exception {
        if (netConnection.isAdmin()) {

            HitResult result = netConnection.getLookingAt();

            if (EntityHitResult.class.isAssignableFrom(result.getClass())) {

                GameEntity entity = (GameEntity) result.get();

                if (LivingEntity.class.isAssignableFrom(entity.getClass())) {

                    LivingEntity livingEntity = (LivingEntity) entity;
                    AiGoal goal = livingEntity.getStateMachine().getCurrentGoal();

                    if (goal != null) {

                        System.out.println("GOAL: " + goal.getClass().getSimpleName());

                        if (MoveToLocationGoal.class.isAssignableFrom(goal.getClass())) {

                            MoveToLocationGoal moveToLocationGoal = (MoveToLocationGoal) goal;

                            // Draw the path
                            netConnection.drawDebugBox(Color.YELLOW, Location.fromVector(moveToLocationGoal.getSubGoal()), new Location(50, 50, 50), 1);

                            for (Vector3 c : moveToLocationGoal.getWaypoints()) {
                                netConnection.drawDebugBox(Color.GREEN, Location.fromVector(c), new Location(50, 50, 50), 1);
                            }

                        }

                    }

                }

            }

        }
    }
}
