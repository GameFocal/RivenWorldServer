package com.gamefocal.rivenworld.game.entites.living;

import com.gamefocal.rivenworld.game.InteractableEntity;
import com.gamefocal.rivenworld.game.ai.AiStateMachine;
import com.gamefocal.rivenworld.game.ai.machines.StaticAiStateMachine;
import com.gamefocal.rivenworld.game.entites.generics.LivingEntity;
import com.gamefocal.rivenworld.game.inventory.InventoryItem;
import com.gamefocal.rivenworld.game.inventory.equipment.EquipmentSlots;

import java.util.HashMap;

public abstract class NPC<T> extends LivingEntity<T> implements InteractableEntity {

    protected EquipmentSlots equipmentSlots = new EquipmentSlots();

    public NPC() {
        super(100, new StaticAiStateMachine());
        this.type = "netnpc";
    }
}
