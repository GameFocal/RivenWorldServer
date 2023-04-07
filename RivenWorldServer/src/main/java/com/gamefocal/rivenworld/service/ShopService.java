package com.gamefocal.rivenworld.service;

import com.gamefocal.rivenworld.entites.econ.Shop;
import com.gamefocal.rivenworld.entites.econ.ShopItem;
import com.gamefocal.rivenworld.entites.service.HiveService;
import com.gamefocal.rivenworld.game.inventory.Inventory;
import com.gamefocal.rivenworld.game.items.resources.econ.GoldCoin;
import com.gamefocal.rivenworld.game.items.weapons.hatchets.StoneHatchet;
import com.gamefocal.rivenworld.game.shops.GameShop;
import com.gamefocal.rivenworld.models.GameShopModel;
import com.google.auto.service.AutoService;

import javax.inject.Singleton;
import java.sql.SQLException;
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
        initShop(new Shop(GameShop.GENERAL_STORE.getUid(), "General Store",
                new ShopItem(StoneHatchet.class, 25, 5)
        ));
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
        loadShopFromDb(shop.getName());
        shops.put(shop.getUid(), shop);
        saveShopToDb(shop);
    }

    public void loadShopFromDb(String shopId) {
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

}


