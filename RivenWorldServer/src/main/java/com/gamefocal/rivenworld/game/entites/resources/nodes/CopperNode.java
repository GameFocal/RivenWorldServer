package com.gamefocal.rivenworld.game.entites.resources.nodes;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.entites.resources.ResourceNodeEntity;
import com.gamefocal.rivenworld.game.inventory.InventoryStack;
import com.gamefocal.rivenworld.game.items.resources.minerals.raw.CopperOre;
import com.gamefocal.rivenworld.game.items.resources.minerals.raw.GoldOre;
import com.gamefocal.rivenworld.game.items.resources.minerals.raw.Stone;
import com.gamefocal.rivenworld.game.items.resources.minerals.refined.CopperIgnot;
import com.gamefocal.rivenworld.game.items.resources.misc.Oil;
import com.gamefocal.rivenworld.game.items.weapons.Pickaxe;

public class CopperNode extends ResourceNodeEntity<CopperNode> {

    public CopperNode() {
        this.type = "copper-node";
        this.allowedTools.add(Pickaxe.class);
    }

    @Override
    public void onSpawn() {

    }

    @Override
    public String helpText(HiveNetConnection connection) {
        if (connection.getInHand() != null && !this.allowedTools.contains(connection.getInHand().getItem().getClass())) {
            return "Use a pickaxe to mine copper";
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
        return new InventoryStack[]{
                new InventoryStack(new CopperOre(), 15),
                new InventoryStack(new Stone(), 5)
        };
    }
}
