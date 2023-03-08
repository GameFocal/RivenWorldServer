package com.gamefocal.rivenworld.commands.net.combat;

import com.gamefocal.rivenworld.DedicatedServer;
import com.gamefocal.rivenworld.entites.net.*;
import com.gamefocal.rivenworld.game.entites.resources.ResourceNodeEntity;
import com.gamefocal.rivenworld.game.entites.resources.nodes.*;
import com.gamefocal.rivenworld.game.foliage.FoliageIntractable;
import com.gamefocal.rivenworld.game.foliage.FoliageState;
import com.gamefocal.rivenworld.game.interactable.InteractAction;
import com.gamefocal.rivenworld.game.inventory.InventoryStack;
import com.gamefocal.rivenworld.game.items.generics.ToolInventoryItem;
import com.gamefocal.rivenworld.game.items.generics.UsableInventoryItem;
import com.gamefocal.rivenworld.game.items.weapons.Hatchet;
import com.gamefocal.rivenworld.game.items.weapons.MeleeWeapon;
import com.gamefocal.rivenworld.game.items.weapons.Pickaxe;
import com.gamefocal.rivenworld.game.items.weapons.Spade;
import com.gamefocal.rivenworld.game.player.Animation;
import com.gamefocal.rivenworld.game.ray.HitResult;
import com.gamefocal.rivenworld.game.ray.hit.EntityHitResult;
import com.gamefocal.rivenworld.game.ray.hit.FoliageHitResult;
import com.gamefocal.rivenworld.game.ray.hit.PlayerHitResult;
import com.gamefocal.rivenworld.game.sounds.GameSounds;
import com.gamefocal.rivenworld.game.util.RandomUtil;
import com.gamefocal.rivenworld.models.GameFoliageModel;
import com.gamefocal.rivenworld.service.DataService;
import com.gamefocal.rivenworld.service.FoliageService;
import com.gamefocal.rivenworld.service.TaskService;

import java.util.LinkedList;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@Command(name = "nchq", sources = "tcp")
public class NetHitEntityQuickAttack extends HiveCommand {

    public static ConcurrentHashMap<UUID, Long> lastHit = new ConcurrentHashMap<>();

    @Override
    public void onCommand(HiveNetMessage message, CommandSource source, HiveNetConnection netConnection) throws Exception {

        System.out.println(message.toString());

        if (lastHit.containsKey(netConnection.getUuid())) {
            Long hitLast = lastHit.get(netConnection.getUuid());
            if (TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - hitLast) < 1) {
                return;
            }
        }

        lastHit.put(netConnection.getUuid(), System.currentTimeMillis());

        /*
         * Process in-hand call
         * */
        if (netConnection.getPlayer().equipmentSlots.inHand != null) {
            if (UsableInventoryItem.class.isAssignableFrom(netConnection.getPlayer().equipmentSlots.inHand.getItem().getClass())) {
                // Is a usable item
                UsableInventoryItem ui = (UsableInventoryItem) netConnection.getPlayer().equipmentSlots.inHand.getItem();
                if (ui.onUse(netConnection, netConnection.getLookingAt(), InteractAction.PRIMARY, netConnection.getPlayer().equipmentSlots.inHand)) {
                    return;
                }
            }
        }

        InventoryStack inHand = netConnection.getPlayer().equipmentSlots.inHand;
        HitResult hitResult = netConnection.getLookingAt();
        if (inHand != null) {

            // Something is here

            if (hitResult == null) {
                return;
            }

            if (Hatchet.class.isAssignableFrom(inHand.getItem().getClass())) {

                // Is a Hatchet
                if (FoliageHitResult.class.isAssignableFrom(hitResult.getClass())) {

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
                        if (netConnection.getPlayer().equipmentSlots.inHand != null) {
                            netConnection.getPlayer().equipmentSlots.inHand.getItem().onInteract(foliageIntractable, netConnection, InteractAction.HIT.setLocation(foliageHitResult.getHitLocation()));
//                            netConnection.playAnimation(Animation.SWING_AXE);
                            netConnection.updatePlayerInventory();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }


            } else if (MeleeWeapon.class.isAssignableFrom(inHand.getItem().getClass())) {

                // Is a melee weapon

            } else if (Pickaxe.class.isAssignableFrom(inHand.getItem().getClass())) {

                // Pickaxe

                if (EntityHitResult.class.isAssignableFrom(hitResult.getClass())) {

                    EntityHitResult entityHitResult = (EntityHitResult) hitResult;

                    LinkedList<Class<? extends ResourceNodeEntity>> allowed = new LinkedList<>();
                    allowed.add(RockNode.class);
                    allowed.add(IronNode.class);
                    allowed.add(CoalNode.class);
                    allowed.add(GoldNode.class);

                    if (allowed.contains(entityHitResult.get().getClass())) {

                        if (ToolInventoryItem.class.isAssignableFrom(inHand.getItem().getClass())) {

                            ToolInventoryItem inventoryItem = (ToolInventoryItem) inHand.getItem();

                            float hitValue = inventoryItem.hit();

                            ResourceNodeEntity nodeEntity = (ResourceNodeEntity) hitResult.get();

                            nodeEntity.health -= hitValue;

                            netConnection.playAnimation(Animation.PICKAXE);
                            TaskService.scheduledDelayTask(() -> {
                                DedicatedServer.instance.getWorld().playSoundAtLocation(GameSounds.PICKAXE_HIT, nodeEntity.location, 300, 1.5f, .5f);
                                netConnection.showFloatingTxt("-" + hitValue, entityHitResult.get().location.cpy().addZ(100));

                                if (nodeEntity.health <= 0) {

                                    for (InventoryStack s : nodeEntity.drops()) {
                                        netConnection.displayItemAdded(s);
                                        netConnection.getPlayer().inventory.add(s);
                                        netConnection.updatePlayerInventory();
                                    }

                                    DedicatedServer.instance.getWorld().despawn(nodeEntity.uuid);
//                                DedicatedServer.get(ResourceService.class).

                                } else {
                                    for (InventoryStack s : nodeEntity.drops()) {
                                        if (RandomUtil.getRandomChance(.5)) {
                                            netConnection.displayItemAdded(s);
                                            netConnection.getPlayer().inventory.add(s);
                                            netConnection.updatePlayerInventory();
                                        }
                                    }
                                }
                            }, 10L, false);

                        }

                    }

                }

            } else if (Spade.class.isAssignableFrom(inHand.getItem().getClass())) {

                // Spade

                if (EntityHitResult.class.isAssignableFrom(hitResult.getClass())) {

                    EntityHitResult entityHitResult = (EntityHitResult) hitResult;

                    LinkedList<Class<? extends ResourceNodeEntity>> allowed = new LinkedList<>();
                    allowed.add(DirtNode.class);
                    allowed.add(SandNode.class);
//                    allowed.add(CoalNode.class);
//                    allowed.add(GoldNode.class);

                    if (allowed.contains(entityHitResult.get().getClass())) {

                        if (ToolInventoryItem.class.isAssignableFrom(inHand.getItem().getClass())) {

                            ToolInventoryItem inventoryItem = (ToolInventoryItem) inHand.getItem();

                            float hitValue = inventoryItem.hit();

                            ResourceNodeEntity nodeEntity = (ResourceNodeEntity) hitResult.get();

                            nodeEntity.health -= hitValue;

                            netConnection.playAnimation(Animation.Digging);
                            TaskService.scheduledDelayTask(() -> {
                                DedicatedServer.instance.getWorld().playSoundAtLocation(GameSounds.FORAGE_SAND, nodeEntity.location, 300, 1.25f, .5f);
                                netConnection.showFloatingTxt("-" + hitValue, entityHitResult.get().location.cpy().addZ(100));

                                if (nodeEntity.health <= 0) {

                                    for (InventoryStack s : nodeEntity.drops()) {
                                        netConnection.displayItemAdded(s);
                                        netConnection.getPlayer().inventory.add(s);
                                    }

                                    DedicatedServer.instance.getWorld().despawn(nodeEntity.uuid);
//                                DedicatedServer.get(ResourceService.class).

                                } else {
                                    for (InventoryStack s : nodeEntity.drops()) {
                                        if (RandomUtil.getRandomChance(.5)) {
                                            netConnection.displayItemAdded(s);
                                            netConnection.getPlayer().inventory.add(s);
                                        }
                                    }
                                }
                            }, 10L, false);
                        }

                    }

                }

            }


        } else {
            netConnection.playAnimation(Animation.PUNCH);
            if (hitResult != null) {
                if (PlayerHitResult.class.isAssignableFrom(hitResult.getClass())) {
                    PlayerHitResult playerHitResult = (PlayerHitResult) hitResult;
                    HiveNetConnection connection = playerHitResult.get();
                    connection.takeDamage(1);
                }
            }
        }
    }
}
