package com.gamefocal.rivenworld.entites.econ;

import com.gamefocal.rivenworld.DedicatedServer;
import com.gamefocal.rivenworld.game.entites.living.NPC;
import com.gamefocal.rivenworld.service.EnvironmentService;

import java.util.Arrays;
import java.util.LinkedList;

public class Shop {

    private String name;

    private LinkedList<ShopItem> items = new LinkedList<>();

    private float open = -1;

    private float close = -1;

    public Shop(String name, ShopItem... items) {
        this.name = name;
        this.items.addAll(Arrays.asList(items));
    }

    public boolean isOpen() {
        float time = DedicatedServer.get(EnvironmentService.class).getDayPercent();

        if (this.open > 0 && this.close > 0) {
            return (time >= this.open && time <= this.close);
        }

        return true;
    }
}
