package com.gamefocal.rivenworld.service;

import com.badlogic.gdx.math.MathUtils;
import com.gamefocal.rivenworld.DedicatedServer;
import com.gamefocal.rivenworld.entites.service.HiveService;
import com.gamefocal.rivenworld.game.DestructibleEntity;
import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.WorldChunk;
import com.gamefocal.rivenworld.game.util.RandomUtil;
import com.gamefocal.rivenworld.game.util.TickUtil;
import com.gamefocal.rivenworld.models.GameChunkModel;
import com.gamefocal.rivenworld.models.GameEntityModel;
import com.google.auto.service.AutoService;

import javax.inject.Singleton;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;

@AutoService(HiveService.class)
@Singleton
public class DecayService implements HiveService<DecayService> {

    private static long lastDecay = 0L;

    @Override
    public void init() {
        TaskService.scheduleRepeatingTask(() -> {
            if (TimeUnit.MILLISECONDS.toMinutes(System.currentTimeMillis() - lastDecay) >= 60) {

                System.out.println("[DECAY]: Processing World Decay");

                for (WorldChunk[] cc : DedicatedServer.instance.getWorld().getChunks()) {
                    for (WorldChunk c : cc) {
                        processDecay(c);
                    }
                }

                lastDecay = System.currentTimeMillis();
            }
        }, 20L, TickUtil.MINUTES(15), false);
    }

    public void processDecay(WorldChunk chunk) {

        if (!DedicatedServer.settings.enableDecay) {
            return;
        }

        boolean hasClaim = false;
        boolean hasFuel = false;
        GameChunkModel model = chunk.getModel();
        if (model != null) {
            if (model.claim != null) {
                hasClaim = true;
                if (model.claim.fuel > 0) {
                    hasFuel = true;
                }
            }
        }

        if (!hasClaim || (hasClaim && !hasFuel)) {
            /*
             * Needs to be decayed
             * */

            try {
                List<GameEntityModel> entityModels = DataService.gameEntities.queryBuilder().where().eq("chunkCords", chunk.getChunkCords()).and().isNotNull("owner_uuid").query();

                List<GameEntity> randomDespawns = new ArrayList<>();

                float entityCount = entityModels.size();

                float totalToDespawn = RandomUtil.getRandomNumberBetween(1, Math.max(1, entityCount / (DedicatedServer.settings.chunkDecayRate / 60)));

                int despawned = 0;
                for (GameEntityModel m : entityModels) {
                    GameEntity e = m.entityData;

                    if (DestructibleEntity.class.isAssignableFrom(e.getClass())) {
                        // Has health

                        float hit = ((DestructibleEntity<?>) e).getMaxHealth() / (DedicatedServer.settings.chunkDecayRate / 60);

                        float heightMulti = Math.max(1, MathUtils.map(0, 65000, 1, 4, e.location.getZ()));

                        hit *= heightMulti;

                        ((DestructibleEntity<?>) e).setHealth(((DestructibleEntity<?>) e).getHealth() - hit);

                        if (((DestructibleEntity<?>) e).getHealth() <= 0) {
                            // Despawn
                            DedicatedServer.instance.getWorld().despawn(e.uuid);
                        }

                    } else {
                        float heightMulti = MathUtils.map(0, 65000, 0.001f, 0.85f, e.location.getZ());
                        if (RandomUtil.getRandomChance(heightMulti) && despawned++ < totalToDespawn) {
                            DedicatedServer.instance.getWorld().despawn(e.uuid);
                        }
                    }
                }

            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }

        }

    }

}
