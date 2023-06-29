package com.gamefocal.rivenworld.service;

import com.gamefocal.rivenworld.DedicatedServer;
import com.gamefocal.rivenworld.entites.service.HiveService;
import com.gamefocal.rivenworld.game.entites.blocks.*;
import com.gamefocal.rivenworld.game.items.placables.blocks.*;
import com.gamefocal.rivenworld.models.GameEntityModel;
import com.google.auto.service.AutoService;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.j256.ormlite.dao.GenericRawResults;
import com.j256.ormlite.support.DatabaseConnection;

import javax.inject.Singleton;
import java.sql.Array;
import java.sql.SQLException;
import java.util.*;

@AutoService(HiveService.class)
@Singleton
public class BackwardsCompatibilityService implements HiveService<BackwardsCompatibilityService> {

    public static HashMap<String, Class> classMap = new HashMap<>();

    @Override
    public void init() {

        System.out.println("Loading Backward Compatability...");

        /*
         * Entites
         * */
        classMap.put("com.gamefocal.rivenworld.game.entites.blocks.Clay.ClayBlock", ClayBlock.class);
        classMap.put("com.gamefocal.rivenworld.game.entites.blocks.Copper.CopperBlock", CopperBlock.class);
        classMap.put("com.gamefocal.rivenworld.game.entites.blocks.Dirt.DirtBlock", DirtBlock.class);
        classMap.put("com.gamefocal.rivenworld.game.entites.blocks.Glass.GlassBlock", GlassBlock.class);
        classMap.put("com.gamefocal.rivenworld.game.entites.blocks.Gold.GoldBlock", GoldBlock.class);
        classMap.put("com.gamefocal.rivenworld.game.entites.blocks.Iron.IronBlock", IronBlock.class);
        classMap.put("com.gamefocal.rivenworld.game.entites.blocks.Log.LogBlock", LogBlock.class);
        classMap.put("com.gamefocal.rivenworld.game.entites.blocks.Plaster.PlasterBlock", PlasterBlock.class);
        classMap.put("com.gamefocal.rivenworld.game.entites.blocks.Sand.SandBlock", SandBlock.class);


        /*
         * Items
         * */
        classMap.put("com.gamefocal.rivenworld.game.items.placables.blocks.Clay.ClayBlockItem", ClayBlockItem.class);
        classMap.put("com.gamefocal.rivenworld.game.items.placables.blocks.Copper.CopperBlockItem", CopperBlockItem.class);
        classMap.put("com.gamefocal.rivenworld.game.items.placables.blocks.Dirt.DirtBlockItem", DirtBlockItem.class);
        classMap.put("com.gamefocal.rivenworld.game.items.placables.blocks.Glass.GlassBlockItem", GlassBlockItem.class);
        classMap.put("com.gamefocal.rivenworld.game.items.placables.blocks.Gold.GoldBlockItem", GoldBlockItem.class);
        classMap.put("com.gamefocal.rivenworld.game.items.placables.blocks.Iron.IronBlockItem", IronBlockItem.class);
        classMap.put("com.gamefocal.rivenworld.game.items.placables.blocks.Log.LogBlockItem", LogBlockItem.class);
        classMap.put("com.gamefocal.rivenworld.game.items.placables.blocks.Plaster.PlasterBlockItem", PlasterBlockItem.class);
        classMap.put("com.gamefocal.rivenworld.game.items.placables.blocks.Sand.SandBlockItem", SandBlockItem.class);

    }

    public static void migrate() {

        System.out.println("Starting World File Migration...");
        for (Map.Entry<String, Class> m : classMap.entrySet()) {

            System.out.println("Checking Migrations for " + m.getKey() + " to " + m.getValue().getName());

            try {
                /*
                 * Migrate Entities to new classes
                 * */
                GenericRawResults<String[]> r = DataService.gameEntities.queryRaw("SELECT * FROM game_entity WHERE entityType = ? AND entityData LIKE ?", m.getValue().getSimpleName(), "%" + m.getKey() + "%");

                System.out.println("Checking Game Entities...");
                for (String[] s : r.getResults()) {
//                    System.out.println(Arrays.toString(s));


                    UUID uuid = UUID.fromString(s[0]);
                    String typeName = s[1];
                    String jsonData = s[2];

                    JsonObject entityObj = JsonParser.parseString(jsonData).getAsJsonObject();
                    entityObj.addProperty("class", m.getValue().getName());

                    String oldItemName = entityObj.get("relatedItem").getAsJsonObject().get("class").getAsString();
                    if (classMap.containsKey(oldItemName)) {
                        JsonObject io = entityObj.get("relatedItem").getAsJsonObject();
                        io.addProperty("class", classMap.get(oldItemName).getName());
                        entityObj.add("relatedItem", io);
                    }

                    DataService.gameEntities.updateRaw("UPDATE game_entity SET entityData = ? WHERE uuid = ?", entityObj.toString(), uuid.toString());
                    System.out.println("Migrated entity with UUID " + uuid.toString() + " to new class location: " + m.getValue().getSimpleName());

//                    System.out.println(entityObj.toString());

                }

                /*
                 * Migrate Player Inventories
                 * */
                System.out.println("Checking Players...");
                GenericRawResults<String[]> r2 = DataService.players.queryRaw("SELECT * FROM player WHERE inventory LIKE ?", "%" + m.getKey() + "%");
                for (String[] s : r2.getResults()) {
                    String playerId = s[0];
                    String name = s[1];
                    UUID playerUUID = UUID.fromString(s[2]);
                    String inv = s[6];

                    JsonObject invObj = JsonParser.parseString(inv).getAsJsonObject();

                    JsonArray items = invObj.get("items").getAsJsonArray();
                    JsonArray newItems = new JsonArray();

                    boolean changed = false;

                    for (JsonElement i : items) {
                        if (i != null && i.isJsonObject()) {
                            JsonObject stack = i.getAsJsonObject();
                            if (stack.has("item")) {
                                JsonObject ii = stack.get("item").getAsJsonObject();

                                if (classMap.containsKey(ii.get("class").getAsString())) {
                                    // Has a bad class name
                                    ii.addProperty("class", classMap.get(ii.get("class").getAsString()).getName());
                                    changed = true;
                                }
                                stack.add("item", ii);

                                newItems.add(stack);
                            }
                        }
                    }

                    invObj.add("items", newItems);

                    if (changed) {
                        DataService.gameEntities.updateRaw("UPDATE player SET inventory = ? WHERE id = ?", invObj.toString(), playerId);
                        System.out.println("Migrating player " + name + " inventory");
                    }
                }

                /*
                 * Migrate game entities that have inventories
                 * */
                System.out.println("Checking Object Inventories...");
                GenericRawResults<String[]> r3 = DataService.gameEntities.queryRaw("SELECT * FROM game_entity WHERE entityData LIKE ?", "%inventory%");

                for (String[] s : r3.getResults()) {
//                    System.out.println(Arrays.toString(s));

                    UUID uuid = UUID.fromString(s[0]);
                    String typeName = s[1];
                    String jsonData = s[2];

                    JsonObject entityObj = JsonParser.parseString(jsonData).getAsJsonObject();

                    if (entityObj.has("inventory")) {
                        // Has a inventory
                        JsonObject invObj = entityObj.get("inventory").getAsJsonObject();

                        JsonArray items = invObj.get("items").getAsJsonArray();
                        JsonArray newItems = new JsonArray();

                        boolean changed = false;

                        for (JsonElement i : items) {
                            if (i != null && i.isJsonObject()) {
                                JsonObject stack = i.getAsJsonObject();
                                if (stack.has("item")) {
                                    JsonObject ii = stack.get("item").getAsJsonObject();

                                    if (classMap.containsKey(ii.get("class").getAsString())) {
                                        // Has a bad class name
                                        ii.addProperty("class", classMap.get(ii.get("class").getAsString()).getName());
                                        changed = true;
                                    }
                                    stack.add("item", ii);

                                    newItems.add(stack);
                                }
                            }
                        }

                        invObj.add("items", newItems);

                        entityObj.add("inventory", invObj);

                        if (changed) {
                            DataService.gameEntities.updateRaw("UPDATE game_entity SET entityData = ? WHERE uuid = ?", entityObj.toString(), uuid.toString());
                            System.out.println("Migrated entity with Inventory by UUID " + uuid.toString());
                        }
                    }

//                    System.out.println(entityObj.toString());

                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }
}
