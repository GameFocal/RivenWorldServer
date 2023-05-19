package com.gamefocal.rivenworld.game.entites.living;

import com.gamefocal.rivenworld.DedicatedServer;
import com.gamefocal.rivenworld.entites.combat.CombatAngle;
import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.ai.machines.PassiveAggroAiStateMachine;
import com.gamefocal.rivenworld.game.entites.generics.LivingEntity;
import com.gamefocal.rivenworld.game.sounds.GameSounds;
import com.gamefocal.rivenworld.game.util.RandomUtil;

import java.util.concurrent.TimeUnit;

public class Bear extends LivingEntity<Bear> {
    public Bear() {
        super(400, new PassiveAggroAiStateMachine(600, 1500, 60 * 15));
        this.type = "bear";
        this.speed = 1f;
    }

    @Override
    public void onTick() {

        if (!this.isAggro) {
            if (TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - this.lastPassiveSound) >= 15) {
                if (RandomUtil.getRandomChance(.25f)) {
                    DedicatedServer.instance.getWorld().playSoundAtLocation(GameSounds.BEAR_PASSIVE, this.location, 5000, 1.2f, 1);
                }

                this.lastPassiveSound = System.currentTimeMillis();
            }
        }

        super.onTick();

        if(this.isAggro) {
            this.speed = 3;
        }
    }

    @Override
    public void attackPlayer(HiveNetConnection connection) {
        float dmg = RandomUtil.getRandomNumberBetween(2, 25);
        // TODO: Is Defending? (Combat Here)
        connection.takeDamage(dmg);

        if(this.stateMachine != null && PassiveAggroAiStateMachine.class.isAssignableFrom(this.stateMachine.getClass())) {
            PassiveAggroAiStateMachine passiveAggroAiStateMachine = (PassiveAggroAiStateMachine) this.stateMachine;
            passiveAggroAiStateMachine.aggroStartAt = System.currentTimeMillis();
        }

    }

    @Override
    public void onSync() {
        super.onSync();
        this.specialState = "none";
    }
}
