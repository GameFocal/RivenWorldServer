package com.gamefocal.rivenworld.game.inventory;

import com.gamefocal.rivenworld.DedicatedServer;
import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.entites.storage.DropBag;
import com.gamefocal.rivenworld.game.exceptions.InventoryOwnedAlreadyException;
import com.gamefocal.rivenworld.game.inventory.crafting.CraftingQueue;
import com.gamefocal.rivenworld.game.inventory.enums.EquipmentSlot;
import com.gamefocal.rivenworld.game.ui.GameUI;
import com.gamefocal.rivenworld.game.util.Location;
import com.gamefocal.rivenworld.service.InventoryService;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.Serializable;
import java.util.*;

public class Inventory implements Serializable {

    private String name = "Inventory";

    private boolean hasHotBar = false;

    private int hotBarSize = 9;

    private boolean isLocked = false;

    private int storageSpace = 16;

    private int maxStack = 64;

    private UUID uuid;

    private InventoryType type = InventoryType.PLAYER;

    private transient HiveNetConnection owner = null;

    private String gameRef = "self";

    private InventoryStack[] items = new InventoryStack[0];

    private UUID attachedEntity = null;

    private CraftingQueue craftingQueue = new CraftingQueue(6);

    private boolean hasEquipment = false;

    private boolean hasOnOff = false;

    private boolean isVendor = false;

    private boolean isLootChest = false;

    private transient LinkedList<GameUI> attachedUIs = new LinkedList<>();

    private HashMap<String, String> tags = new HashMap<>();

    private int hotBarSelection = 0;

    private InventoryInterface attachedToInterface = null;

    private boolean showZeroItems = false;

    public Inventory(int storageSpace) {
        this.storageSpace = storageSpace;
        this.items = new InventoryStack[this.storageSpace];
        this.uuid = UUID.randomUUID();
    }

    public Inventory(InventoryStack[] items) {
        this.items = items;
        this.storageSpace = this.items.length;
        this.uuid = UUID.randomUUID();
    }

    public Inventory(InventoryType type, String name, String gameRef, int storageSpace) {
        this.type = type;
        this.name = name;
        this.gameRef = gameRef;
        this.storageSpace = storageSpace;
        this.items = new InventoryStack[this.storageSpace];
        this.uuid = UUID.randomUUID();
    }

    public Inventory(InventoryType type, String name, String gameRef, int storageSpace, int craftingQueueSize) {
        this.type = type;
        this.name = name;
        this.gameRef = gameRef;
        this.storageSpace = storageSpace;
        this.items = new InventoryStack[this.storageSpace];
        this.uuid = UUID.randomUUID();
        if (craftingQueueSize > 0) {
            this.craftingQueue = new CraftingQueue(craftingQueueSize);
        }
    }

    public Inventory(InventoryType type, String name, String gameRef, InventoryStack[] items) {
        this.type = type;
        this.name = name;
        this.gameRef = gameRef;
        this.items = items;
        this.storageSpace = this.items.length;
        this.uuid = UUID.randomUUID();
    }

    public void setShowZeroItems(boolean showZeroItems) {
        this.showZeroItems = showZeroItems;
    }

    public InventoryInterface getAttachedToInterface() {
        return attachedToInterface;
    }

    public void setAttachedToInterface(InventoryInterface attachedToInterface) {
        this.attachedToInterface = attachedToInterface;
    }

    public void attachToUI(GameUI ui) {
        if (this.attachedUIs == null) {
            this.attachedUIs = new LinkedList<>();
        }

        if (!this.attachedUIs.contains(ui)) {
            this.attachedUIs.add(ui);
        }
    }

    public void detachFromUI(GameUI ui) {
        if (this.attachedUIs == null) {
            this.attachedUIs = new LinkedList<>();
        }

        this.attachedUIs.remove(ui);
    }

    public void clearAttachedUIs() {
        this.attachedUIs.clear();
    }

    public void updateUIs(HiveNetConnection connection) {
        for (GameUI ui : this.attachedUIs) {
            ui.update(connection);
        }
    }

    public void updateUIs() {
        if (this.attachedUIs != null && this.attachedUIs.size() > 0) {
            for (GameUI ui : this.attachedUIs) {
                if (ui.getOwner() != null) {
                    ui.update(ui.getOwner());
                }
            }
        }
    }

    public boolean isHasHotBar() {
        return hasHotBar;
    }

    public void setHasHotBar(boolean hasHotBar) {
        this.hasHotBar = hasHotBar;
    }

    public int getHotBarSize() {
        return hotBarSize;
    }

    public void setHotBarSize(int hotBarSize) {
        this.hotBarSize = hotBarSize;
    }

    public HashMap<String, String> getTags() {
        return tags;
    }

    public void setTags(HashMap<String, String> tags) {
        this.tags = tags;
    }

    public boolean isHasEquipment() {
        return hasEquipment;
    }

    public void setHasEquipment(boolean hasEquipment) {
        this.hasEquipment = hasEquipment;
    }

    public boolean isHasOnOff() {
        return hasOnOff;
    }

    public void setHasOnOff(boolean hasOnOff) {
        this.hasOnOff = hasOnOff;
    }

    public boolean isVendor() {
        return isVendor;
    }

    public void setVendor(boolean vendor) {
        isVendor = vendor;
    }

    public boolean isLootChest() {
        return isLootChest;
    }

    public void setLootChest(boolean lootChest) {
        isLootChest = lootChest;
    }

    public void add(InventoryItem item) {
        InventoryStack stack = new InventoryStack(item);
        this.add(stack);
    }

    public void add(InventoryItem item, int amt) {
        InventoryStack stack = new InventoryStack(item, amt);
        this.add(stack);
    }

    public boolean canAdd(InventoryStack stack) {

        if (stack == null) {
            return false;
        }

        ArrayList<InventoryStack> existingStacks = new ArrayList<>();

        for (InventoryStack s : this.items) {
            if (s != null && stack != null && s.getAmount() > 0) {
                if (s.getHash().equalsIgnoreCase(stack.getHash()) && s.getItem().isStackable) {
                    existingStacks.add(s);
                }
            }
        }

        int amtToAdd = stack.getAmount();
        for (InventoryStack existing : existingStacks) {
            if (existing != null) {
                if (amtToAdd <= 0) {
                    break;
                }

                int toAddToStack = this.maxStack - existing.getAmount();

                if (amtToAdd < toAddToStack) {
                    toAddToStack = amtToAdd;
                }

                amtToAdd -= toAddToStack;
            }
        }

        while (amtToAdd > 0 && this.hasEmptySlot()) {
            for (int i = 0; i < this.items.length; i++) {
                if (this.items[i] == null || (this.items[i] != null && this.items[i].getAmount() <= 0)) {

                    int toAdd = amtToAdd;
                    if (toAdd > this.maxStack) {
                        toAdd = this.maxStack;
                    }

                    amtToAdd -= toAdd;
                    break;
                }
            }
        }

        return amtToAdd <= 0;
    }

    public int getEmptySlotCount() {
        int empty = 0;
        for (InventoryStack s : this.items) {
            if (s == null || s.getAmount() <= 0) {
                empty++;
            }
        }

        return empty;
    }

    public boolean hasEmptySlot() {
        for (InventoryStack s : this.items) {
            if (s == null || s.getAmount() <= 0) {
                return true;
            }
        }

        return false;
    }

    public void addToEmptySlot(InventoryStack stack) {
        // None found add a new stack
        for (int i = 0; i < this.items.length; i++) {
            if (this.items[i] == null || (this.items[i] != null && this.items[i].getAmount() <= 0)) {
                this.items[i] = stack;
                break;
            }
        }
    }

    public void addToEmptySlot(InventoryItem item) {
        this.addToEmptySlot(new InventoryStack(item, 1));
    }

    public void addToEmptySlot(InventoryItem item, int amt) {
        this.addToEmptySlot(new InventoryStack(item, amt));
    }

    public void add(InventoryStack stack) {

        if (stack == null) {
            return;
        }

        int amtToAdd = stack.getAmount();
        if (stack.getItem().isStackable && !stack.getItem().hasDurability) {
            InventoryStack currentStack = null;

            ArrayList<InventoryStack> existingStacks = new ArrayList<>();

            for (InventoryStack s : this.items) {
                if (s != null && stack != null && s.getAmount() > 0) {
                    if (s.getHash().equalsIgnoreCase(stack.getHash()) && s.getItem().isStackable) {
                        existingStacks.add(s);
//                    currentStack = s;
//                    break;
                    }
                }
            }

            for (InventoryStack existing : existingStacks) {
                if (existing != null) {
                    if (amtToAdd <= 0) {
                        break;
                    }

                    int toAddToStack = this.maxStack - existing.getAmount();

                    if (amtToAdd < toAddToStack) {
                        toAddToStack = amtToAdd;
                    }

                    existing.add(toAddToStack);
                    amtToAdd -= toAddToStack;
                }
            }
        }

        while (amtToAdd > 0 && this.hasEmptySlot()) {
            for (int i = 0; i < this.items.length; i++) {
                if (this.items[i] == null || (this.items[i] != null && this.items[i].getAmount() <= 0)) {

                    int toAdd = amtToAdd;
                    if (toAdd > this.maxStack) {
                        toAdd = this.maxStack;
                    }

                    this.items[i] = new InventoryStack(stack.getItem(), toAdd);
                    amtToAdd -= toAdd;
                    break;
                }
            }
        }

        this.update();
    }

    public boolean isEmpty(int slot) {
        return this.items[slot] == null;
    }

    public boolean isEmpty() {
        int items = 0;
        for (InventoryStack s : this.items) {
            if (s != null && s.getAmount() > 0) {
                items += s.getAmount();
            }
        }

        return (items == 0);
    }

    public InventoryStack get(int index) {
        if (this.isEmpty(index)) {
            return null;
        }

        return this.items[index];
    }

    public void clear(int index) {
        this.items[index] = null;
        this.update();
    }

    public void updateCount(int index, int amt) {
        this.items[index].setAmount(amt);
        if (this.items[index].getAmount() == 0) {
            this.clear(index);
        }
    }

    public void addToSlot(int index, int amt) {
        this.items[index].setAmount(this.items[index].getAmount() + amt);
        if (this.items[index].getAmount() > 64) {
            this.items[index].setAmount(64);
        }
    }

    public void clearInv() {
        this.items = new InventoryStack[this.storageSpace];
    }

    public boolean hasOfType(Class<? extends InventoryItem> t) {
        for (InventoryStack s : this.items) {
            if (s != null && s.getAmount() > 0) {
                if (t.isAssignableFrom(s.getItem().getClass())) {
                    return true;
                }
            }
        }

        return false;
    }

    public List<InventoryStack> getOfType(Class<? extends InventoryItem> t) {
        List<InventoryStack> s = new ArrayList<>();
        for (InventoryStack ss : this.items) {
            if (ss != null && ss.getAmount() > 0) {
                if (t.isAssignableFrom(ss.getItem().getClass())) {
                    s.add(ss);
                }
            }
        }

        return s;
    }

    public int amtOfType(Class<? extends InventoryItem> t) {
        int amt = 0;
        List<InventoryStack> s = new ArrayList<>();
        for (InventoryStack ss : this.items) {
            if (ss != null && ss.getAmount() > 0) {
                if (t.isAssignableFrom(ss.getItem().getClass())) {
                    amt += ss.getAmount();
                }
            }
        }

        return amt;
    }

    public boolean removeRecipeItems(CraftingRecipe recipe) {
        for (Map.Entry<Class<? extends InventoryItem>, Integer> e : recipe.getRequires().entrySet()) {
            int rm = this.removeOfType(e.getKey(), e.getValue());
            if (rm < e.getValue()) {
                return false;
            }
        }

        return true;
    }

    public int removeOfType(Class<? extends InventoryItem> t, int amt) {
        int tmp = amt;

        for (InventoryStack s : this.getOfType(t)) {
            if (tmp > 0) {
                if (s.getAmount() > tmp) {
                    s.setAmount(s.getAmount() - tmp);
                    tmp -= tmp;
                } else {
                    tmp -= s.getAmount();
                    s.setAmount(0);
                }
            }
        }

        this.update();

        return (amt - tmp);
    }

    public void update() {
        // Update the inventory on the server.

        int i = 0;
        for (InventoryStack s : this.items) {
            if (s != null) {
                if (s.getAmount() > this.maxStack) {
                    s.setAmount(this.maxStack);
                } else if (s.getAmount() <= 0 && !showZeroItems) {
                    this.items[i] = null;
                }
            }
            i++;
        }

        if (this.attachedToInterface != null) {
            this.attachedToInterface.onInventoryUpdate(this);
        }

        // Sync to tracking is exist
        DedicatedServer.get(InventoryService.class).trackInventory(this);
    }

    public void set(int index, InventoryStack stack) {
        this.items[index] = stack;
    }

    public int getStorageSpace() {
        return storageSpace;
    }

    public void setStorageSpace(int storageSpace) {
        this.storageSpace = storageSpace;
    }

    public int getMaxStack() {
        return maxStack;
    }

    public void setMaxStack(int maxStack) {
        this.maxStack = maxStack;
    }

    public InventoryStack[] getItems() {
        return items;
    }

    public void setItems(InventoryStack[] items) {
        this.items = items;
    }

    public void releaseOwnership() {
        this.owner = null;
    }

    public void takeOwnership(HiveNetConnection connection) throws InventoryOwnedAlreadyException {
        this.takeOwnership(connection, false);
    }

    public void takeOwnership(HiveNetConnection connection, boolean override) throws InventoryOwnedAlreadyException {
        if (this.owner == null || override) {
            this.owner = connection;
        } else {
            throw new InventoryOwnedAlreadyException();
        }
    }

    public boolean hasOwner() {
        return this.owner != null;
    }

    public boolean isOwner(HiveNetConnection connection) {
        if (this.hasOwner()) {
            return (this.owner.getUuid() == connection.getUuid());
        }

        return false;
    }

    public UUID getUuid() {
        return uuid;
    }

    public String getName() {
        return name;
    }

    public Inventory setName(String name) {
        this.name = name;
        return this;
    }

    public boolean isLocked() {
        return isLocked;
    }

    public Inventory setLocked(boolean locked) {
        isLocked = locked;
        return this;
    }

    public InventoryType getType() {
        return type;
    }

    public void setType(InventoryType type) {
        this.type = type;
    }

    public HiveNetConnection getOwner() {
        return owner;
    }

    public UUID getAttachedEntity() {
        return this.attachedEntity;
    }

    public void setAttachedEntity(UUID attachedEntity) {
        this.attachedEntity = attachedEntity;
    }

    public int getSlotIndexByUUID(UUID uuid) {
        int i = 0;
        for (InventoryStack s : this.items) {
            if (s != null) {
                if (s.getItem().getItemUUID() == uuid) {
                    return i;
                }
            }
            i++;
        }

        return -1;
    }

    public boolean canCraft() {
        return this.craftingQueue != null;
    }

    public CraftingQueue getCraftingQueue() {
        return craftingQueue;
    }

    public void setCraftingQueue(CraftingQueue craftingQueue) {
        this.craftingQueue = craftingQueue;
    }

    public int canCraftAmt(CraftingRecipe recipe) {

        LinkedList<Float> values = new LinkedList<>();

        for (Map.Entry<Class<? extends InventoryItem>, Integer> e : recipe.getRequires().entrySet()) {
            float hasAmount = this.amtOfType(e.getKey());
            if (hasAmount <= 0) {
                return 0;
            }

            if (hasAmount < e.getValue()) {
                return 0;
            }

            float canMake = (float) Math.floor(hasAmount / e.getValue());

            if (canMake > 0) {
                values.add(canMake);
            }
        }

        float minNumber = Float.MAX_VALUE;
        for (float v : values) {
            if (v < minNumber) {
                minNumber = v;
            }
        }

        return (int) minNumber;
    }

    public JsonObject toJson() {
        JsonObject o = new JsonObject();
        o.addProperty("name", this.name);
        o.addProperty("id", this.uuid.toString());
        o.addProperty("hotbar", this.hasHotBar);
        o.addProperty("hotbarsize", this.hotBarSize);
        o.addProperty("hotbarselect", this.hotBarSelection);
        o.addProperty("showZero", this.showZeroItems);
        JsonArray a = new JsonArray();
        for (InventoryStack s : this.items) {
            if (s != null && s.getItem() != null) {
                a.add(s.toJson());
            } else {
                JsonObject e = new JsonObject();
                e.addProperty("amt", 0);
                a.add(e);
            }
        }
        o.add("items", a);
        return o;
    }

    public void transferToSlot(int fromSlot, int toSlot) {
        if (!this.isLocked) {
            // Can transfer
            InventoryStack from = this.get(fromSlot);
//            InventoryStack to = this.get(toSlot);
            if (from != null) {

                InventoryStack n = this.moveItem(from, toSlot, false);
                this.set(fromSlot, n);

                // Has something to move
//                if (to == null) {
//                    this.set(toSlot, from);
//                    this.clear(fromSlot);
//                } else {
//                    this.set(toSlot, from);
//                    this.set(fromSlot, to);
//                }
            }
        }
    }

    public void transferToInventory(Inventory inventory, int fromLocalSlot, int toRemoteSlot, boolean split) {
        if (!inventory.isLocked()) {
            InventoryStack fromSlot = this.get(fromLocalSlot);
            InventoryStack n = inventory.moveItem(fromSlot, toRemoteSlot, split);
            this.set(fromLocalSlot, n);
        }
    }

    public InventoryStack moveItem(InventoryStack stack, int slot, boolean split) {

        int amt = stack.getAmount();
        if (split) {
            amt = stack.getAmount() / 2;
        }

//        System.out.println("MV AMT: " + amt);

        InventoryStack from = new InventoryStack(stack.getItem(), amt);
        InventoryStack to = this.get(slot);
        if (to == null || to.getAmount() == 0) {
            // just add it all
            this.set(slot, from);

//            System.out.println("Is Empty: " + ((split) ? (stack.getAmount() - amt) : "NULL"));

            return (split) ? stack.remove(amt) : null;
        } else {
            // Has something there

            if (stack.getHash().equalsIgnoreCase(to.getHash()) && stack.getItem().isStackable && !stack.getItem().hasDurability) {
                // Is the same item, try to add to this stack

                int canAdd = this.maxStack - to.getAmount();
                if (canAdd > amt) {
                    canAdd = amt;
                }

                to.add(canAdd);

                stack.remove(canAdd);

//                System.out.println("Same Hash: " + stack.getAmount());

                return stack;
            } else {

//                System.out.println("SAWP");

                this.set(slot, from);
                return to;
            }
        }
    }

    public void split(int slot, int toSlot) {
        InventoryStack fromStack = this.get(slot);
        if (fromStack != null) {
            InventoryStack n = this.moveItem(fromStack, toSlot, true);

            this.set(slot, n);
            this.update();
        }
    }

    public void resize(int add) {
        InventoryStack[] newItems = new InventoryStack[this.items.length + add];
        System.arraycopy(this.items, 0, newItems, 0, this.items.length);
        this.items = newItems;
    }

    public void resizeToAmt(int amt) throws Exception {
        if (amt > this.items.length) {
            InventoryStack[] newItems = new InventoryStack[amt];
            System.arraycopy(this.items, 0, newItems, 0, this.items.length);
            this.items = newItems;
        } else {
            throw new Exception("New Inventory size is smaller than the current");
        }
    }

    public void addAllFromInventory(Inventory inventory, boolean growToFit) throws Exception {
        for (InventoryStack s : inventory.getItems()) {
            if (!this.canAdd(s)) {
                if (growToFit) {
                    this.resize(1);
                } else {
                    throw new Exception("Inventory will not hold this");
                }
            }

            this.add(s);
        }
    }

    public void dropCompleteSlot(HiveNetConnection connection, int slotNumber) {
        InventoryStack newStack = this.get(slotNumber);

        this.clear(slotNumber);

        if (newStack != null) {
            List<DropBag> bags = DedicatedServer.instance.getWorld().getEntitesOfTypeWithinRadius(DropBag.class, connection.getPlayer().location, 1000);

            boolean isPlaced = false;
            for (DropBag b : bags) {
                if (b.getDroppedBy() == connection.getUuid()) {
                    // Is the same player
                    if (b.getInventory().canAdd(newStack)) {
                        b.getInventory().add(newStack);
                        isPlaced = true;
                        break;
                    }
                }
            }

            if (!isPlaced) {
                // Spawn a new bag here
                Location dropLocation = DedicatedServer.instance.getWorld().getNearbyLocationWithNoCollision(connection.getPlayer().location, 200);
                dropLocation = DedicatedServer.instance.getWorld().getRawHeightmap().getHeightLocationFromLocation(dropLocation);

                DedicatedServer.instance.getWorld().spawn(new DropBag(connection, newStack), dropLocation);
            }

            this.update();
        }
    }

    public boolean equipFromSlot(HiveNetConnection connection, int fromSlot, EquipmentSlot slot) {

        InventoryStack from = this.get(fromSlot);
        if (from != null) {
            if (from.getItem().isEquipable()) {
                // Can be equiped
                try {
                    if (connection.getPlayer().equipmentSlots.isLocked(from.getItem().getEquipTo())) {
//                        System.err.println("Equipment Slot is Locked");
                        return false;
                    }

                    connection.getPlayer().equipmentSlots.setBySlotName(from.getItem().getEquipTo(), from);
                    return true;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
//                System.err.println("Can not equip");
            }
        } else {
//            System.err.println("Failed to find from slot");
        }

        return false;
    }

    public int getHotBarSelection() {
        return hotBarSelection;
    }

    public void setHotBarSelection(int hotBarSelection) {
        this.hotBarSelection = hotBarSelection;
    }
}
