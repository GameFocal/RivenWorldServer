package com.gamefocal.rivenworld.service;

import com.gamefocal.rivenworld.DedicatedServer;
import com.gamefocal.rivenworld.entites.service.HiveService;
import com.gamefocal.rivenworld.game.entites.loot.LargeLootChest;
import com.gamefocal.rivenworld.game.entites.loot.LootChest;
import com.gamefocal.rivenworld.game.entites.loot.MediumLootChest;
import com.gamefocal.rivenworld.game.entites.loot.SmallLootChest;
import com.gamefocal.rivenworld.game.inventory.InventoryItem;
import com.gamefocal.rivenworld.game.inventory.InventoryStack;
import com.gamefocal.rivenworld.game.items.clothes.chest.cloth.FancyClothShirt;
import com.gamefocal.rivenworld.game.items.clothes.chest.cloth.SimpleClothShirt;
import com.gamefocal.rivenworld.game.items.clothes.chest.iron.MediumIronShirt;
import com.gamefocal.rivenworld.game.items.clothes.chest.iron.SimpleIronShirt;
import com.gamefocal.rivenworld.game.items.clothes.chest.leather.HeavyLeatherShirt;
import com.gamefocal.rivenworld.game.items.clothes.chest.leather.MediumLeatherShirt;
import com.gamefocal.rivenworld.game.items.clothes.chest.leather.SimpleLeatherShirt;
import com.gamefocal.rivenworld.game.items.clothes.chest.steel.CrusiaderShirt;
import com.gamefocal.rivenworld.game.items.clothes.chest.steel.HeavySteelPlateShirt;
import com.gamefocal.rivenworld.game.items.clothes.chest.steel.SteelPlateShirt;
import com.gamefocal.rivenworld.game.items.clothes.feet.iron.HeavyIronBoots;
import com.gamefocal.rivenworld.game.items.clothes.feet.iron.MediumIronBoots;
import com.gamefocal.rivenworld.game.items.clothes.feet.iron.SimpleIronBoots;
import com.gamefocal.rivenworld.game.items.clothes.feet.leather.FancyLeatherBoots;
import com.gamefocal.rivenworld.game.items.clothes.feet.leather.FancyLeatherShoes;
import com.gamefocal.rivenworld.game.items.clothes.feet.leather.SimpleLeatherBoots;
import com.gamefocal.rivenworld.game.items.clothes.feet.leather.SimpleLeatherShoes;
import com.gamefocal.rivenworld.game.items.clothes.feet.steel.SteelBoots;
import com.gamefocal.rivenworld.game.items.clothes.head.CloakHead;
import com.gamefocal.rivenworld.game.items.clothes.head.ClothCap;
import com.gamefocal.rivenworld.game.items.clothes.legs.cloth.FancyClothLegs;
import com.gamefocal.rivenworld.game.items.clothes.legs.cloth.SimpleClothLegs;
import com.gamefocal.rivenworld.game.items.clothes.legs.iron.HeavyIronLegs;
import com.gamefocal.rivenworld.game.items.clothes.legs.iron.MediumIronLegs;
import com.gamefocal.rivenworld.game.items.clothes.legs.iron.SimpleIronLegs;
import com.gamefocal.rivenworld.game.items.clothes.legs.leather.HeavyLeatherLegs;
import com.gamefocal.rivenworld.game.items.clothes.legs.leather.MediumLeatherLegs;
import com.gamefocal.rivenworld.game.items.clothes.legs.leather.SimpleLeatherLegs;
import com.gamefocal.rivenworld.game.items.food.consumable.Apple;
import com.gamefocal.rivenworld.game.items.food.consumable.Cabbage;
import com.gamefocal.rivenworld.game.items.food.consumable.Pear;
import com.gamefocal.rivenworld.game.items.food.consumable.Potato;
import com.gamefocal.rivenworld.game.items.food.seeds.*;
import com.gamefocal.rivenworld.game.items.resources.minerals.raw.*;
import com.gamefocal.rivenworld.game.items.resources.minerals.refined.CopperIgnot;
import com.gamefocal.rivenworld.game.items.resources.minerals.refined.GoldIgnot;
import com.gamefocal.rivenworld.game.items.resources.minerals.refined.IronIgnot;
import com.gamefocal.rivenworld.game.items.resources.minerals.refined.SteelIgnot;
import com.gamefocal.rivenworld.game.items.resources.misc.Feather;
import com.gamefocal.rivenworld.game.items.resources.misc.Fiber;
import com.gamefocal.rivenworld.game.items.resources.misc.Thatch;
import com.gamefocal.rivenworld.game.items.resources.wood.WoodLog;
import com.gamefocal.rivenworld.game.items.resources.wood.WoodPlank;
import com.gamefocal.rivenworld.game.items.resources.wood.WoodStick;
import com.gamefocal.rivenworld.game.items.weapons.*;
import com.gamefocal.rivenworld.game.items.weapons.Basic.WoodenClub;
import com.gamefocal.rivenworld.game.items.weapons.PickAxe.IronPickaxe;
import com.gamefocal.rivenworld.game.items.weapons.PickAxe.SteelPickaxe;
import com.gamefocal.rivenworld.game.items.weapons.PickAxe.StonePickaxe;
import com.gamefocal.rivenworld.game.items.weapons.PickAxe.WoodPickaxe;
import com.gamefocal.rivenworld.game.items.weapons.hatchets.IronHatchet;
import com.gamefocal.rivenworld.game.items.weapons.hatchets.SteelHatchet;
import com.gamefocal.rivenworld.game.items.weapons.hatchets.StoneHatchet;
import com.gamefocal.rivenworld.game.items.weapons.hatchets.WoodHatchet;
import com.gamefocal.rivenworld.game.items.weapons.hoe.WoodenHoe;
import com.gamefocal.rivenworld.game.items.weapons.sword.*;
import com.gamefocal.rivenworld.game.util.Location;
import com.gamefocal.rivenworld.game.util.RandomUtil;
import com.gamefocal.rivenworld.game.util.TickUtil;
import com.google.auto.service.AutoService;

import javax.inject.Singleton;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@AutoService(HiveService.class)
@Singleton


/*
 * TODO:
 *  - Spawn loot, Low = 6, Mid = 4, High = 3
 *  - Loot should re-spawn at every restart
 *  - Loot should auto despawn after 60 minutes of being spawned
 *  - Loot should destroy when empty
 *  - Loot should attempt a respawn every 15 minutes
 *  - LOW: 3 Slots
 *  - MID: 6 Slots
 *  - High: 10 Slots
 * */
public class LootService implements HiveService<LootService> {

    public static LinkedList<Location> LootSpawns = new LinkedList<>();

    public LinkedList<Class<? extends InventoryItem>> low = new LinkedList<>();
    public LinkedList<Class<? extends InventoryItem>> mid = new LinkedList<>();
    public LinkedList<Class<? extends InventoryItem>> high = new LinkedList<>();

    public static int lowCount = 0;
    public static int midCount = 0;
    public static int highCount = 0;

    public static final int maxLowSpawn = 6;
    public static final int maxMidSpawn = 4;
    public static final int maxHighSpawn = 3;

    public static ConcurrentHashMap<Location, LootChest> currentSpawns = new ConcurrentHashMap<>();

    @Override
    public void init() {
        /*
         * Low Grade Loot
         * */
        this.low.add(WoodenHoe.class);
        this.low.add(WoodHatchet.class);
        this.low.add(WoodStick.class);
        this.low.add(WoodLog.class);
        this.low.add(Wood_Spade.class);
        this.low.add(WoodenClub.class);
        this.low.add(Rope.class);
        this.low.add(WoodenSword.class);
        this.low.add(WoodBucket.class);
        this.low.add(Stone.class);
        this.low.add(WoodPickaxe.class);
        this.low.add(SimpleClothShirt.class);
        this.low.add(SimpleIronBoots.class);
        this.low.add(ClothCap.class);
        this.low.add(CloakHead.class);
        this.low.add(SimpleClothLegs.class);
        this.low.add(Apple.class);
        this.low.add(Pear.class);
        this.low.add(Feather.class);
        this.low.add(Fiber.class);
        this.low.add(WoodPlank.class);
        this.low.add(WheatSeed.class);

        /*
         * Med Grade Loot
         * */
        this.mid.add(StoneHatchet.class);
        this.mid.add(IronHatchet.class);
        this.mid.add(Spade.class);
        this.mid.add(IronSword.class);
        this.mid.add(Iron_LongSword.class);
        this.mid.add(StonePickaxe.class);
        this.mid.add(IronPickaxe.class);
        this.mid.add(GoldOre.class);
        this.mid.add(IronOre.class);
        this.mid.add(Thatch.class);
        this.mid.add(Coal.class);
        this.mid.add(CopperOre.class);
        this.mid.add(Bow.class);
        this.mid.add(FancyClothShirt.class);
        this.mid.add(FancyClothLegs.class);
        this.mid.add(SimpleIronShirt.class);
        this.mid.add(MediumIronShirt.class);
        this.mid.add(SimpleLeatherShirt.class);
        this.mid.add(MediumLeatherShirt.class);
        this.mid.add(FancyLeatherBoots.class);
        this.mid.add(FancyLeatherShoes.class);
        this.mid.add(SimpleLeatherBoots.class);
        this.mid.add(SimpleLeatherShoes.class);
        this.mid.add(SimpleLeatherLegs.class);
        this.mid.add(MediumLeatherLegs.class);
        this.mid.add(SimpleIronLegs.class);
        this.mid.add(MediumIronLegs.class);
        this.mid.add(CornSeed.class);
        this.mid.add(Cabbage.class);
        this.mid.add(Potato.class);
        this.mid.add(TomatoSeed.class);

        /*
         * High Grade Loot
         * */
        this.high.add(SteelHatchet.class);
        this.high.add(SteelSword.class);
        this.high.add(Steel_LongSword.class);
        this.high.add(SteelPickaxe.class);
        this.high.add(GoldIgnot.class);
        this.high.add(IronIgnot.class);
        this.high.add(CopperIgnot.class);
        this.high.add(SteelIgnot.class);
        this.high.add(HeavyLeatherLegs.class);
        this.high.add(HeavyLeatherShirt.class);
        this.high.add(SteelPlateShirt.class);
        this.high.add(HeavySteelPlateShirt.class);
        this.high.add(CrusiaderShirt.class);
        this.high.add(SimpleIronBoots.class);
        this.high.add(MediumIronBoots.class);
        this.high.add(HeavyIronBoots.class);
        this.high.add(SteelBoots.class);
        this.high.add(HeavyIronLegs.class);
        this.high.add(PumpkinSeed.class);
        this.high.add(WatermelonSeed.class);

        currentSpawns = new ConcurrentHashMap<>();

        /*
         * Loot Spawn Points
         * */
        LootSpawns.add(Location.fromUEString("(X=90652.731114,Y=87940.212279,Z=10461.318587)", "(Pitch=0.000000,Yaw=-119.999999,Roll=0.000000)"));
        LootSpawns.add(Location.fromUEString("(X=91780.330012,Y=88286.763425,Z=10568.767926)", "(Pitch=0.000000,Yaw=-169.999999,Roll=0.000000)"));
        LootSpawns.add(Location.fromUEString("(X=92614.753192,Y=88693.135453,Z=10918.788203)", "(Pitch=0.000000,Yaw=169.999999,Roll=0.000000)"));
        LootSpawns.add(Location.fromUEString("(X=91021.742483,Y=86818.901716,Z=10869.193568)", "(Pitch=0.000000,Yaw=0.000000,Roll=0.000000)"));
        LootSpawns.add(Location.fromUEString("(X=89760.109342,Y=87635.737795,Z=10500.041615)", "(Pitch=0.000000,Yaw=-119.999999,Roll=0.000000)"));
        LootSpawns.add(Location.fromUEString("(X=90085.975691,Y=86703.662201,Z=11042.129960)", "(Pitch=0.000000,Yaw=-119.999999,Roll=0.000000)"));
        LootSpawns.add(Location.fromUEString("(X=89405.093913,Y=87321.824552,Z=10359.284374)", "(Pitch=0.000000,Yaw=60.000000,Roll=0.000000)"));
        LootSpawns.add(Location.fromUEString("(X=93158.543338,Y=91321.590844,Z=9446.516909)", "(Pitch=0.000000,Yaw=-60.000000,Roll=0.000000)"));
        LootSpawns.add(Location.fromUEString("(X=93881.839462,Y=94480.785683,Z=8950.024979)", "(Pitch=0.000000,Yaw=0.000000,Roll=0.000000)"));
        LootSpawns.add(Location.fromUEString("(X=82456.414407,Y=88893.100743,Z=11822.639535)", "(Pitch=0.000000,Yaw=0.000000,Roll=0.000000)"));
        LootSpawns.add(Location.fromUEString("(X=114594.999215,Y=108584.518759,Z=6968.089726)", "(Pitch=0.000000,Yaw=0.000000,Roll=0.000000)"));
        LootSpawns.add(Location.fromUEString("(X=112636.797619,Y=109890.222468,Z=6455.190404)", "(Pitch=0.000000,Yaw=10.000000,Roll=0.000000)"));
        LootSpawns.add(Location.fromUEString("(X=128902.843721,Y=123549.574284,Z=6918.678901)", "(Pitch=0.000000,Yaw=-30.000000,Roll=0.000000)"));
        LootSpawns.add(Location.fromUEString("(X=138606.958612,Y=93142.046882,Z=4041.637441)", "(Pitch=0.000000,Yaw=-50.000000,Roll=0.000000)"));
        LootSpawns.add(Location.fromUEString("(X=112841.909359,Y=71188.582779,Z=13682.405331)", "(Pitch=0.000000,Yaw=40.000000,Roll=0.000000)"));
        LootSpawns.add(Location.fromUEString("(X=115128.510745,Y=71146.092587,Z=14612.374004)", "(Pitch=0.000000,Yaw=50.000000,Roll=0.000000)"));
        LootSpawns.add(Location.fromUEString("(X=112452.567502,Y=71327.178651,Z=15362.175195)", "(Pitch=0.000000,Yaw=0.000000,Roll=0.000000)"));
        LootSpawns.add(Location.fromUEString("(X=127403.638250,Y=49680.493454,Z=7328.302167)", "(Pitch=0.000000,Yaw=0.000000,Roll=0.000000)"));
        LootSpawns.add(Location.fromUEString("(X=122829.087128,Y=38485.344065,Z=6199.046465)", "(Pitch=0.000000,Yaw=0.000000,Roll=0.000000)"));
        LootSpawns.add(Location.fromUEString("(X=56963.579000,Y=37594.131869,Z=18465.107657)", "(Pitch=0.000000,Yaw=0.000000,Roll=0.000000)"));
        LootSpawns.add(Location.fromUEString("(X=60339.455620,Y=42586.911862,Z=18751.923728)", "(Pitch=0.000000,Yaw=109.999999,Roll=0.000000)"));
        LootSpawns.add(Location.fromUEString("(X=58084.212526,Y=43350.440069,Z=18686.752618)", "(Pitch=0.000000,Yaw=-179.999999,Roll=0.000000)"));
        LootSpawns.add(Location.fromUEString("(X=64663.967027,Y=39421.235735,Z=18700.276998)", "(Pitch=0.000000,Yaw=0.000000,Roll=0.000000)"));
        LootSpawns.add(Location.fromUEString("(X=60898.395500,Y=50295.244107,Z=22584.250118)", "(Pitch=0.000000,Yaw=119.999999,Roll=0.000000)"));
        LootSpawns.add(Location.fromUEString("(X=37233.358879,Y=78515.673497,Z=18943.279038)", "(Pitch=0.000000,Yaw=0.000000,Roll=0.000000)"));
        LootSpawns.add(Location.fromUEString("(X=18558.677861,Y=95170.686291,Z=13774.509144)", "(Pitch=0.000000,Yaw=0.000000,Roll=0.000000)"));
        LootSpawns.add(Location.fromUEString("(X=3889.707634,Y=90774.957929,Z=8867.899077)", "(Pitch=0.000000,Yaw=0.000000,Roll=0.000000)"));
        LootSpawns.add(Location.fromUEString("(X=-2626.472516,Y=156029.461714,Z=3325.498784)", "(Pitch=0.000000,Yaw=-60.000000,Roll=0.000000)"));
        LootSpawns.add(Location.fromUEString("(X=-11853.171416,Y=155666.382277,Z=5495.087253)", "(Pitch=0.000000,Yaw=-139.999999,Roll=0.000000)"));
        LootSpawns.add(Location.fromUEString("(X=70417.558915,Y=122952.761707,Z=24502.582785)", "(Pitch=0.000000,Yaw=0.000000,Roll=0.000000)"));
    }

    public Location getRandomLootSpawnLocation() {
        Location r = RandomUtil.getRandomElementFromList(LootSpawns);
        if (!currentSpawns.containsKey(r)) {
            return r;
        }

        return this.getRandomLootSpawnLocation();
    }

    public LinkedList<Location> getLootSpawns(int spawns) {
        LinkedList<Location> s = new LinkedList<>();
        for (int i = 0; i < spawns; i++) {
            s.add(this.getRandomLootSpawnLocation());
        }
        return s;
    }

    public void populateWorld() {
        // Small
        if (lowCount < maxLowSpawn) {
            LinkedList<Location> lowSpawns = this.getLootSpawns(maxLowSpawn - lowCount);

            System.out.println("Spawning LOW Loot: " + lowSpawns.size());

            for (Location location : lowSpawns) {
                this.spawnLootBoxAtLocation(new SmallLootChest(), location);
            }
        } else {
            System.out.println("Loot LOW already at max.");
        }
        if (midCount < maxMidSpawn) {
            LinkedList<Location> lowSpawns = this.getLootSpawns(maxMidSpawn - midCount);

            System.out.println("Spawning MID Loot: " + lowSpawns.size());

            for (Location location : lowSpawns) {
                this.spawnLootBoxAtLocation(new MediumLootChest(), location);
            }
        } else {
            System.out.println("Loot MID already at max.");
        }
        if (highCount < maxHighSpawn) {
            LinkedList<Location> lowSpawns = this.getLootSpawns(maxHighSpawn - highCount);

            System.out.println("Spawning HIGH Loot: " + lowSpawns.size());

            for (Location location : lowSpawns) {
                this.spawnLootBoxAtLocation(new LargeLootChest(), location);
            }
        } else {
            System.out.println("Loot HIGH already at max.");
        }
    }

    public void spawnLootBoxAtLocation(LootChest chest, Location location) {
        int tier = 0;
        if (SmallLootChest.class.isAssignableFrom(chest.getClass())) {
            // Small
            tier = 0;
            lowCount++;
        } else if (MediumLootChest.class.isAssignableFrom(chest.getClass())) {
            tier = 1;
            midCount++;
        } else if (LargeLootChest.class.isAssignableFrom(chest.getClass())) {
            tier = 2;
            highCount++;
        }

        chest.getInventory().clearInv();
        chest.setSpawnedAt(System.currentTimeMillis());
        LinkedList<InventoryStack> stacks = this.generateLoot(tier, chest.getInventory().getSize());
        for (InventoryStack s : stacks) {
            chest.getInventory().add(s);
        }

        DedicatedServer.instance.getWorld().spawn(chest, location);

        currentSpawns.put(location, chest);
    }

    public void despawnLootBox(LootChest chest) {
        int tier = 0;
        if (SmallLootChest.class.isAssignableFrom(chest.getClass())) {
            // Small
            tier = 0;
            lowCount--;
        } else if (MediumLootChest.class.isAssignableFrom(chest.getClass())) {
            tier = 1;
            midCount--;
        } else if (LargeLootChest.class.isAssignableFrom(chest.getClass())) {
            tier = 2;
            highCount--;
        }

        currentSpawns.remove(chest.location);
        DedicatedServer.instance.getWorld().despawn(chest.getModel().uuid);
    }

    public LinkedList<InventoryStack> generateLoot(int tier, int itemCount) {
        Random rand = new Random();
        LinkedList<Class<? extends InventoryItem>> potentialItems = new LinkedList<>();

        if (tier >= 0) {
            for (int i = 0; i < (3 - tier); i++) { // Add low tier items multiple times based on tier
                potentialItems.addAll(low);
            }
        }
        if (tier >= 1) {
            for (int i = 0; i < (3 - tier + 1); i++) { // Add mid tier items multiple times based on tier
                potentialItems.addAll(mid);
            }
        }
        if (tier == 2) {
            potentialItems.addAll(high); // High tier items are added only once for high tier loot
        }

        Set<Class<? extends InventoryItem>> selectedItems = new HashSet<>(); // Ensures no repeats
        LinkedList<InventoryStack> result = new LinkedList<>();

        while (result.size() < itemCount) {
            Class<? extends InventoryItem> randomItem = potentialItems.get(rand.nextInt(potentialItems.size()));

            if (!selectedItems.contains(randomItem)) {
                selectedItems.add(randomItem);

                try {
                    InventoryItem itemInstance = randomItem.getDeclaredConstructor().newInstance();

                    InventoryStack stack = new InventoryStack(itemInstance);

                    result.add(stack);
                } catch (Exception e) {
                    e.printStackTrace(); // Log or handle the exception as you see fit
                }
            }
        }

        return result;
    }
}
