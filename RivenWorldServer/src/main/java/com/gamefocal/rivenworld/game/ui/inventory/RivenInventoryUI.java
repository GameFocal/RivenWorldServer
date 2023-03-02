package com.gamefocal.rivenworld.game.ui.inventory;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.interactable.InteractAction;
import com.gamefocal.rivenworld.game.inventory.Inventory;
import com.gamefocal.rivenworld.game.inventory.InventoryStack;
import com.gamefocal.rivenworld.game.inventory.enums.EquipmentSlot;
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
//        System.out.println(obj.toJson().toString());

        JsonObject o = new JsonObject();
        o.add("inv", obj.toJson());
//        o.add("eq", connection.getPlayer().equipmentSlots.toJson());

        return o;
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
        } else if (tag.equalsIgnoreCase("mv")) {
            String fromSlot = data[0];
            String toSlot = data[1];
            this.getAttached().transferToSlot(Integer.parseInt(fromSlot), Integer.parseInt(toSlot));
            this.update(connection);
        } else if (tag.equalsIgnoreCase("drop")) {
            String fromSlot = data[0];
            this.getAttached().dropCompleteSlot(connection, Integer.parseInt(fromSlot));
            this.update(connection);
        } else if (tag.equalsIgnoreCase("split")) {
            String slot = data[0];
            String toSlot = data[1];
            this.getAttached().split(Integer.parseInt(data[0]), Integer.parseInt(toSlot));
            this.update(connection);
        } else if (tag.equalsIgnoreCase("eq")) {

            int fromSlot = Integer.parseInt(data[0]);
            String toSlot = data[1];

            InventoryStack from = this.getAttached().get(fromSlot);
            if (from != null) {
                if (from.getItem().isEquipable()) {
                    // Can be equiped
                    try {
                        EquipmentSlot slot = EquipmentSlot.valueOf(toSlot);
                        connection.getPlayer().equipmentSlots.setBySlotName(slot, from);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

        } else if (tag.equalsIgnoreCase("ueq")) {
            System.out.println("UNEQ");
        }
    }
}
