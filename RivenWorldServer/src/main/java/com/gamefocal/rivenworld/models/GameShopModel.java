package com.gamefocal.rivenworld.models;

import com.gamefocal.rivenworld.entites.econ.Shop;
import com.gamefocal.rivenworld.game.inventory.InventoryItem;
import com.gamefocal.rivenworld.serializer.JsonDataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "game_shop_stock")
public class GameShopModel {

    @DatabaseField(id = true)
    public String id;

    @DatabaseField
    public String shopId;

    @DatabaseField
    public String itemClass;

    @DatabaseField(persisterClass = JsonDataType.class)
    public InventoryItem item;

    @DatabaseField
    public int stock = 0;

}
