package com.gamefocal.rivenworld.game.items.weapons.hoe;

import com.badlogic.gdx.graphics.Color;
import com.gamefocal.rivenworld.DedicatedServer;
import com.gamefocal.rivenworld.entites.net.ChatColor;
import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.ai.path.WorldCell;
import com.gamefocal.rivenworld.game.entites.crops.CropEntity;
import com.gamefocal.rivenworld.game.interactable.InteractAction;
import com.gamefocal.rivenworld.game.interactable.Intractable;
import com.gamefocal.rivenworld.game.inventory.InventoryItem;
import com.gamefocal.rivenworld.game.inventory.InventoryStack;
import com.gamefocal.rivenworld.game.inventory.enums.EquipmentSlot;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryItemType;
import com.gamefocal.rivenworld.game.items.generics.EquipmentItem;
import com.gamefocal.rivenworld.game.items.generics.ToolInventoryItem;
import com.gamefocal.rivenworld.game.items.generics.UsableInventoryItem;
import com.gamefocal.rivenworld.game.player.AnimSlot;
import com.gamefocal.rivenworld.game.player.Animation;
import com.gamefocal.rivenworld.game.ray.HitResult;
import com.gamefocal.rivenworld.game.ray.hit.TerrainHitResult;
import com.gamefocal.rivenworld.game.sounds.GameSounds;
import com.gamefocal.rivenworld.game.world.LandscapeType;
import com.gamefocal.rivenworld.game.world.WorldMetaData;
import com.gamefocal.rivenworld.service.FarmingService;
import com.gamefocal.rivenworld.service.TaskService;

public class WoodenHoe extends ToolInventoryItem implements UsableInventoryItem {

    public WoodenHoe() {
        this.isEquipable = true;
        this.type = InventoryItemType.PRIMARY;
        this.tag("weapon", "oneHand");
        this.icon = InventoryDataRow.Hoe;
        this.mesh = InventoryDataRow.Hoe;
        this.name = "Wooden Hoe";
        this.desc = "A tool to till soil to plant seeds";
        this.initDurability(200);
        this.spawnNames.add("hoe");
    }

    @Override
    public void generateUpperRightHelpText() {
        this.upperRightText.add("[LMB] Till Soil");
        super.generateUpperRightHelpText();
    }

    @Override
    public float hit() {
        return 5;
    }

    @Override
    public float block() {
        return 0;
    }

    @Override
    public void onInteract(Intractable intractable, HiveNetConnection connection, InteractAction action) {

    }

    @Override
    public String inHandTip(HiveNetConnection connection, HitResult hitResult) {
        if (hitResult != null && TerrainHitResult.class.isAssignableFrom(hitResult.getClass())) {
            return "[LMB] To till the soil";
        }
        return "Look at terrain to till the soil";
    }

    @Override
    public boolean onUse(HiveNetConnection connection, HitResult hitResult, InteractAction action, InventoryStack inHand) {

        if (hitResult != null && TerrainHitResult.class.isAssignableFrom(hitResult.getClass())) {

            TerrainHitResult terrainHitResult = (TerrainHitResult) hitResult;

            WorldCell cell = DedicatedServer.instance.getWorld().getGrid().getCellFromGameLocation(connection.getLookingAtTerrain());
            WorldMetaData metaData = DedicatedServer.instance.getWorld().getRawHeightmap().getMetaDataFromXY(cell.getX(), cell.getY());
//
//            if (metaData != null) {
//                System.out.println(metaData.getForest());
//                System.out.println(metaData.getLandscapeType());
//            }

            if (cell.getCenterInGameSpace(true).dist(connection.getPlayer().location) > 400) {
                return false;
            }

            if (!FarmingService.canFarm(cell)) {
                connection.sendChatMessage(ChatColor.RED + " You can not till this soil");
                return true;
            }

            /*
             * Durability Check
             * */
            if (!this.canUseDurabilityOrBreak(5)) {
                connection.breakItemInSlot(EquipmentSlot.PRIMARY);
                return true;
            }
            connection.updatePlayerInventory();

            connection.playAnimation(Animation.PICKAXE, "DefaultSlot", 1, 0, -1, 0.25f, 0.25f, true);
            DedicatedServer.instance.getWorld().playSoundAtLocation(GameSounds.FORAGE_DIRT, cell.getGameLocation(), 5000, 1, 1, 2);
            TaskService.schedulePlayerInterruptTask(() -> {
                /*
                 * Spawn the soil entity
                 * */
                DedicatedServer.instance.getWorld().playSoundAtLocation(GameSounds.PLACE_ITEM, cell.getGameLocation(), 5000, 1, 1);
//                CropEntity cropEntity = new CropEntity();
//                DedicatedServer.instance.getWorld().spawn(cropEntity, cell.getCenterInGameSpace(true).cpy().addZ(-10));
            }, 2L, "Tilling Soil", Color.BLUE, connection, () -> {
                connection.cancelPlayerAnimation();
            });

            return true;
        }

        return false;
    }
}
