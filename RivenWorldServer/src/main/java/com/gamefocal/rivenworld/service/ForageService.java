package com.gamefocal.rivenworld.service;

import com.badlogic.gdx.math.collision.Sphere;
import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.entites.service.HiveService;
import com.gamefocal.rivenworld.game.inventory.InventoryItem;
import com.gamefocal.rivenworld.game.inventory.InventoryStack;
import com.gamefocal.rivenworld.game.items.food.consumable.Apple;
import com.gamefocal.rivenworld.game.items.food.consumable.Pear;
import com.gamefocal.rivenworld.game.items.food.seeds.AppleSeed;
import com.gamefocal.rivenworld.game.items.food.seeds.CornSeed;
import com.gamefocal.rivenworld.game.items.food.seeds.PearSeed;
import com.gamefocal.rivenworld.game.items.food.spices.Buttercup;
import com.gamefocal.rivenworld.game.items.food.spices.Camomile;
import com.gamefocal.rivenworld.game.items.food.spices.Clover;
import com.gamefocal.rivenworld.game.items.placables.blocks.DirtBlockItem;
import com.gamefocal.rivenworld.game.items.placables.blocks.SandBlockItem;
import com.gamefocal.rivenworld.game.items.resources.minerals.raw.Flint;
import com.gamefocal.rivenworld.game.items.resources.minerals.raw.Stone;
import com.gamefocal.rivenworld.game.items.resources.misc.Clay;
import com.gamefocal.rivenworld.game.items.resources.misc.Leaves;
import com.gamefocal.rivenworld.game.items.resources.misc.Poop;
import com.gamefocal.rivenworld.game.items.resources.misc.Thatch;
import com.gamefocal.rivenworld.game.items.resources.wood.WoodStick;
import com.gamefocal.rivenworld.game.util.Location;
import com.gamefocal.rivenworld.game.util.MathUtil;
import com.gamefocal.rivenworld.game.util.RandomUtil;
import com.gamefocal.rivenworld.models.GameFoliageModel;
import com.google.auto.service.AutoService;

import javax.inject.Singleton;
import java.util.*;

@Singleton
@AutoService(HiveService.class)
public class ForageService implements HiveService<ForageService> {

    public Hashtable<String, Float> foragedTrees = new Hashtable<>();

    public Hashtable<Location, Float> foragedLocations = new Hashtable<>();

    public HashMap<String, HashMap<Class<? extends InventoryItem>, Integer>> findChances = new HashMap<>();

    @Override
    public void init() {
        HashMap<Class<? extends InventoryItem>, Integer> tree = new HashMap<>();
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

        /*
         * Tree Foliage
         * */
        tree.put(WoodStick.class, 10);
        tree.put(Apple.class, 2);
        tree.put(Pear.class, 2);
        tree.put(Leaves.class, 9);

        this.findChances.put("Rocks", rocks);
        this.findChances.put("Dirt", dirt);
        this.findChances.put("Grass", grass);
        this.findChances.put("Sand", sand);
        this.findChances.put("tree",tree);
    }

    public List<InventoryStack> forageFoliage(HiveNetConnection connection, Location location, GameFoliageModel foliageModel) {

        ArrayList<InventoryStack> stacks = new ArrayList<>();

        float health = 100f;
        if (this.foragedTrees.containsKey(foliageModel.uuid)) {
            health = this.foragedTrees.get(foliageModel.uuid);
        }

        System.out.println(health);

        if (health <= 0) {
            return new ArrayList<>();
        }

        float maxItemsToFind = MathUtil.map(health, 0, 100, 0, 4);

        float findAmt = (float) Math.floor(RandomUtil.getRandomNumberBetween(0, maxItemsToFind));

        for (int i = 0; i < findAmt; i++) {
            Class<? extends InventoryItem> item = RandomUtil.getRandomElementFromMap(this.findChances.get("tree"));

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

        this.foragedTrees.put(foliageModel.uuid, health);

        return stacks;
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

        if (this.findChances.containsKey(type)) {
            HashMap<Class<? extends InventoryItem>, Integer> selectedSet = this.findChances.get(type);

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
