package com.gamefocal.rivenworld.game.items.resources.animals;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.interactable.InteractAction;
import com.gamefocal.rivenworld.game.interactable.Intractable;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.items.generics.ConsumableInventoryItem;
import com.gamefocal.rivenworld.game.util.RandomUtil;
import com.gamefocal.rivenworld.game.util.TickUtil;
import com.gamefocal.rivenworld.service.TaskService;

public class RawRedMeat extends ConsumableInventoryItem {
    public RawRedMeat() {
        this.icon = InventoryDataRow.Raw_Meat;
        this.mesh = InventoryDataRow.Raw_Meat;
        this.name = "Raw Meat";
        this.desc = "Meat fresh off the bone of a animal";
    }

    @Override
    public float onConsume(HiveNetConnection connection) {
        if (connection != null) {
            connection.getPlayer().playerStats.health += 5;
            if (RandomUtil.getRandomChance(.5)) {
                // Sick
                TaskService.scheduleRepeatingLimitedTask(() -> {
                    connection.takeDamage(10);
                }, TickUtil.MINUTES(1), TickUtil.MINUTES(1), 15, false);
            }
        }

        return 10;
    }

    @Override
    public void onInteract(Intractable intractable, HiveNetConnection connection, InteractAction action) {

    }
}
