package com.gamefocal.rivenworld.game.entites.living;

import com.badlogic.gdx.math.collision.BoundingBox;
import com.gamefocal.rivenworld.DedicatedServer;
import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.InteractableEntity;
import com.gamefocal.rivenworld.game.ai.goals.enums.AiBehavior;
import com.gamefocal.rivenworld.game.ai.machines.PassiveAggroAiStateMachine;
import com.gamefocal.rivenworld.game.entites.generics.LivingEntity;
import com.gamefocal.rivenworld.game.interactable.InteractAction;
import com.gamefocal.rivenworld.game.inventory.InventoryStack;
import com.gamefocal.rivenworld.game.items.resources.misc.Fabric;
import com.gamefocal.rivenworld.game.sounds.GameSounds;
import com.gamefocal.rivenworld.game.util.RandomUtil;
import com.gamefocal.rivenworld.game.util.ShapeUtil;
import com.gamefocal.rivenworld.service.InventoryService;
import com.gamefocal.rivenworld.service.LootService;
import com.gamefocal.rivenworld.service.TaskService;

import java.util.concurrent.TimeUnit;

public class Undead extends LivingEntity<Undead> implements InteractableEntity {
    public Undead() {
        super(600, new PassiveAggroAiStateMachine(800, 4800, 60 * 30));
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

        TaskService.scheduledDelayTask(() -> {
            InventoryStack stack = null;

            int roll = RandomUtil.getRandomNumberBetween(1, 100);

            if (roll == 1) {
                // Linen
                stack = new InventoryStack(new Fabric(), 5);
            } else if (roll <= 25) {
                stack = DedicatedServer.get(LootService.class).generateLoot(RandomUtil.getRandomNumberBetween(0, 2), 1).get(0);
            }

            if (stack != null) {
//            DropBag dropBag = new DropBag(null, stack);
                DedicatedServer.get(InventoryService.class).dropBagAtLocation(null, DedicatedServer.instance.getWorld().getRawHeightmap().getHeightLocationFromLocation(this.location.cpy()), stack);
            }
            DedicatedServer.instance.getWorld().despawn(this.uuid);
        }, 100L, false);

        // TODO: Set despawn timer and spawn loot
    }

    @Override
    public void onTick() {
        if (!this.isAggro) {
            if (TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - this.lastPassiveSound) >= 15) {
                if (RandomUtil.getRandomChance(.25f)) {
                    DedicatedServer.instance.getWorld().playSoundAtLocation(GameSounds.BOAR_AGGRO, this.location, 5000, 1.2f, 1);
                }

                this.lastPassiveSound = System.currentTimeMillis();
            }
        }

        super.onTick();

        if (this.isAggro) {
            this.speed = 3;
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
        float dmg = RandomUtil.getRandomNumberBetween(2, 6);
        // TODO: Is Defending? (Combat Here)
        connection.takeHitWithReduction(null, dmg);

        this.specialState = "cast";
        if (this.stateMachine != null && PassiveAggroAiStateMachine.class.isAssignableFrom(this.stateMachine.getClass())) {
            PassiveAggroAiStateMachine passiveAggroAiStateMachine = (PassiveAggroAiStateMachine) this.stateMachine;
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
