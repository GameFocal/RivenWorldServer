package com.gamefocal.rivenworld.listeners;

import com.gamefocal.rivenworld.DedicatedServer;
import com.gamefocal.rivenworld.entites.events.EventHandler;
import com.gamefocal.rivenworld.entites.events.EventInterface;
import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.events.combat.PlayerDealDamageEvent;
import com.gamefocal.rivenworld.events.entity.ProjectileMoveEvent;
import com.gamefocal.rivenworld.events.player.PlayerStateSyncEvent;
import com.gamefocal.rivenworld.game.items.ammo.IronArrow;
import com.gamefocal.rivenworld.game.items.ammo.SteelArrow;
import com.gamefocal.rivenworld.game.items.ammo.StoneArrow;
import com.gamefocal.rivenworld.game.items.ammo.WoodenArrow;
import com.gamefocal.rivenworld.game.items.weapons.RangedWeapon;
import com.gamefocal.rivenworld.game.player.PlayerBlendState;
import com.gamefocal.rivenworld.game.sounds.GameSounds;
import com.gamefocal.rivenworld.service.CombatService;
import com.gamefocal.rivenworld.service.PlayerService;

import java.util.LinkedList;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class BowEffectListener implements EventInterface {

    public static LinkedList<UUID> hasDrawnBow = new LinkedList<>();
    public static ConcurrentHashMap<UUID, LinkedList<UUID>> wizPlayed = new ConcurrentHashMap<>();

    @EventHandler
    public void onWorldSyncEvent(PlayerStateSyncEvent event) {
        /*
         * Check to see if bow is drawn
         * */
        HiveNetConnection connection = event.getConnection();

        PlayerBlendState state = connection.getState().blendState;
        if (state.hasBow && state.isAiming) {
            if (state.BlendWUpper >= 1 && !hasDrawnBow.contains(connection.getUuid())) {
                DedicatedServer.instance.getWorld().playSoundAtLocation(GameSounds.BOW_DRAW, connection.getPlayer().location, 1500, 1, 1);
                hasDrawnBow.add(connection.getUuid());
            } else if (state.BlendWUpper == 0) {
                hasDrawnBow.remove(connection.getUuid());
            }
        }
    }

    @EventHandler
    public void onProjectileMoveEvent(ProjectileMoveEvent moveEvent) {
        for (HiveNetConnection connection : DedicatedServer.get(PlayerService.class).players.values()) {
            // In range
            if (connection.getPlayer().location.dist(moveEvent.getProjectile().location) <= 5000) {
                if (!wizPlayed.containsKey(moveEvent.getProjectile().uuid)) {
                    wizPlayed.put(moveEvent.getProjectile().uuid, new LinkedList<>());
                }

                if (!wizPlayed.get(moveEvent.getProjectile().uuid).contains(connection.getUuid())) {
                    connection.playLocalSoundAtLocation(GameSounds.ARROW_PASS, moveEvent.getProjectile().location, 1, 1);
                    wizPlayed.get(moveEvent.getProjectile().uuid).add(connection.getUuid());
                }
            }
        }
    }

    @EventHandler
    public void onDealDamageEvent(PlayerDealDamageEvent dealDamageEvent) {
        if (dealDamageEvent.getPlayer().getInHand() != null && RangedWeapon.class.isAssignableFrom(dealDamageEvent.getPlayer().getInHand().getItem().getClass())) {
            // Is a ranged weapon in-hand

            if (dealDamageEvent.getPlayer().selectedAmmo == null) {
                dealDamageEvent.setDamage(0);
                dealDamageEvent.setCanceled(true);
                return;
            }

            if (DedicatedServer.get(CombatService.class).getAmountCountOfType(dealDamageEvent.getPlayer(), dealDamageEvent.getPlayer().selectedAmmo) <= 0) {
                dealDamageEvent.setDamage(0);
                dealDamageEvent.setCanceled(true);
                return;
            }

            if (WoodenArrow.class.isAssignableFrom(dealDamageEvent.getPlayer().selectedAmmo)) {
                dealDamageEvent.setDamage(5);
            } else if (StoneArrow.class.isAssignableFrom(dealDamageEvent.getPlayer().selectedAmmo)) {
                dealDamageEvent.setDamage(10);
            } else if (IronArrow.class.isAssignableFrom(dealDamageEvent.getPlayer().selectedAmmo)) {
                dealDamageEvent.setDamage(15);
            } else if (SteelArrow.class.isAssignableFrom(dealDamageEvent.getPlayer().selectedAmmo)) {
                dealDamageEvent.setDamage(25);
            }
        }
    }

}
