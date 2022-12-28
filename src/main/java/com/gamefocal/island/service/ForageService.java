package com.gamefocal.island.service;

import com.badlogic.gdx.math.collision.Sphere;
import com.gamefocal.island.entites.net.HiveNetConnection;
import com.gamefocal.island.entites.service.HiveService;
import com.gamefocal.island.game.inventory.InventoryItem;
import com.gamefocal.island.game.inventory.InventoryStack;
import com.gamefocal.island.game.items.food.seeds.AppleSeed;
import com.gamefocal.island.game.items.food.seeds.CornSeed;
import com.gamefocal.island.game.items.food.seeds.PearSeed;
import com.gamefocal.island.game.items.food.spices.Buttercup;
import com.gamefocal.island.game.items.food.spices.Camomile;
import com.gamefocal.island.game.items.food.spices.Clover;
import com.gamefocal.island.game.items.placables.DirtBlockItem;
import com.gamefocal.island.game.items.placables.SandBlockItem;
import com.gamefocal.island.game.items.resources.minerals.raw.Flint;
import com.gamefocal.island.game.items.resources.minerals.raw.Stone;
import com.gamefocal.island.game.items.resources.misc.Clay;
import com.gamefocal.island.game.items.resources.misc.Poop;
import com.gamefocal.island.game.items.resources.misc.Thatch;
import com.gamefocal.island.game.items.resources.wood.WoodStick;
import com.gamefocal.island.game.util.Location;
import com.gamefocal.island.game.util.MathUtil;
import com.gamefocal.island.game.util.RandomUtil;
import com.google.auto.service.AutoService;

import javax.inject.Singleton;
import java.util.*;

@Singleton
@AutoService(HiveService.class)
public class ForageService implements HiveService<ForageService> {

    public Hashtable<UUID, Float> foragedTrees = new Hashtable<>();

    public Hashtable<Location, Float> foragedLocations = new Hashtable<>();

    public HashMap<String, HashMap<Class<? extends InventoryItem>, Integer>> groundValues = new HashMap<>();

    @Override
    public void init() {
        HashMap<Class<? extends InventoryItem>, Integer> rocks = new HashMap<>();
        HashMap<Class<? extends InventoryItem>, Integer> dirt = new HashMap<>();
        HashMap<Class<? extends InventoryItem>, Integer> grass = new HashMap<>();
        HashMap<Class<? extends InventoryItem>, Integer> sand = new HashMap<>();

        /*
         * Rock Ground Type
         * */
        rocks.put(Stone.class, 10);
        rocks.put(WoodStick.class, 1);
        rocks.put(Flint.class, 4);

        /*
         * Dirt Ground Type
         * */
        dirt.put(Clay.class, 5);
        dirt.put(DirtBlockItem.class, 10);
        dirt.put(Poop.class, 2);
        dirt.put(WoodStick.class, 3);

        /*
         * Grass Ground Type
         * */
        grass.put(WoodStick.class, 4);
        grass.put(Thatch.class, 6);
        grass.put(AppleSeed.class, 2);
        grass.put(PearSeed.class, 2);
        grass.put(CornSeed.class, 4);
        grass.put(DirtBlockItem.class, 2);
        grass.put(Buttercup.class, 6);
        grass.put(Camomile.class, 7);
        grass.put(Clover.class, 8);

        /*
         * Send Ground Type
         * */
        sand.put(SandBlockItem.class, 10);
        sand.put(WoodStick.class, 5);

        this.groundValues.put("Rocks", rocks);
        this.groundValues.put("Dirt", dirt);
        this.groundValues.put("Grass", grass);
        this.groundValues.put("Sand", sand);
    }

    public List<InventoryStack> forageGround(HiveNetConnection connection, String type, Location location) {

        // TODO: Add skills into this later...

        ArrayList<InventoryStack> stacks = new ArrayList<>();

        Sphere testSphere = new Sphere(location.toVector(), 100 * 5);

        Location foundForageLocation = null;

        for (Map.Entry<Location, Float> e : this.foragedLocations.entrySet()) {
            Sphere locSphere = new Sphere(e.getKey().toVector(), 100 * 5);
            if (testSphere.overlaps(locSphere)) {
                // Overlaps this one.
                foundForageLocation = e.getKey();
                break;
            }
        }

        if (foundForageLocation == null) {
            foundForageLocation = location;
        }

        float health = 100f;
        if (this.foragedLocations.containsKey(foundForageLocation)) {
            health = this.foragedLocations.get(foundForageLocation);
        }

        System.out.println("Forage Health: " + health);

        if (health <= 0) {
            return new ArrayList<>();
        }

        float maxItemsToFind = MathUtil.map(health, 0, 100, 0, 4);

        System.out.println("Find MAX: " + maxItemsToFind);

        float findAmt = (float) Math.floor(RandomUtil.getRandomNumberBetween(0, maxItemsToFind));

        System.out.println("FIND LOOP: " + findAmt);

        if (this.groundValues.containsKey(type)) {
            HashMap<Class<? extends InventoryItem>, Integer> selectedSet = this.groundValues.get(type);

            for (int i = 0; i < findAmt; i++) {
                Class<? extends InventoryItem> item = RandomUtil.getRandomElementFromMap(selectedSet);

                if (item != null) {
                    int found = RandomUtil.getRandomNumberBetween(1, 4);

                    System.out.println("Found: " + found + " of " + item.getSimpleName());

                    try {
                        stacks.add(new InventoryStack(item.newInstance(), found));
                    } catch (InstantiationException | IllegalAccessException e) {
                        e.printStackTrace();
                    }

                    health -= 25;
                }
            }
        } else {
            System.out.println("Invalid Type of Ground");
        }

        this.foragedLocations.put(location, health);

        return stacks;
    }

}