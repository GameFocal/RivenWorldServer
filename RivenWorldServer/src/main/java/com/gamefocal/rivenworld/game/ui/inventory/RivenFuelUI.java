package com.gamefocal.rivenworld.game.ui.inventory;

import com.gamefocal.rivenworld.DedicatedServer;
import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.entites.generics.CraftingStation;
import com.gamefocal.rivenworld.game.entites.generics.FuelEntity;
import com.gamefocal.rivenworld.game.interactable.InteractAction;
import com.gamefocal.rivenworld.game.inventory.Inventory;
import com.gamefocal.rivenworld.game.inventory.crafting.CraftingQueue;
import com.gamefocal.rivenworld.game.ui.CraftingUI;
import com.gamefocal.rivenworld.game.ui.GameUI;
import com.gamefocal.rivenworld.game.util.Location;
import com.gamefocal.rivenworld.service.InventoryService;
import com.gamefocal.rivenworld.service.SaveService;
import com.google.gson.JsonObject;

public class RivenFuelUI extends GameUI<FuelEntity> {

    public boolean isOn = false;
    protected HiveNetConnection connection;

    public RivenFuelUI() {
    }

    @Override
    public String name() {
        return "fuel";
    }

    @Override
    public JsonObject data(HiveNetConnection connection, FuelEntity fuel) {
        SaveService.queueForSave(fuel.getModel());

        connection.updatePlayerInventory();
        JsonObject o = new JsonObject();
        o.addProperty("on", fuel.isOn());
        o.add("fuel", fuel.getFuel().toJson());
        return o;
    }

    @Override
    public void onOpen(HiveNetConnection connection, FuelEntity object) {
        DedicatedServer.get(InventoryService.class).trackInventory(connection.getPlayer().inventory);
        DedicatedServer.get(InventoryService.class).trackInventory(object.getFuel());

        connection.getPlayer().inventory.attachToUI(this);
        object.getFuel().attachToUI(this);
    }

    @Override
    public void onClose(HiveNetConnection connection, FuelEntity object) {
        connection.getPlayer().inventory.detachFromUI(this);
        object.getFuel().detachFromUI(this);
    }

    @Override
    public void onAction(HiveNetConnection connection, InteractAction action, String tag, String[] data) {
        if (tag.equalsIgnoreCase("close")) {
            this.close(connection);
        } else if (tag.equalsIgnoreCase("OnOff")) {
            this.getAttached().toggleOnOff();
        }
    }
}
