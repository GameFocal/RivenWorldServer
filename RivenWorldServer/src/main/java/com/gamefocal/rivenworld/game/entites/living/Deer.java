package com.gamefocal.rivenworld.game.entites.living;

import com.badlogic.gdx.math.collision.BoundingBox;
import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.InteractableEntity;
import com.gamefocal.rivenworld.game.ai.machines.PassiveAiStateMachine;
import com.gamefocal.rivenworld.game.entites.generics.LivingEntity;
import com.gamefocal.rivenworld.game.interactable.InteractAction;
import com.gamefocal.rivenworld.game.inventory.InventoryStack;
import com.gamefocal.rivenworld.game.util.ShapeUtil;

public class Deer extends LivingEntity<Deer> implements InteractableEntity {
    public Deer() {
        super(45, new PassiveAiStateMachine());
        this.type = "deer";
        this.speed = 1;
    }

    @Override
    public BoundingBox getBoundingBox() {
        return ShapeUtil.makeBoundBox(this.location.toVector().add(0,0,100), 40, 50);
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
        if(!this.isAlive) {
            return "Hit the buck to collect resources, use a sharp tool to get hide.";
        }

        return null;
    }

    @Override
    public String onFocus(HiveNetConnection connection) {
        return null;
    }
}
