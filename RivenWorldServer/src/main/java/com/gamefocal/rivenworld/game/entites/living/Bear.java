package com.gamefocal.rivenworld.game.entites.living;

import com.badlogic.gdx.math.collision.BoundingBox;
import com.gamefocal.rivenworld.DedicatedServer;
import com.gamefocal.rivenworld.entites.combat.CombatStance;
import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.InteractableEntity;
import com.gamefocal.rivenworld.game.ai.goals.enums.AiBehavior;
import com.gamefocal.rivenworld.game.ai.machines.PassiveAggroAiStateMachine;
import com.gamefocal.rivenworld.game.entites.generics.LivingEntity;
import com.gamefocal.rivenworld.game.interactable.InteractAction;
import com.gamefocal.rivenworld.game.inventory.InventoryItem;
import com.gamefocal.rivenworld.game.inventory.InventoryStack;
import com.gamefocal.rivenworld.game.items.resources.animals.AnimalHide;
import com.gamefocal.rivenworld.game.items.resources.animals.RawRedMeat;
import com.gamefocal.rivenworld.game.items.weapons.Sword;
import com.gamefocal.rivenworld.game.sounds.GameSounds;
import com.gamefocal.rivenworld.game.util.RandomUtil;
import com.gamefocal.rivenworld.game.util.ShapeUtil;
import com.gamefocal.rivenworld.service.InventoryService;

import java.util.LinkedList;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class Bear extends LivingEntity<Bear> implements InteractableEntity {
    public Bear() {
        super(200, new PassiveAggroAiStateMachine(600, 1500, 60 * 15));
        this.type = "bear";
        this.speed = 1f;
        this.aiBehavior = AiBehavior.PASSIVE_AGGRESSIVE;
    }

    @Override
    public void kill() {
        super.kill();
        this.heal(50);
    }

    @Override
    public void onTick() {

        if (!this.isAggro) {
            if (TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - this.lastPassiveSound) >= 15) {
                if (RandomUtil.getRandomChance(.25f)) {
                    DedicatedServer.instance.getWorld().playSoundAtLocation(GameSounds.BEAR_PASSIVE, this.location, 5000, 1.2f, 1, 5);
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
        InventoryItem inHand = connection.getInHand().getItem();

        LinkedList<InventoryItem> items = new LinkedList<>();

        InventoryItem meatItem = new RawRedMeat();
        meatItem.setName("Raw Bear Meat");
        meatItem.tag("source", this.getClass().getSimpleName());

        InventoryItem hideItem = new AnimalHide();
        hideItem.setName("Bear Hide");
        hideItem.tag("source", this.getClass().getSimpleName());

//        InventoryItem boneItem = new Bone();
//        meatItem.setName("Hare Bones");
//        meatItem.tag("source", this.getClass().getSimpleName());

        if (Sword.class.isAssignableFrom(inHand.getClass())) {
            items.add(meatItem);
            items.add(hideItem);
        } else {
            items.add(meatItem);
        }

        InventoryStack stack = new InventoryStack(Objects.requireNonNull(RandomUtil.getRandomElementFromList(items)), 6);

        if (connection.getPlayer().inventory.canAdd(stack)) {
            connection.getPlayer().inventory.add(stack);
            connection.displayItemAdded(stack);
            connection.updatePlayerInventory();
        } else {
            connection.displayInventoryFull();
        }

        return true;
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
        float dmg = RandomUtil.getRandomNumberBetween(12, 14);
        // TODO: Is Defending? (Combat Here)

        connection.takeHitWithReduction(null,dmg);

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
        if (!this.isAlive) {
            return "Hit the bear to collect resources, use a sharp tool to get hide.";
        }

        return null;
    }

    @Override
    public String onFocus(HiveNetConnection connection) {
        return null;
    }
}
