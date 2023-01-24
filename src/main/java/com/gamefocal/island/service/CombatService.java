package com.gamefocal.island.service;

import com.badlogic.gdx.math.Vector3;
import com.gamefocal.island.entites.combat.NetHitResult;
import com.gamefocal.island.entites.combat.PlayerHitBox;
import com.gamefocal.island.entites.net.HiveNetConnection;
import com.gamefocal.island.entites.service.HiveService;
import com.gamefocal.island.game.util.Location;
import com.google.auto.service.AutoService;
import com.google.inject.Inject;

import javax.inject.Singleton;
import java.util.Map;
import java.util.UUID;

@Singleton
@AutoService(HiveService.class)
public class CombatService implements HiveService<CombatService> {

    @Inject
    private PlayerService playerService;

    @Override
    public void init() {

    }


    public HiveNetConnection meleeHitResult(HiveNetConnection source, float attackDegree, float range) {

        // Search players nearby
        for (Map.Entry<UUID, Float> e : source.getPlayerDistances().entrySet()) {
            if (e.getValue() <= 300) {
                // Check hitbox here.

                PlayerHitBox hitBox = new PlayerHitBox(this.playerService.players.get(e.getKey()));

                NetHitResult result = hitBox.traceMelee(source.getPlayer().location.toVector(), 100, 0f);

                if(result != NetHitResult.NONE) {
                    // We found a HIT
                    System.out.println(result.name());
                }

            }
        }

        return null;
    }

    public HiveNetConnection randedHitResult(HiveNetConnection source, Location startingLocation, Vector3 vector) {
        return null;
    }

}
