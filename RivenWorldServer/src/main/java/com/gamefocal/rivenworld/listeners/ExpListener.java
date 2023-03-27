package com.gamefocal.rivenworld.listeners;

import com.gamefocal.rivenworld.entites.events.EventHandler;
import com.gamefocal.rivenworld.entites.events.EventInterface;
import com.gamefocal.rivenworld.events.combat.PlayerDealDamageEvent;
import com.gamefocal.rivenworld.events.crafting.CraftItemEvent;
import com.gamefocal.rivenworld.events.player.PlayerDeathEvent;
import com.gamefocal.rivenworld.events.resources.DestroyResourceNodeEvent;
import com.gamefocal.rivenworld.events.resources.PlayerForageEvent;
import com.gamefocal.rivenworld.game.entites.generics.CraftingStation;
import com.gamefocal.rivenworld.game.entites.stations.*;
import com.gamefocal.rivenworld.game.inventory.InventoryStack;
import com.gamefocal.rivenworld.game.items.generics.ToolInventoryItem;
import com.gamefocal.rivenworld.game.items.resources.minerals.raw.Stone;
import com.gamefocal.rivenworld.game.items.resources.minerals.refined.StoneBrick;
import com.gamefocal.rivenworld.game.items.resources.wood.WoodLog;
import com.gamefocal.rivenworld.game.items.resources.wood.WoodPlank;
import com.gamefocal.rivenworld.game.items.resources.wood.WoodStick;
import com.gamefocal.rivenworld.game.items.weapons.Bow;
import com.gamefocal.rivenworld.game.items.weapons.Hatchet;
import com.gamefocal.rivenworld.game.items.weapons.MeleeWeapon;
import com.gamefocal.rivenworld.game.items.weapons.RangedWeapon;
import com.gamefocal.rivenworld.game.items.weapons.sword.IronSword;
import com.gamefocal.rivenworld.game.skills.skillTypes.*;
import com.gamefocal.rivenworld.game.ui.inventory.RivenCraftingUI;
import com.gamefocal.rivenworld.game.ui.inventory.RivenInventoryUI;
import com.gamefocal.rivenworld.service.SkillService;

public class ExpListener implements EventInterface {

    @EventHandler
    public void onCraftItemEvent(CraftItemEvent event) {
        if (RivenInventoryUI.class.isAssignableFrom(event.getUi().getClass())) {
            // From the player inventory

            // TODO: Primitive Skill

        } else if (RivenCraftingUI.class.isAssignableFrom(event.getUi().getClass())) {
            // Crafting Bench

            RivenCraftingUI rivenCraftingUI = (RivenCraftingUI) event.getUi();

            CraftingStation station = rivenCraftingUI.getAttached();

            if (WorkBenchPlaceable.class.isAssignableFrom(station.getClass())) {
                // Work Bench

//                SkillService.addExp(event.getConnection(),);
                // TODO: Primitive Skill

            } else if (MasonBench.class.isAssignableFrom(station.getClass())) {
                // Mason

                SkillService.addExp(event.getConnection(), MasonrySkill.class, 5);

            } else if (CampFirePlaceableCrafting.class.isAssignableFrom(station.getClass())) {
                // Campfire

                // TODO: Cooking

            } else if (FurnacePlaceableCrafting.class.isAssignableFrom(station.getClass())) {
                // Furnace

                // TODO: Metal Working Skill

            } else if (ForgePlaceableCrafting.class.isAssignableFrom(station.getClass())) {
                // Forage

                // TODO: Metal Working Skill

            }

        }
    }

    @EventHandler
    public void onPlayerDealDamageEvent(PlayerDealDamageEvent damageEvent) {
        // TODO: Combat Exp added
        InventoryStack inHand = damageEvent.getPlayer().getPlayer().equipmentSlots.inHand;

        if (ToolInventoryItem.class.isAssignableFrom(inHand.getItem().getClass())) {
            SkillService.addExp(damageEvent.getPlayer(), OneHandedCombatSkill.class, 2);
        } else if (MeleeWeapon.class.isAssignableFrom(inHand.getItem().getClass())) {
            SkillService.addExp(damageEvent.getPlayer(), OneHandedCombatSkill.class, Math.max(1, damageEvent.getDamage() / 16));
        } else if (Bow.class.isAssignableFrom(inHand.getItem().getClass())) {
            SkillService.addExp(damageEvent.getPlayer(), LongBowSkill.class, Math.max(1, damageEvent.getDamage() / 16));
        }

        // TODO: Add addtional weapon types here

    }

    // TODO: Add a defend event here and hook up defending

    @EventHandler
    public void onPlayerDeathEvent(PlayerDeathEvent deathEvent) {
        // TODO: Loose exp
    }

    @EventHandler
    public void onResourceNodeDestoryEvent(DestroyResourceNodeEvent event) {
        // TODO: Mining Skills
        SkillService.addExp(event.getBy(), MiningSkill.class, 1);
    }

    @EventHandler
    public void onForageEvent(PlayerForageEvent event) {
        // TODO: Forage Skills
        SkillService.addExp(event.getConnection(), ForagingSkill.class, 1);
    }

}
