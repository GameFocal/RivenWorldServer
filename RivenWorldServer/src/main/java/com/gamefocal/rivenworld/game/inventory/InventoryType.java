package com.gamefocal.rivenworld.game.inventory;

public enum InventoryType {

    NONE("None"),
    PLAYER("Player"),
    CONTAINER("Storage container"),
    WORKBENCH("Workbench"),
    COOKING_POT("Cooking Pot"),
    FURNACE("Furnace"),
    VENDOR("Vendor"),
    REFRIGERATOR("Refrigerator"),
    LOOT_CHEST("Loot Chest"),
    FORGE("Forge"),
    SMELTER("Smelter"),
    GRIND_STONE("Grindstone"),
    TANNING_RACK("Tanning Rack"),
    CAMPFIRE("Campfire"),
    ALCHEMY("Alchemy Lab"),
    MORTAR_PESTLE("Mortar and Pestle"),
    RECYCLE("Recycler"),
    COMPOST("Compost Bin");

    private String unrealName = "None";

    InventoryType(String unrealName) {
        this.unrealName = unrealName;
    }

    public String getUnrealName() {
        return unrealName;
    }

    public byte index() {
        for (int i = 0; i < values().length; i++) {
            if (values()[i] == this) {
                return (byte) i;
            }
        }

        return 0;
    }
}
