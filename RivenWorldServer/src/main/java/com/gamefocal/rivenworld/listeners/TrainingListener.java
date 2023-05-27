package com.gamefocal.rivenworld.listeners;

import com.gamefocal.rivenworld.entites.events.EventHandler;
import com.gamefocal.rivenworld.entites.events.EventInterface;
import com.gamefocal.rivenworld.events.combat.PlayerDealDamageEvent;
import com.gamefocal.rivenworld.game.combat.EntityHitDamage;
import com.gamefocal.rivenworld.game.combat.HitDamage;
import com.gamefocal.rivenworld.game.entites.placable.AttackDummyEntity;
import com.gamefocal.rivenworld.game.ray.HitResult;
import com.gamefocal.rivenworld.service.TaskService;

public class TrainingListener implements EventInterface {

    /*
     * Shake the dummy when it is hit
     * */
    @EventHandler
    public void onHitDummy(PlayerDealDamageEvent dealDamageEvent) {
        HitResult hitResult = dealDamageEvent.getHitResult();
        HitDamage hitDamage = dealDamageEvent.getHitDamage();

        if (EntityHitDamage.class.isAssignableFrom(hitDamage.getClass())) {

            EntityHitDamage entityHitDamage = (EntityHitDamage) hitDamage;

            if (AttackDummyEntity.class.isAssignableFrom(entityHitDamage.getEntity().getClass())) {
                AttackDummyEntity attackDummyEntity = (AttackDummyEntity) entityHitDamage.getEntity();

                // TODO: ++ EXP
                // TODO: Shake
                TaskService.scheduledDelayTask(() -> {
                    attackDummyEntity.setMeta("anim", true);
                }, 0L, false);
                TaskService.scheduledDelayTask(() -> {
                    attackDummyEntity.setMeta("anim", false);
                }, 20L, false);


//                if (attackDummyEntity.cancelTask != null) {
//                    attackDummyEntity.setMeta("anim", false);
//                    attackDummyEntity.cancelTask.cancel();
//                }
//
//                attackDummyEntity.cancelTask = TaskService.scheduledDelayTask(() -> {
//                    attackDummyEntity.setMeta("anim", false);
//                }, 20L, false);
            }

        }

    }

}
