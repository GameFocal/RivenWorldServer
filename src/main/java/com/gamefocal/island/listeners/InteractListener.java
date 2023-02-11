package com.gamefocal.island.listeners;

import com.gamefocal.island.entites.events.EventHandler;
import com.gamefocal.island.entites.events.EventInterface;
import com.gamefocal.island.events.game.ServerWorldSyncEvent;
import com.gamefocal.island.game.GameEntity;
import com.gamefocal.island.game.InteractableEntity;
import com.gamefocal.island.game.ray.HitResult;
import com.gamefocal.island.game.ray.hit.EntityHitResult;
import com.gamefocal.island.game.ray.hit.FoliageHitResult;
import com.gamefocal.island.game.ray.hit.PlayerHitResult;
import com.gamefocal.island.game.ray.hit.TerrainHitResult;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class InteractListener implements EventInterface {

    private static ConcurrentHashMap<UUID, HitResult> lastHitResult = new ConcurrentHashMap<>();

    @EventHandler
    public void onWorldSyncEvent(ServerWorldSyncEvent event) {

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

                // TODO: Return to show the tooltip
//                event.getConnection().showCursorToolTipText("[e] Forage Tree");

            }
        } else {
            event.getConnection().hideCursorToolTipText();
        }

    }

}
