package com.gamefocal.rivenworld.game.entites.living;

import com.badlogic.gdx.math.collision.BoundingBox;
import com.gamefocal.rivenworld.DedicatedServer;
import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.InteractableEntity;
import com.gamefocal.rivenworld.game.ai.machines.PassiveAggroAiStateMachine;
import com.gamefocal.rivenworld.game.entites.generics.LivingEntity;
import com.gamefocal.rivenworld.game.interactable.InteractAction;
import com.gamefocal.rivenworld.game.inventory.InventoryStack;
import com.gamefocal.rivenworld.game.sounds.GameSounds;
import com.gamefocal.rivenworld.game.util.RandomUtil;
import com.gamefocal.rivenworld.game.util.ShapeUtil;

import java.util.concurrent.TimeUnit;

public class Boar extends LivingEntity<Boar> implements InteractableEntity {
    public Boar() {
        super(100, new PassiveAggroAiStateMachine(1500, 4800, 60 * 30));
        this.type = "boar";
        this.speed = 1;
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

        if(this.isAggro) {
            this.speed = 3;
        }
    }

    @Override
    public BoundingBox getBoundingBox() {
        return ShapeUtil.makeBoundBox(this.location.toVector().add(0,0,100), 40, 50);
    }

    @Override
    public void attackPlayer(HiveNetConnection connection) {
        float dmg = RandomUtil.getRandomNumberBetween(2, 10);
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

    @Override
    public void onInteract(HiveNetConnection connection, InteractAction action, InventoryStack inHand) {

    }

    @Override
    public String helpText(HiveNetConnection connection) {
        if(!this.isAlive) {
            return "Hit the boar to collect resources, use a sharp tool to get hide.";
        }
        return null;
    }

    @Override
    public boolean canInteract(HiveNetConnection netConnection) {
        return true;
    }

    @Override
    public String onFocus(HiveNetConnection connection) {
        return null;
    }
}
