package com.gamefocal.rivenworld.listeners;

import com.gamefocal.rivenworld.DedicatedServer;
import com.gamefocal.rivenworld.entites.events.EventHandler;
import com.gamefocal.rivenworld.entites.events.EventInterface;
import com.gamefocal.rivenworld.entites.events.EventPriority;
import com.gamefocal.rivenworld.events.combat.PlayerDealDamageEvent;
import com.gamefocal.rivenworld.game.WorldChunk;
import com.gamefocal.rivenworld.game.combat.PlayerHitDamage;
import com.gamefocal.rivenworld.models.GameChunkModel;
import com.gamefocal.rivenworld.service.EnvironmentService;

public class PvPListener implements EventInterface {

    @EventHandler(priority = EventPriority.FIRST)
    public void onPlayerAttackEvent(PlayerDealDamageEvent event) {
        if (PlayerHitDamage.class.isAssignableFrom(event.getHitDamage().getClass())) {
            // A player vs player hit

            PlayerHitDamage playerHitDamage = (PlayerHitDamage) event.getHitDamage();

            // PvP is off
            if (DedicatedServer.settings.pvpMode.equalsIgnoreCase("off")) {
                event.setCanceled(true);
                return;
            }

            /*
             * Protect the player in their claim
             * */
            if (DedicatedServer.settings.protectInClaim) {
                WorldChunk inChunk = DedicatedServer.instance.getWorld().getChunk(event.getPlayer().getPlayer().location);
                if (inChunk != null) {
                    GameChunkModel model = inChunk.getModel();
                    if (model != null) {
                        if (model.claim != null) {
                            if (model.claim.owner.uuid.equalsIgnoreCase(event.getPlayer().getPlayer().uuid)) {
                                event.setCanceled(true);
                                return;
                            }

                            if (model.claim.owner.guild != null && event.getPlayer().getPlayer().guild != null && model.claim.owner.guild.id == event.getPlayer().getPlayer().guild.id) {
                                event.setCanceled(true);
                                return;
                            }
                        }
                    }
                }
            }

            if (DedicatedServer.settings.pvpMode.equalsIgnoreCase("night-only") && DedicatedServer.get(EnvironmentService.class).isDay) {
                event.setCanceled(true);
                return;
            }

            event.setDamage(event.getDamage() * DedicatedServer.settings.damageMultiple);

            if (DedicatedServer.settings.pvpMode.equalsIgnoreCase("night") && DedicatedServer.get(EnvironmentService.class).isDay) {
                // Nerf during the day
                event.setDamage((float) (event.getDamage() * .25));
            }

            playerHitDamage.getA().markInCombat();
            playerHitDamage.getB().markInCombat();
        }
    }

}
