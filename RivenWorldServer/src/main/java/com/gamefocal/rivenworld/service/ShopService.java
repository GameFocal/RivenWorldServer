package com.gamefocal.rivenworld.service;

import com.gamefocal.rivenworld.entites.econ.Shop;
import com.gamefocal.rivenworld.entites.econ.ShopItem;
import com.gamefocal.rivenworld.entites.service.HiveService;
import com.gamefocal.rivenworld.game.items.food.consumable.Apple;
import com.gamefocal.rivenworld.game.items.food.consumable.Pear;
import com.gamefocal.rivenworld.game.items.placables.LandClaimItem;
import com.gamefocal.rivenworld.game.items.weapons.PickAxe.StonePickaxe;
import com.gamefocal.rivenworld.game.items.weapons.WoodBucket;
import com.gamefocal.rivenworld.game.items.weapons.hatchets.StoneHatchet;
import com.gamefocal.rivenworld.models.GameShopModel;
import com.google.auto.service.AutoService;
import org.apache.commons.codec.digest.DigestUtils;

import javax.inject.Singleton;
import java.sql.SQLException;
import java.util.List;
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
        shops.put("GeneralStore", new Shop("gs", "General Store",
                new ShopItem(StoneHatchet.class, 25, 5)
        ));
    }

    public void loadShop(String uid) {

    }

}
