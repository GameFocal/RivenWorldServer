package com.gamefocal.rivenworld.commands.net.combat;

import com.gamefocal.rivenworld.DedicatedServer;
import com.gamefocal.rivenworld.entites.net.*;
import com.gamefocal.rivenworld.game.foliage.FoliageIntractable;
import com.gamefocal.rivenworld.game.foliage.FoliageState;
import com.gamefocal.rivenworld.game.interactable.InteractAction;
import com.gamefocal.rivenworld.game.inventory.InventoryStack;
import com.gamefocal.rivenworld.game.items.weapons.Hatchet;
import com.gamefocal.rivenworld.game.items.weapons.MeleeWeapon;
import com.gamefocal.rivenworld.game.items.weapons.Spade;
import com.gamefocal.rivenworld.game.player.Animation;
import com.gamefocal.rivenworld.game.ray.HitResult;
import com.gamefocal.rivenworld.game.ray.hit.FoliageHitResult;
import com.gamefocal.rivenworld.game.util.Location;
import com.gamefocal.rivenworld.models.GameFoliageModel;
import com.gamefocal.rivenworld.service.DataService;
import com.gamefocal.rivenworld.service.FoliageService;

@Command(name = "nchq", sources = "tcp")
public class NetHitEntityQuickAttack extends HiveCommand {
    @Override
    public void onCommand(HiveNetMessage message, CommandSource source, HiveNetConnection netConnection) throws Exception {

        InventoryStack inHand = netConnection.getPlayer().equipmentSlots.getWeapon();
        if (inHand != null) {

            // Something is here

            HitResult hitResult = netConnection.getLookingAt();

            if (Hatchet.class.isAssignableFrom(inHand.getItem().getClass())) {

                // Is a Hatchet
                if (hitResult != null && FoliageHitResult.class.isAssignableFrom(hitResult.getClass())) {

                    // Looking at a tree or foliage

                    try {
                        FoliageHitResult foliageHitResult = (FoliageHitResult) hitResult;

                        String hash = FoliageService.getHash(foliageHitResult.getName(), foliageHitResult.getFoliageLocation().toString());

                        GameFoliageModel f = DataService.gameFoliage.queryForId(hash);
                        if (f == null) {

                            f = new GameFoliageModel();
                            f.uuid = hash;
                            f.modelName = foliageHitResult.getName();
                            f.foliageIndex = foliageHitResult.getIndex();
                            f.foliageState = FoliageState.GROWN;
                            f.health = DedicatedServer.get(FoliageService.class).getStartingHealth(foliageHitResult.getName());
                            f.growth = 100;
                            f.location = foliageHitResult.getFoliageLocation();

                            DataService.gameFoliage.createOrUpdate(f);

                            System.out.println("New Foliage Detected...");
                        }

                        FoliageIntractable foliageIntractable = new FoliageIntractable(f);
                        if (netConnection.getPlayer().equipmentSlots.getWeapon() != null) {
                            netConnection.getPlayer().equipmentSlots.getWeapon().getItem().onInteract(foliageIntractable, netConnection, InteractAction.HIT.setLocation(foliageHitResult.getHitLocation()));
                            netConnection.playAnimation(Animation.SWING_AXE);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }


            } else if (MeleeWeapon.class.isAssignableFrom(inHand.getItem().getClass())) {

                // Is a melee weapon

            } else if (Spade.class.isAssignableFrom(inHand.getItem().getClass())) {

                // Shovel

            }

        }

    }
}
