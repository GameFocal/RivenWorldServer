package com.gamefocal.rivenworld.game.entites.living;

import com.badlogic.gdx.math.collision.BoundingBox;
import com.gamefocal.rivenworld.DedicatedServer;
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

public class Boar extends LivingEntity<Boar> implements InteractableEntity {
    public Boar() {
        super(150, new PassiveAggroAiStateMachine(1500, 4800, 60 * 30));
        this.type = "boar";
        this.speed = 1;
        this.aiBehavior = AiBehavior.PASSIVE_AGGRESSIVE;
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
    public void kill() {
        super.kill();
        this.heal(25);
    }

    @Override
    public boolean onHarvest(HiveNetConnection connection) {
        InventoryItem inHand = connection.getInHand().getItem();

        LinkedList<InventoryItem> items = new LinkedList<>();

        InventoryItem meatItem = new RawRedMeat();
        meatItem.setName("Raw Boar Meat");
        meatItem.tag("source", this.getClass().getSimpleName());

        InventoryItem hideItem = new AnimalHide();
        hideItem.setName("Boar Hide");
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

        InventoryStack stack = new InventoryStack(Objects.requireNonNull(RandomUtil.getRandomElementFromList(items)), RandomUtil.getRandomNumberBetween(1,3));

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
        return ShapeUtil.makeBoundBox(this.location.toVector(), 65, 150);
    }

    @Override
    public void attackPlayer(HiveNetConnection connection) {
        float dmg = RandomUtil.getRandomNumberBetween(2, 6);
        // TODO: Is Defending? (Combat Here)
        connection.takeHitWithReduction(null,dmg);

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
