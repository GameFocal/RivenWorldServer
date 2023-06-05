package com.gamefocal.rivenworld.listeners;

import com.gamefocal.rivenworld.entites.events.EventHandler;
import com.gamefocal.rivenworld.entites.events.EventInterface;
import com.gamefocal.rivenworld.events.combat.PlayerBlockEvent;
import com.gamefocal.rivenworld.events.combat.PlayerDealDamageEvent;
import com.gamefocal.rivenworld.events.crafting.CraftItemEvent;
import com.gamefocal.rivenworld.events.player.PlayerDeathEvent;
import com.gamefocal.rivenworld.events.resources.DestroyResourceNodeEvent;
import com.gamefocal.rivenworld.events.resources.PlayerForageEvent;
import com.gamefocal.rivenworld.game.entites.generics.CraftingStation;
import com.gamefocal.rivenworld.game.entites.stations.*;
import com.gamefocal.rivenworld.game.inventory.InventoryStack;
import com.gamefocal.rivenworld.game.items.generics.ToolInventoryItem;
import com.gamefocal.rivenworld.game.items.weapons.Bow;
import com.gamefocal.rivenworld.game.items.weapons.MeleeWeapon;
import com.gamefocal.rivenworld.game.skills.skillTypes.*;
import com.gamefocal.rivenworld.game.ui.inventory.RivenCraftingUI;
import com.gamefocal.rivenworld.game.ui.inventory.RivenInventoryUI;
import com.gamefocal.rivenworld.service.SkillService;

public class ExpListener implements EventInterface {

    @EventHandler
    public void onCraftItemEvent(CraftItemEvent event) {
        if (RivenInventoryUI.class.isAssignableFrom(event.getUi().getClass())) {
            // From the player inventory
            SkillService.addExp(event.getConnection(), CraftingSkill.class, 5);
        } else if (RivenCraftingUI.class.isAssignableFrom(event.getUi().getClass())) {
            // Crafting Bench

            RivenCraftingUI rivenCraftingUI = (RivenCraftingUI) event.getUi();

            CraftingStation station = rivenCraftingUI.getAttached();

            if (WorkBenchPlaceable.class.isAssignableFrom(station.getClass())) {
                // Work Bench
                SkillService.addExp(event.getConnection(), CraftingSkill.class, 5);
            } else if (MasonBench.class.isAssignableFrom(station.getClass())) {
                // Mason
                SkillService.addExp(event.getConnection(), MasonrySkill.class, 5);
            } else if (CampFirePlaceableCrafting.class.isAssignableFrom(station.getClass())) {
                // Campfire
                SkillService.addExp(event.getConnection(), CookingSkill.class, 5);
            } else if (FurnacePlaceableCrafting.class.isAssignableFrom(station.getClass())) {
                // Furnace
                SkillService.addExp(event.getConnection(), MetalWorkingSkill.class, 5);
            } else if (ForgePlaceableCrafting.class.isAssignableFrom(station.getClass())) {
                // Forage
                SkillService.addExp(event.getConnection(), SmeltingSkill.class, 5);
            }

        }
    }

    @EventHandler
    public void onPlayerDealDamageEvent(PlayerDealDamageEvent damageEvent) {
        // TODO: Combat Exp added
        InventoryStack inHand = damageEvent.getPlayer().getPlayer().equipmentSlots.inHand;

        if (MeleeWeapon.class.isAssignableFrom(inHand.getItem().getClass())) {
            if (inHand.getItem().hasTag("weapon")) {
                if (inHand.getItem().tagEquals("weapon", "spear")) {
                    // Spear
                    SkillService.addExp(damageEvent.getPlayer(), SpearCombatSkill.class, Math.max(1, damageEvent.getDamage() / 16));
                } else if (inHand.getItem().tagEquals("weapon", "oneHand")) {
                    // One Handed
                    SkillService.addExp(damageEvent.getPlayer(), OneHandedCombatSkill.class, Math.max(1, damageEvent.getDamage() / 16));
                } else if (inHand.getItem().tagEquals("weapon", "twoHand")) {
                    // Two Handed
                    SkillService.addExp(damageEvent.getPlayer(), TwoHandedCombatSkill.class, Math.max(1, damageEvent.getDamage() / 16));
                } else {
                    SkillService.addExp(damageEvent.getPlayer(), OneHandedCombatSkill.class, Math.max(1, damageEvent.getDamage() / 16));
                }
            } else {
                SkillService.addExp(damageEvent.getPlayer(), OneHandedCombatSkill.class, Math.max(1, damageEvent.getDamage() / 16));
            }
        } else if (ToolInventoryItem.class.isAssignableFrom(inHand.getItem().getClass())) {
            SkillService.addExp(damageEvent.getPlayer(), OneHandedCombatSkill.class, 2);
        } else if (Bow.class.isAssignableFrom(inHand.getItem().getClass())) {
            SkillService.addExp(damageEvent.getPlayer(), LongBowSkill.class, Math.max(1, damageEvent.getDamage() / 16));
        }
    }

    @EventHandler
    public void onDefendEvent(PlayerBlockEvent blockEvent) {
        SkillService.addExp(blockEvent.getConnection(), BlockingSkill.class, 5);
    }

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
