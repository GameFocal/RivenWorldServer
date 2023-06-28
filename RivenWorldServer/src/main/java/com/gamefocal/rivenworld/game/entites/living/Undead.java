package com.gamefocal.rivenworld.game.entites.living;

import com.badlogic.gdx.math.collision.BoundingBox;
import com.gamefocal.rivenworld.DedicatedServer;
import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.InteractableEntity;
import com.gamefocal.rivenworld.game.ai.goals.enums.AiBehavior;
import com.gamefocal.rivenworld.game.ai.machines.GuardingPassiveAggroAiStateMachine;
import com.gamefocal.rivenworld.game.entites.generics.LivingEntity;
import com.gamefocal.rivenworld.game.interactable.InteractAction;
import com.gamefocal.rivenworld.game.inventory.InventoryStack;
import com.gamefocal.rivenworld.game.items.weapons.Torch;
import com.gamefocal.rivenworld.game.sounds.GameSounds;
import com.gamefocal.rivenworld.game.util.RandomUtil;
import com.gamefocal.rivenworld.game.util.ShapeUtil;

import java.util.concurrent.TimeUnit;

public class Undead extends LivingEntity<Undead> implements InteractableEntity {
    public Undead() {
        super(RandomUtil.getRandomNumberBetween(100, 400), new GuardingPassiveAggroAiStateMachine(1200, 1500, 60 * 15));
        this.type = "Undead";
        this.speed = 1f;
        this.aiBehavior = AiBehavior.AGGRESSIVE;
        this.passiveSound = GameSounds.SHRINE_SOUND;
        this.aggroSound = GameSounds.HELL_SOUND;
    }

    @Override
    public void onSpawn() {
        super.onSpawn();
        this.specialState = "spawn";
    }

    @Override
    public void kill() {
        super.kill();
        this.heal(50);

        // TODO: Set despawn timer and spawn loot
    }

    @Override
    public void onTick() {

        if (!this.isAggro) {
            if (TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - this.lastPassiveSound) >= 15) {
                if (RandomUtil.getRandomChance(.25f)) {
                    DedicatedServer.instance.getWorld().playSoundAtLocation(GameSounds.SHRINE_SOUND, this.location, 5000, 1.0f, 1f, 6);
                }

                this.lastPassiveSound = System.currentTimeMillis();
            }
        }

        super.onTick();

        if (this.isAggro) {
            this.speed = 3;

            /*
             * Check if player holding a torch
             * */
            GuardingPassiveAggroAiStateMachine guardingPassiveAggroAiStateMachine = (GuardingPassiveAggroAiStateMachine) this.stateMachine;
            if (guardingPassiveAggroAiStateMachine.aggro != null) {
                if (guardingPassiveAggroAiStateMachine.aggro.getPlayer().equipmentSlots.inHand != null && Torch.class.isAssignableFrom(guardingPassiveAggroAiStateMachine.aggro.getPlayer().equipmentSlots.inHand.getItem().getClass())) {
                    // Holding a torch
                    this.stateMachine.closeGoal(this);
                }
            }

        }
    }

    @Override
    public boolean onHarvest(HiveNetConnection connection) {
        return false;
    }

    @Override
    public boolean onHit(HiveNetConnection connection) {
        return false;
    }

    @Override
    public BoundingBox getBoundingBox() {
        return ShapeUtil.makeBoundBox(this.location.toVector(), 75, 200);
    }

    @Override
    public void attackPlayer(HiveNetConnection connection) {
        float dmg = RandomUtil.getRandomNumberBetween(2, 25);
        // TODO: Is Defending? (Combat Here)
        connection.takeDamage(dmg);

        this.specialState = "cast";
        if (this.stateMachine != null && GuardingPassiveAggroAiStateMachine.class.isAssignableFrom(this.stateMachine.getClass())) {
            GuardingPassiveAggroAiStateMachine passiveAggroAiStateMachine = (GuardingPassiveAggroAiStateMachine) this.stateMachine;
            passiveAggroAiStateMachine.aggroStartAt = System.currentTimeMillis();
        }

    }

    @Override
    public void onSync() {
        super.onSync();
        this.specialState = "none";
    }

    @Override
    public void onInteract(HiveNetConnection connection, InteractAction action, InventoryStack inHand) {

    }

    @Override
    public boolean canInteract(HiveNetConnection netConnection) {
        return true;
    }

    @Override
    public String helpText(HiveNetConnection connection) {
//        if (!this.isAlive) {
//            return "Hit the bear to collect resources, use a sharp tool to get hide.";
//        }
//
        return null;
    }

    @Override
    public String onFocus(HiveNetConnection connection) {
        return null;
    }
}
