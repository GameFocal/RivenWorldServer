package com.gamefocal.rivenworld.game.entites.resources.nodes;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.entites.resources.ResourceNodeEntity;
import com.gamefocal.rivenworld.game.inventory.InventoryStack;
import com.gamefocal.rivenworld.game.items.resources.minerals.raw.GoldOre;
import com.gamefocal.rivenworld.game.items.resources.minerals.raw.Stone;
import com.gamefocal.rivenworld.game.items.weapons.Pickaxe;

public class GoldNode extends ResourceNodeEntity<GoldNode> {

    public GoldNode() {
        this.type = "Gold-node";
        this.allowedTools.add(Pickaxe.class);
    }

    @Override
    public void onSpawn() {

    }

    @Override
    public String helpText(HiveNetConnection connection) {
        if (connection.getInHand() != null && !this.allowedTools.contains(connection.getInHand().getItem().getClass())) {
            return "Use a pickaxe to mine gold";
        }
        return null;
    }

    @Override
    public void onDespawn() {

    }

    @Override
    public void onTick() {

    }

    @Override
    public InventoryStack[] drops() {
        if (this.health <= 10) {
            return new InventoryStack[]{
                    new InventoryStack(new GoldOre(), 4)
//                new InventoryStack(new Stone(), 5)
            };
        } else {
            return new InventoryStack[]{
                    new InventoryStack(new Stone(), 3)
//                new InventoryStack(new Stone(), 5)
            };
        }
    }
}
