package com.gamefocal.rivenworld.game.entites.resources.nodes;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.entites.resources.ResourceNodeEntity;
import com.gamefocal.rivenworld.game.inventory.InventoryStack;
import com.gamefocal.rivenworld.game.items.resources.minerals.raw.GoldOre;
import com.gamefocal.rivenworld.game.items.resources.minerals.raw.IronOre;
import com.gamefocal.rivenworld.game.items.resources.minerals.raw.Stone;
import com.gamefocal.rivenworld.game.items.weapons.Pickaxe;

public class IronNode extends ResourceNodeEntity<IronNode> {

    public IronNode() {
        this.type = "Iron-node";
        this.allowedTools.add(Pickaxe.class);
    }

    @Override
    public void onSpawn() {

    }

    @Override
    public void onDespawn() {

    }

    @Override
    public void onTick() {

    }

    @Override
    public String helpText(HiveNetConnection connection) {
        if (connection.getInHand() != null && !this.allowedTools.contains(connection.getInHand().getItem().getClass())) {
            return "Use a pickaxe to mine iron";
        }
        return null;
    }

    @Override
    public InventoryStack[] drops() {
        return new InventoryStack[]{
                new InventoryStack(new IronOre(), 5),
                new InventoryStack(new Stone(), 5)
        };
    }
}
