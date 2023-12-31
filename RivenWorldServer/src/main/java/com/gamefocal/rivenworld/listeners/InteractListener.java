package com.gamefocal.rivenworld.listeners;

import com.gamefocal.rivenworld.DedicatedServer;
import com.gamefocal.rivenworld.entites.events.EventHandler;
import com.gamefocal.rivenworld.entites.events.EventInterface;
import com.gamefocal.rivenworld.events.game.ServerWorldSyncEvent;
import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.InteractableEntity;
import com.gamefocal.rivenworld.game.world.WorldChunk;
import com.gamefocal.rivenworld.game.inventory.InventoryStack;
import com.gamefocal.rivenworld.game.items.generics.UsableInventoryItem;
import com.gamefocal.rivenworld.game.ray.HitResult;
import com.gamefocal.rivenworld.game.ray.hit.*;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class InteractListener implements EventInterface {

    private static ConcurrentHashMap<UUID, HitResult> lastHitResult = new ConcurrentHashMap<>();

    @EventHandler
    public void onWorldSyncEvent(ServerWorldSyncEvent event) {

        InventoryStack inHand = event.getConnection().getPlayer().equipmentSlots.inHand;

        event.getConnection().clearUpperRightHelptext();
        if (inHand != null) {
            inHand.getItem().upperRightText.clear();
            inHand.getItem().generateUpperRightHelpText();
            if (inHand.getItem().upperRightText.size() > 0) {

                StringBuilder builder = new StringBuilder();
                for (String s : inHand.getItem().upperRightText) {
                    builder.append(s).append("&&");
                }

                event.getConnection().setUpperRightHelpText(builder.toString());
            }
        }

        event.getConnection().setHelpboxText(null);
        if (event.getConnection().getPlayer().equipmentSlots.inHand != null && UsableInventoryItem.class.isAssignableFrom(event.getConnection().getPlayer().equipmentSlots.inHand.getItem().getClass())) {
            // Has a usable item in-hand

            UsableInventoryItem ui = (UsableInventoryItem) event.getConnection().getPlayer().equipmentSlots.inHand.getItem();

            String helpBox = ui.inHandTip(event.getConnection(), event.getConnection().getLookingAt());

            if (helpBox != null) {
//                System.out.println(helpBox);
                event.getConnection().setHelpboxText(helpBox);
            }
        }

        if (event.getConnection().getLookingAt() != null) {
            HitResult looking = event.getConnection().getLookingAt();

            if (EntityHitResult.class.isAssignableFrom(looking.getClass())) {
                // Is a entity

                EntityHitResult r = (EntityHitResult) looking;

                GameEntity e = r.get();

                if (e != null) {
                    // Is a valid entity
                    if (InteractableEntity.class.isAssignableFrom(e.getClass())) {
                        // Has interaction

                        if (e.helpText(event.getConnection()) != null) {
                            event.getConnection().setHelpboxText(e.helpText(event.getConnection()));
                        }

                        // Check for interact perms
                        WorldChunk chunk = DedicatedServer.instance.getWorld().getChunk(e.location);
                        if (chunk != null) {
                            if (!chunk.canInteract(event.getConnection())) {
                                event.setCanceled(true);
                                return;
                            }
                        }

                        if (((InteractableEntity) e).canInteract(event.getConnection())) {
                            // Can interact
                            String t = ((InteractableEntity) e).onFocus(event.getConnection());
                            event.getConnection().showCursorToolTipText(t);
                        }
                    }
                }

            } else if (FoliageHitResult.class.isAssignableFrom(looking.getClass())) {
                // Is Foliage

                // TODO: Return to show the interact tooltip
                event.getConnection().showCursorToolTipText("[e] Forage");

            } else if (TerrainHitResult.class.isAssignableFrom(looking.getClass())) {
                // Is Terrain

                // TODO: Return to show the interact tooltip
                event.getConnection().showCursorToolTipText("[e] Forage");

            } else if (PlayerHitResult.class.isAssignableFrom(looking.getClass())) {
                // Is another player

//                PlayerHitResult ph = (PlayerHitResult) looking;

                event.getConnection().setHelpboxText("[q] Player Actions");

                // TODO: Return to show the tooltip
//                event.getConnection().showCursorToolTipText(ph.get().getPlayer().displayName + " | [q] To Interact");
            }
//            else if (WaterHitResult.class.isAssignableFrom(looking.getClass())) {
//                event.getConnection().showCursorToolTipText("Hold a container in your hand to gather water");
//            }
        } else {
            event.getConnection().hideCursorToolTipText();
        }

    }

}
