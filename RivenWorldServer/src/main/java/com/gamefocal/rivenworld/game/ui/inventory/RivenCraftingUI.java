package com.gamefocal.rivenworld.game.ui.inventory;

import com.gamefocal.rivenworld.DedicatedServer;
import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.entites.generics.CraftingStation;
import com.gamefocal.rivenworld.game.interactable.InteractAction;
import com.gamefocal.rivenworld.game.inventory.Inventory;
import com.gamefocal.rivenworld.game.inventory.crafting.CraftingQueue;
import com.gamefocal.rivenworld.game.ui.CraftingUI;
import com.gamefocal.rivenworld.game.ui.GameUI;
import com.gamefocal.rivenworld.game.util.Location;
import com.gamefocal.rivenworld.service.InventoryService;
import com.google.gson.JsonObject;

public class RivenCraftingUI extends GameUI<CraftingStation> implements CraftingUI {

    public boolean hasFuel = false;
    public boolean isOn = false;
    protected HiveNetConnection connection;

    @Override
    public String name() {
        return "crafting";
    }

    @Override
    public JsonObject data(HiveNetConnection connection, CraftingStation obj) {
        JsonObject o = new JsonObject();

        JsonObject dest = this.getAttached().dest().toJson();
        o.add("station", dest);
        o.add("crafting", this.queue().toJson(connection.getPlayer().inventory));

        o.addProperty("hasFuel", obj.hasFuel());

        if (obj.hasFuel()) {
            o.addProperty("isOn", obj.isOn());
            o.add("fuel", this.getAttached().fuel().toJson());
        }

        return o;
    }

    @Override
    public void onOpen(HiveNetConnection connection, CraftingStation object) {
        this.connection = connection;
        connection.updatePlayerInventory();
        DedicatedServer.get(InventoryService.class).trackInventory(connection.getPlayer().inventory);
    }

    @Override
    public void onClose(HiveNetConnection connection, CraftingStation object) {

    }


    @Override
    public void onAction(HiveNetConnection connection, InteractAction action, String tag, String[] data) {
        if (tag.equalsIgnoreCase("close")) {
            this.close(connection);
        } else if (tag.equalsIgnoreCase("OnOff")) {
            this.getAttached().toggleOnOff(connection);
        }
    }

    @Override
    public CraftingQueue queue() {
        return this.getAttached().dest().getCraftingQueue();
    }

    @Override
    public Inventory getSource() {
        return this.connection.getPlayer().inventory;
    }

    @Override
    public Inventory getDest() {
        return this.getAttached().dest();
    }

    @Override
    public Location getLocation() {
        return this.getAttached().getLocation();
    }
}
