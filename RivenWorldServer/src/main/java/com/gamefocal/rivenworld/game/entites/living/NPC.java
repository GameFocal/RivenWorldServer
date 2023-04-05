package com.gamefocal.rivenworld.game.entites.living;

import com.gamefocal.rivenworld.game.ai.AiStateMachine;
import com.gamefocal.rivenworld.game.ai.machines.StaticAiStateMachine;
import com.gamefocal.rivenworld.game.entites.generics.LivingEntity;
import com.gamefocal.rivenworld.game.inventory.InventoryItem;

import java.util.HashMap;

public abstract class NPC<T> extends LivingEntity<T> {
    public NPC() {
        super(100, new StaticAiStateMachine());
        this.type = "netnpc";
    }
}
