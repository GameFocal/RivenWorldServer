package com.gamefocal.rivenworld.game.entites.living;

import com.gamefocal.rivenworld.game.InteractableEntity;
import com.gamefocal.rivenworld.game.ai.AiStateMachine;
import com.gamefocal.rivenworld.game.ai.machines.StaticAiStateMachine;
import com.gamefocal.rivenworld.game.entites.generics.LivingEntity;
import com.gamefocal.rivenworld.game.inventory.InventoryItem;
import com.gamefocal.rivenworld.game.inventory.equipment.EquipmentSlots;
import com.gamefocal.rivenworld.models.GameNpcModel;
import com.gamefocal.rivenworld.service.DataService;

import java.util.HashMap;

public abstract class NPC<T> extends LivingEntity<T> implements InteractableEntity {

    protected EquipmentSlots equipmentSlots = new EquipmentSlots();

    public NPC() {
        super(100, new StaticAiStateMachine());
        this.type = "netnpc";
    }

    @Override
    public void onDespawn() {
        super.onDespawn();

        // Remove the NPC
        try {
            GameNpcModel npcModel = DataService.npcModels.queryBuilder().where().eq("spawnedId", this.uuid).queryForFirst();
            if (npcModel != null) {
                npcModel.spawnedId = null;
                DataService.npcModels.createOrUpdate(npcModel);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
