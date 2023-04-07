package com.gamefocal.rivenworld.service;

import com.gamefocal.rivenworld.DedicatedServer;
import com.gamefocal.rivenworld.entites.service.HiveService;
import com.gamefocal.rivenworld.game.entites.generics.LivingEntity;
import com.gamefocal.rivenworld.game.entites.living.NPC;
import com.gamefocal.rivenworld.game.entites.living.npc.BlacksmithNPC;
import com.gamefocal.rivenworld.game.entites.living.npc.ClothingNPC;
import com.gamefocal.rivenworld.game.entites.living.npc.FoodNPC;
import com.gamefocal.rivenworld.game.entites.living.npc.GeneralStoreNPC;
import com.gamefocal.rivenworld.game.util.Location;
import com.gamefocal.rivenworld.models.GameEntityModel;
import com.gamefocal.rivenworld.models.GameNpcModel;
import com.google.auto.service.AutoService;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import javax.inject.Singleton;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Singleton
@AutoService(HiveService.class)
public class NpcService implements HiveService<NpcService> {

    public static ConcurrentHashMap<String, Class<? extends NPC>> npcTypes = new ConcurrentHashMap<>();

    @Override
    public void init() {
//        this.load();
//        this.checkForNpcToSpawn();

        npcTypes.put("gs", GeneralStoreNPC.class);
        npcTypes.put("cloth", ClothingNPC.class);
        npcTypes.put("food", FoodNPC.class);
        npcTypes.put("smith", BlacksmithNPC.class);
    }

    public void load() {

        try {
            long npcCount = DataService.npcModels.countOf();

            if (npcCount == 0) {

                System.out.println("Loading NPCs");

                InputStream s = getClass().getClassLoader().getResourceAsStream("npc.json");
                try {
                    JsonObject object = JsonParser.parseString(new String(s.readAllBytes())).getAsJsonObject();

                    for (Map.Entry<String, JsonElement> m : object.entrySet()) {

                        if (!m.getValue().isJsonArray()) {
                            continue;
                        }

                        Class<? extends LivingEntity> lec = null;
                        try {
                            lec = (Class<? extends LivingEntity>) Class.forName(m.getKey());
                        } catch (ClassNotFoundException e) {
                            continue;
                        }

                        for (JsonElement loc : m.getValue().getAsJsonArray()) {
                            GameNpcModel npcModel = new GameNpcModel();
                            npcModel.npcType = lec.getName();
                            npcModel.location = Location.fromString(loc.getAsString());
                            npcModel.spawnedId = null;

                            DataService.npcModels.createIfNotExists(npcModel);

                            spawnNpc(npcModel);
                        }

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public GameNpcModel registerNpc(Class<? extends LivingEntity> c, Location location) {
        GameNpcModel npcModel = new GameNpcModel();
        npcModel.npcType = c.getName();
        npcModel.location = location;
        try {
            DataService.npcModels.createOrUpdate(npcModel);

            return npcModel;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return null;
    }

    public void checkForNpcToSpawn() {
        List<GameNpcModel> npcs = new ArrayList<>();
        try {
            npcs = DataService.npcModels.queryForAll();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        for (GameNpcModel npcModel : npcs) {
            this.spawnNpc(npcModel);
        }
    }

    public void spawnNpc(GameNpcModel model) {
        try {
            NPC npc = (NPC) Class.forName(model.npcType).newInstance();

            GameEntityModel spawn = DedicatedServer.instance.getWorld().spawn(npc, model.location);

            model.spawnedId = spawn.uuid;

            DataService.npcModels.createOrUpdate(model);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

}
