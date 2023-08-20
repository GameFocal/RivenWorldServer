package com.gamefocal.rivenworld.service;

import com.gamefocal.rivenworld.DedicatedServer;
import com.gamefocal.rivenworld.entites.econ.Shop;
import com.gamefocal.rivenworld.entites.econ.ShopItem;
import com.gamefocal.rivenworld.entites.service.HiveService;
import com.gamefocal.rivenworld.game.inventory.Inventory;
import com.gamefocal.rivenworld.game.items.clothes.chest.cloth.FancyClothShirt;
import com.gamefocal.rivenworld.game.items.clothes.chest.cloth.SimpleClothShirt;
import com.gamefocal.rivenworld.game.items.clothes.chest.iron.ChainmailShirt;
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
import com.gamefocal.rivenworld.game.items.clothes.feet.leather.SimpleLeatherBoots;
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
import com.gamefocal.rivenworld.game.items.food.consumable.*;
import com.gamefocal.rivenworld.game.items.food.seeds.*;
import com.gamefocal.rivenworld.game.items.placables.LandClaimItem;
import com.gamefocal.rivenworld.game.items.resources.econ.GoldCoin;
import com.gamefocal.rivenworld.game.items.resources.minerals.raw.CopperOre;
import com.gamefocal.rivenworld.game.items.resources.minerals.raw.GoldOre;
import com.gamefocal.rivenworld.game.items.resources.minerals.raw.IronOre;
import com.gamefocal.rivenworld.game.items.resources.minerals.refined.GoldIgnot;
import com.gamefocal.rivenworld.game.items.resources.minerals.refined.IronIgnot;
import com.gamefocal.rivenworld.game.items.resources.minerals.refined.SteelIgnot;
import com.gamefocal.rivenworld.game.items.resources.misc.Oil;
import com.gamefocal.rivenworld.game.items.resources.wood.WoodLog;
import com.gamefocal.rivenworld.game.items.resources.wood.WoodPlank;
import com.gamefocal.rivenworld.game.items.resources.wood.WoodStick;
import com.gamefocal.rivenworld.game.items.weapons.Basic.WoodenClub;
import com.gamefocal.rivenworld.game.items.weapons.PickAxe.StonePickaxe;
import com.gamefocal.rivenworld.game.items.weapons.Torch;
import com.gamefocal.rivenworld.game.items.weapons.WoodBucket;
import com.gamefocal.rivenworld.game.items.weapons.hatchets.StoneHatchet;
import com.gamefocal.rivenworld.game.items.weapons.hoe.WoodenHoe;
import com.gamefocal.rivenworld.game.shops.GameShop;
import com.gamefocal.rivenworld.game.util.Location;
import com.gamefocal.rivenworld.models.GameShopModel;
import com.google.auto.service.AutoService;

import javax.inject.Singleton;
import java.sql.SQLException;
import java.util.concurrent.ConcurrentHashMap;

@Singleton
@AutoService(HiveService.class)
public class ShopService implements HiveService<ShopService> {

    public static ConcurrentHashMap<String, Shop> shops = new ConcurrentHashMap<>();

    public static int getCoins(Inventory inventory) {
        int a = 0;
        a += inventory.amtOfType(GoldCoin.class);

        // TODO: Add Bags of Coins (Later)

        return a;
    }

    @Override
    public void init() {
        /*
         * Load Shops
         * */
        int startingStock = DedicatedServer.settings.shopStartingStock;

        // General Store
        initShop(new Shop(GameShop.GENERAL_STORE.getUid(), "General Store",
                new ShopItem(StoneHatchet.class, 25, startingStock),
                new ShopItem(StonePickaxe.class, 25, startingStock),
                new ShopItem(LandClaimItem.class, 50, startingStock),
                new ShopItem(WoodBucket.class, 10, startingStock)
//                new ShopItem(Torch.class, 10, startingStock)
        ));

        // Clothing
        initShop(new Shop(GameShop.CLOTHING_STORE.getUid(), "Clothing Store",
                new ShopItem(SimpleClothShirt.class, 10, startingStock),
                new ShopItem(FancyClothShirt.class, 20, startingStock),
                new ShopItem(SimpleClothLegs.class, 10, startingStock),
                new ShopItem(FancyClothLegs.class, 20, startingStock),
                new ShopItem(SimpleLeatherShirt.class, 10, startingStock),
                new ShopItem(MediumLeatherShirt.class, 20, startingStock),
                new ShopItem(HeavyLeatherShirt.class, 40, startingStock),
                new ShopItem(SimpleLeatherLegs.class, 10, startingStock),
                new ShopItem(MediumLeatherLegs.class, 20, startingStock),
                new ShopItem(HeavyLeatherLegs.class, 40, startingStock),
                new ShopItem(SimpleLeatherBoots.class, 10, startingStock),
                new ShopItem(FancyLeatherBoots.class, 20, startingStock),
                new ShopItem(ClothCap.class, 25, startingStock),
                new ShopItem(CloakHead.class, 10, startingStock)
        ));

        // Food
        initShop(new Shop(GameShop.FOOD_STORE.getUid(), "Farming Store",
                new ShopItem(Apple.class, 4, startingStock),
                new ShopItem(Pear.class, 6, startingStock),
                new ShopItem(Blueberry.class, 2, startingStock),
                new ShopItem(Watermelon.class, 10, startingStock)
        ));

        // Blacksmith
        initShop(new Shop(GameShop.BLACKSMITH_STORE.getUid(), "Blacksmith",
                new ShopItem(IronOre.class, 5, startingStock),
                new ShopItem(GoldOre.class, 10, startingStock),
                new ShopItem(CopperOre.class, 15, startingStock),
                new ShopItem(Oil.class, 20, startingStock),
                new ShopItem(IronIgnot.class, 10, startingStock),
                new ShopItem(GoldIgnot.class, 20, startingStock),
                new ShopItem(SteelIgnot.class, 30, startingStock),
                new ShopItem(SimpleIronShirt.class, 50, startingStock),
                new ShopItem(MediumIronShirt.class, 75, startingStock),
                new ShopItem(ChainmailShirt.class, 125, startingStock),
                new ShopItem(SimpleIronBoots.class, 50, startingStock),
                new ShopItem(MediumIronBoots.class, 75, startingStock),
                new ShopItem(HeavyIronBoots.class, 125, startingStock),
                new ShopItem(SimpleIronLegs.class, 50, startingStock),
                new ShopItem(MediumIronLegs.class, 75, startingStock),
                new ShopItem(HeavyIronLegs.class, 125, startingStock),
                new ShopItem(CrusiaderShirt.class, 150, startingStock),
                new ShopItem(SteelPlateShirt.class, 200, startingStock),
                new ShopItem(HeavySteelPlateShirt.class, 250, startingStock),
                new ShopItem(SteelBoots.class, 125, startingStock)
        ));

        // Lumber
        initShop(new Shop(GameShop.LUMBER_STORE.getUid(), "Lumber",
                new ShopItem(WoodStick.class, 2, startingStock),
                new ShopItem(WoodLog.class, 10, startingStock),
                new ShopItem(WoodPlank.class, 2, startingStock),
                new ShopItem(WoodenClub.class, 4, startingStock)
        ));

        // Farming
        initShop(new Shop(GameShop.FARMING.getUid(), "Farming",
                new ShopItem(WoodenHoe.class, 25, startingStock),
                new ShopItem(WheatSeed.class, 2, startingStock),
                new ShopItem(CornSeed.class, 2, startingStock),
                new ShopItem(Cabbage.class, 2, startingStock),
                new ShopItem(Potato.class, 2, startingStock),
                new ShopItem(TomatoSeed.class, 2, startingStock),
                new ShopItem(PumpkinSeed.class, 2, startingStock),
                new ShopItem(WatermelonSeed.class, 2, startingStock)
        ));

        loadShopsFromDb();

        if (DedicatedServer.settings.lockNPCTowns) {
            ClaimService.lockChunksBetween(
                    Location.fromString("75.0,43.0,0.0,0.0,0.0,0.0"),
                    Location.fromString("72.0,49.0,0.0,0.0,0.0,0.0")
            );
            ClaimService.lockChunksBetween(
                    Location.fromString("25.0,68.0,0.0,0.0,0.0,0.0"),
                    Location.fromString("26.0,65.0,0.0,0.0,0.0,0.0")
            );
        }
    }

    public String shopItemId(Shop shop, ShopItem item) {
        return shop.getUid() + ":" + item.getItem().getSimpleName();
    }

    public void initShop(Shop shop) {
        shops.put(shop.getUid(), shop);
    }

    public void loadShopsFromDb() {
        for (Shop shop : shops.values()) {
            if (shop != null) {
                for (ShopItem item : shop.getItems()) {
                    try {
                        GameShopModel model = DataService.shopItems.queryForId(shopItemId(shop, item));
                        if (model != null) {
                            item.setAmt(model.stock);
                        }
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }
                }

                shops.put(shop.getUid(), shop);
            }
        }
    }

    public void saveShopToDb(Shop shop) {
        for (ShopItem i : shop.getItems()) {
            try {
                GameShopModel model = DataService.shopItems.queryForId(shopItemId(shop, i));
                if (model == null) {
                    model = new GameShopModel();
                    model.itemClass = i.getItem().getSimpleName();
                    model.shopId = shop.getUid();
                    model.item = i.getItem().newInstance();
                    model.id = shopItemId(shop, i);
                }

                model.stock = i.getAmt();

                DataService.shopItems.createOrUpdate(model);

            } catch (SQLException | InstantiationException | IllegalAccessException throwables) {
                throwables.printStackTrace();
            }
        }
    }

    public Shop getShop(String uid) {
        return shops.get(uid);
    }

    public Shop getShop(GameShop uid) {
        return shops.get(uid.getUid());
    }

    public void save() {
        for (Shop s : shops.values()) {
            saveShopToDb(s);
        }
    }

}


