package com.gamefocal.rivenworld.game.entites.resources.nodes;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.entites.resources.ResourceNodeEntity;
import com.gamefocal.rivenworld.game.inventory.InventoryStack;
import com.gamefocal.rivenworld.game.items.placables.blocks.SandBlockItem;
import com.gamefocal.rivenworld.game.items.resources.minerals.raw.GoldOre;
import com.gamefocal.rivenworld.game.items.resources.minerals.raw.Stone;
import com.gamefocal.rivenworld.game.items.weapons.Pickaxe;
import com.gamefocal.rivenworld.game.items.weapons.Spade;
import com.gamefocal.rivenworld.game.items.weapons.Wood_Spade;
import com.gamefocal.rivenworld.game.player.Animation;

public class SandNode extends ResourceNodeEntity<SandNode> {

    public SandNode() {
        this.type = "Sand-node";
        this.allowedTools.add(Pickaxe.class);
        this.hitAnimation = Animation.PICKAXE;
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
            return "Use a pickaxe to mine sandstone";
        }
        return null;
    }

    @Override
    public InventoryStack[] drops() {
        return new InventoryStack[]{
                new InventoryStack(new SandBlockItem(), 2),
                new InventoryStack(new Stone(), 3)
        };
    }
}
