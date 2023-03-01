package com.gamefocal.rivenworld.game.ui.inventory;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.interactable.InteractAction;
import com.gamefocal.rivenworld.game.inventory.Inventory;
import com.gamefocal.rivenworld.game.ui.GameUI;
import com.google.gson.JsonObject;

public class RivenInventoryUI extends GameUI<Inventory> {

    public RivenInventoryUI() {
        this.focus = false;
    }

    @Override
    public String name() {
        return "riven-inv";
    }

    @Override
    public JsonObject data(HiveNetConnection connection, Inventory obj) {
//        JsonObject data = InventoryUtil.inventoryToJson(obj);
        System.out.println(obj.toJson().toString());
        return obj.toJson();
    }

    @Override
    public void onOpen(HiveNetConnection connection, Inventory object) {
    }

    @Override
    public void onClose(HiveNetConnection connection, Inventory object) {

    }

    @Override
    public void onAction(HiveNetConnection connection, InteractAction action, String tag, String[] data) {
        if (tag.equalsIgnoreCase("close")) {
            this.close(connection);
        } else if(tag.equalsIgnoreCase("mv")) {
            String fromSlot = data[0];
            String toSlot = data[1];

            // TODO: Move the move logic into the Inventory class for easier calling :)

        }
    }
}
