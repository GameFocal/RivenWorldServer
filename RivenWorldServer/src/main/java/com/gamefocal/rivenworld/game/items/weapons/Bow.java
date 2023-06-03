package com.gamefocal.rivenworld.game.items.weapons;

import com.gamefocal.rivenworld.DedicatedServer;
import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.interactable.InteractAction;
import com.gamefocal.rivenworld.game.interactable.Intractable;
import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.inventory.InventoryCraftingInterface;
import com.gamefocal.rivenworld.game.inventory.InventoryStack;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.items.ammo.IronArrow;
import com.gamefocal.rivenworld.game.items.ammo.SteelArrow;
import com.gamefocal.rivenworld.game.items.ammo.StoneArrow;
import com.gamefocal.rivenworld.game.items.ammo.WoodenArrow;
import com.gamefocal.rivenworld.game.items.generics.AmmoInventoryItem;
import com.gamefocal.rivenworld.game.items.generics.UsableInventoryItem;
import com.gamefocal.rivenworld.game.ray.HitResult;
import com.gamefocal.rivenworld.game.recipes.weapons.BasicBowRecipe;
import com.gamefocal.rivenworld.game.ui.UIIcon;
import com.gamefocal.rivenworld.game.ui.radialmenu.RadialMenuOption;
import com.gamefocal.rivenworld.service.CombatService;

import java.util.ArrayList;
import java.util.List;

public class Bow extends RangedWeapon implements InventoryCraftingInterface, UsableInventoryItem {

    public Bow() {
        this.icon = InventoryDataRow.Basic_Bow;
        this.mesh = InventoryDataRow.Basic_Bow;
        this.name = "Basic Bow";
        this.desc = "A Basic Bow made of string and wood";
        this.spawnNames.add("basicbow");
        this.initDurability(200);
        this.ammoTypes.push(WoodenArrow.class);
        this.ammoTypes.push(StoneArrow.class);
        this.ammoTypes.push(IronArrow.class);
        this.ammoTypes.push(SteelArrow.class);
    }


    @Override
    public void onInteract(Intractable intractable, HiveNetConnection connection, InteractAction action) {
    }

    @Override
    public CraftingRecipe canCraft(HiveNetConnection connection) {
        return new BasicBowRecipe();
    }

    @Override
    public String inHandTip(HiveNetConnection connection, HitResult hitResult) {

        int ammoAmount = 0;
        CombatService combatService = DedicatedServer.get(CombatService.class);

        if (!combatService.hasAmmo(connection, this)) {
            return "Add arrows to your inventory to use bow";
        }

        if (connection.selectedAmmo == null) {
            return "Press [Q] to select your arrow type";
        }

        ammoAmount = combatService.getAmountCountOfType(connection, connection.selectedAmmo);

        return (ammoAmount + " " + connection.selectedAmmo.getClass().getSimpleName());
//        return null;
    }

    @Override
    public boolean onUse(HiveNetConnection connection, HitResult hitResult, InteractAction action, InventoryStack inHand) {
        if (action == InteractAction.ALT) {
            // Trigger the wheel

            CombatService combatService = DedicatedServer.get(CombatService.class);

            if (combatService.hasAmmo(connection, this)) {
                List<RadialMenuOption> opts = new ArrayList<>();

//                for (InventoryStack s : combatService.getAmmoAmounts(connection)) {
//                    opts.add(
//                            new RadialMenuOption(s.getItem().getName(), s.getItem().getClass().getSimpleName(), UIIcon.LONG_BOW)
//                    );
//                }

                for (Class<? extends AmmoInventoryItem> a : this.ammoTypes) {
                    int amt = connection.getPlayer().inventory.getOfType(a).size();
                    if (amt > 0) {
                        opts.add(
                                new RadialMenuOption(a.getSimpleName(), a.getSimpleName(), UIIcon.LONG_BOW)
                        );
                    }
                }

                connection.openRadialMenu(action1 -> {
                    for (Class<? extends AmmoInventoryItem> t : this.ammoTypes) {
                        if (t.getSimpleName().equalsIgnoreCase(action1)) {
                            // is a allowed ammo

                            connection.selectedAmmo = t;
                            break;

                        }
                    }
                }, opts);
            }
        }

        return false;
    }
}
