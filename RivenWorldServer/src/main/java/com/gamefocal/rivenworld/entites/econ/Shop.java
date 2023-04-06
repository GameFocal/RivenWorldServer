package com.gamefocal.rivenworld.entites.econ;

import com.gamefocal.rivenworld.DedicatedServer;
import com.gamefocal.rivenworld.game.entites.living.NPC;
import com.gamefocal.rivenworld.service.EnvironmentService;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.Arrays;
import java.util.LinkedList;

public class Shop {

    private String uid;

    private String name;

    private LinkedList<ShopItem> items = new LinkedList<>();

    private float open = -1;

    private float close = -1;

    public Shop(String uid, String name) {
        this.name = name;
        this.uid = uid;
    }

    public Shop(String uid, String name, ShopItem... items) {
        this.name = name;
        this.uid = uid;
        this.items.addAll(Arrays.asList(items));
    }

    public void addShopItem(ShopItem item) {
        this.items.add(item);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LinkedList<ShopItem> getItems() {
        return items;
    }

    public void setItems(LinkedList<ShopItem> items) {
        this.items = items;
    }

    public float getOpen() {
        return open;
    }

    public void setOpen(float open) {
        this.open = open;
    }

    public float getClose() {
        return close;
    }

    public void setClose(float close) {
        this.close = close;
    }

    public boolean isOpen() {
        float time = DedicatedServer.get(EnvironmentService.class).getDayPercent();

        if (this.open > 0 && this.close > 0) {
            return (time >= this.open && time <= this.close);
        }

        return true;
    }

    public JsonObject toJson() {
        JsonObject o = new JsonObject();
        o.addProperty("uid", uid);
        o.addProperty("name", this.name);
        o.addProperty("isOpen", this.isOpen());

        JsonArray items = new JsonArray();
        for (ShopItem i : this.items) {
            items.add(i.toJson());
        }

        o.add("items", items);

        return o;
    }

}
