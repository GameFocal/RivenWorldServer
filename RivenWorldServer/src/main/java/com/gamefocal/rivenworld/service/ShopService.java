package com.gamefocal.rivenworld.service;

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
import com.gamefocal.rivenworld.game.items.food.consumable.Apple;
import com.gamefocal.rivenworld.game.items.food.consumable.Blueberry;
import com.gamefocal.rivenworld.game.items.food.consumable.Pear;
import com.gamefocal.rivenworld.game.items.food.consumable.Watermelon;
import com.gamefocal.rivenworld.game.items.placables.LandClaimItem;
import com.gamefocal.rivenworld.game.items.resources.econ.GoldCoin;
import com.gamefocal.rivenworld.game.items.resources.minerals.raw.CopperOre;
import com.gamefocal.rivenworld.game.items.resources.minerals.raw.GoldOre;
import com.gamefocal.rivenworld.game.items.resources.minerals.raw.IronOre;
import com.gamefocal.rivenworld.game.items.resources.minerals.raw.SilverOre;
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
import com.gamefocal.rivenworld.game.shops.GameShop;
import com.gamefocal.rivenworld.game.util.Location;
import com.gamefocal.rivenworld.models.GameShopModel;
import com.google.auto.service.AutoService;

import javax.inject.Singleton;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentHashMap;

@Singleton
@AutoService(HiveService.class)
public class ShopService implements HiveService<ShopService> {

    public static ConcurrentHashMap<String, Shop> shops = new ConcurrentHashMap<>();

    @Override
    public void init() {
        /*
         * Load Shops
         * */

        // General Store
        initShop(new Shop(GameShop.GENERAL_STORE.getUid(), "General Store",
                new ShopItem(StoneHatchet.class, 25, 5),
                new ShopItem(StonePickaxe.class, 25, 5),
                new ShopItem(LandClaimItem.class, 50, 5),
                new ShopItem(WoodBucket.class, 10, 5),
                new ShopItem(Torch.class, 10, 5)
        ));

        // Clothing
        initShop(new Shop(GameShop.CLOTHING_STORE.getUid(), "Clothing Store",
                new ShopItem(SimpleClothShirt.class, 10, 5),
                new ShopItem(FancyClothShirt.class, 20, 5),
                new ShopItem(SimpleClothLegs.class, 10, 5),
                new ShopItem(FancyClothLegs.class, 20, 5),
                new ShopItem(SimpleLeatherShirt.class, 10, 5),
                new ShopItem(MediumLeatherShirt.class, 20, 5),
                new ShopItem(HeavyLeatherShirt.class, 40, 5),
                new ShopItem(SimpleLeatherLegs.class, 10, 5),
                new ShopItem(MediumLeatherLegs.class, 20, 5),
                new ShopItem(HeavyLeatherLegs.class, 40, 5),
                new ShopItem(SimpleLeatherBoots.class, 10, 5),
                new ShopItem(FancyLeatherBoots.class, 20, 5),
                new ShopItem(ClothCap.class, 25, 5),
                new ShopItem(CloakHead.class, 10, 5)
        ));

        // Food
        initShop(new Shop(GameShop.FOOD_STORE.getUid(), "Farming Store",
                new ShopItem(Apple.class, 4, 5),
                new ShopItem(Pear.class, 6, 5),
                new ShopItem(Blueberry.class, 2, 5),
                new ShopItem(Watermelon.class, 10, 5)
        ));

        // Blacksmith
        initShop(new Shop(GameShop.BLACKSMITH_STORE.getUid(), "Blacksmith",
                new ShopItem(IronOre.class, 5, 5),
                new ShopItem(GoldOre.class, 10, 5),
                new ShopItem(CopperOre.class, 15, 5),
                new ShopItem(Oil.class, 20, 5),
                new ShopItem(IronIgnot.class, 10, 5),
                new ShopItem(GoldIgnot.class, 20, 5),
                new ShopItem(SteelIgnot.class, 30, 5),
                new ShopItem(SimpleIronShirt.class, 50, 5),
                new ShopItem(MediumIronShirt.class, 75, 5),
                new ShopItem(ChainmailShirt.class, 125, 5),
                new ShopItem(SimpleIronBoots.class, 50, 5),
                new ShopItem(MediumIronBoots.class, 75, 5),
                new ShopItem(HeavyIronBoots.class, 125, 5),
                new ShopItem(SimpleIronLegs.class, 50, 5),
                new ShopItem(MediumIronLegs.class, 75, 5),
                new ShopItem(HeavyIronLegs.class, 125, 5),
                new ShopItem(CrusiaderShirt.class, 150, 5),
                new ShopItem(SteelPlateShirt.class, 200, 5),
                new ShopItem(HeavySteelPlateShirt.class, 250, 5),
                new ShopItem(SteelBoots.class, 125, 5)
        ));

        // Lumber
        initShop(new Shop(GameShop.LUMBER_STORE.getUid(), "Lumber",
                new ShopItem(WoodStick.class, 2, 5),
                new ShopItem(WoodLog.class, 10, 5),
                new ShopItem(WoodPlank.class, 2, 5),
                new ShopItem(WoodenClub.class, 4, 5)
        ));

        loadShopsFromDb();

        ClaimService.lockChunksBetween(
                Location.fromString("75.0,43.0,0.0,0.0,0.0,0.0"),
                Location.fromString("72.0,49.0,0.0,0.0,0.0,0.0")
        );
    }

    public static int getCoins(Inventory inventory) {
        int a = 0;
        a += inventory.amtOfType(GoldCoin.class);

        // TODO: Add Bags of Coins (Later)

        return a;
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


