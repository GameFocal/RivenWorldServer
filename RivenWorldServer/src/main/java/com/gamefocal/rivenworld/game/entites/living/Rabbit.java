package com.gamefocal.rivenworld.game.entites.living;

import com.badlogic.gdx.math.collision.BoundingBox;
import com.gamefocal.rivenworld.DedicatedServer;
import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.InteractableEntity;
import com.gamefocal.rivenworld.game.ai.goals.enums.AiBehavior;
import com.gamefocal.rivenworld.game.ai.machines.PassiveAiStateMachine;
import com.gamefocal.rivenworld.game.entites.generics.LivingEntity;
import com.gamefocal.rivenworld.game.interactable.InteractAction;
import com.gamefocal.rivenworld.game.inventory.InventoryItem;
import com.gamefocal.rivenworld.game.inventory.InventoryStack;
import com.gamefocal.rivenworld.game.items.resources.animals.AnimalHide;
import com.gamefocal.rivenworld.game.items.resources.animals.RawRedMeat;
import com.gamefocal.rivenworld.game.items.weapons.Hatchet;
import com.gamefocal.rivenworld.game.items.weapons.Sword;
import com.gamefocal.rivenworld.game.items.weapons.sword.IronSword;
import com.gamefocal.rivenworld.game.util.RandomUtil;
import com.gamefocal.rivenworld.game.util.ShapeUtil;
import com.gamefocal.rivenworld.service.InventoryService;

import java.util.LinkedList;
import java.util.Objects;

public class Rabbit extends LivingEntity<Rabbit> implements InteractableEntity {
    public Rabbit() {
        super(15, new PassiveAiStateMachine());
        this.type = "Rabbit";
        this.speed = .5f;
        this.aiBehavior = AiBehavior.PASSIVE;
        this.animMulti = 1;
    }

    @Override
    public void kill() {
        super.kill();
        this.heal(10);
    }

    @Override
    public boolean onHarvest(HiveNetConnection connection) {
        InventoryItem inHand = connection.getInHand().getItem();

        LinkedList<InventoryItem> items = new LinkedList<>();

        InventoryItem meatItem = new RawRedMeat();
        meatItem.setName("Raw Hare Meat");
        meatItem.tag("source", this.getClass().getSimpleName());

        InventoryItem hideItem = new AnimalHide();
        hideItem.setName("Hare Hide");
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

        InventoryStack stack = new InventoryStack(Objects.requireNonNull(RandomUtil.getRandomElementFromList(items)), 1);

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
        return ShapeUtil.makeBoundBox(this.location.toVector(), 25, 50);
    }

    @Override
    public String helpText(HiveNetConnection connection) {
        if (!this.isAlive) {
            return "Hit the hare to collect resources, use a sharp tool to get hide.";
        }
        return null;
    }

    @Override
    public void onInteract(HiveNetConnection connection, InteractAction action, InventoryStack inHand) {

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
