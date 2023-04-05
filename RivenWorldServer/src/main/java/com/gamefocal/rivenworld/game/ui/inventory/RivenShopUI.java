package com.gamefocal.rivenworld.game.ui.inventory;

import com.gamefocal.rivenworld.DedicatedServer;
import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.entites.generics.FuelEntity;
import com.gamefocal.rivenworld.game.entites.living.NPC;
import com.gamefocal.rivenworld.game.interactable.InteractAction;
import com.gamefocal.rivenworld.game.ui.GameUI;
import com.gamefocal.rivenworld.service.InventoryService;
import com.google.gson.JsonObject;

public class RivenShopUI extends GameUI<NPC> {

    public RivenShopUI() {
    }

    @Override
    public String name() {
        return "npc-shop";
    }

    @Override
    public JsonObject data(HiveNetConnection connection, NPC obj) {



        return null;
    }

    @Override
    public void onOpen(HiveNetConnection connection, NPC object) {

    }

    @Override
    public void onClose(HiveNetConnection connection, NPC object) {

    }

    @Override
    public void onAction(HiveNetConnection connection, InteractAction action, String tag, String[] data) {

    }

}
