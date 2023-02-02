package com.gamefocal.island.game.inventory;

import com.gamefocal.island.DedicatedServer;
import com.gamefocal.island.entites.net.HiveNetConnection;
import com.gamefocal.island.game.GameEntity;
import com.gamefocal.island.game.exceptions.InventoryOwnedAlreadyException;
import com.gamefocal.island.game.inventory.crafting.CraftingQueue;
import com.gamefocal.island.models.GameEntityModel;
import com.gamefocal.island.service.InventoryService;
import com.google.gson.JsonObject;

import java.io.Serializable;
import java.util.*;

public class Inventory implements Serializable {

    private String name = "Inventory";

    private boolean isLocked = false;

    private int storageSpace = 16;

    private int maxStack = 64;

    private UUID uuid;

    private InventoryType type = InventoryType.PLAYER;

    private transient HiveNetConnection owner = null;

    private String gameRef = "self";

    private InventoryStack[] items = new InventoryStack[0];

    private UUID attachedEntity = null;

    private CraftingQueue craftingQueue = null;

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

    public void add(InventoryItem item) {
        InventoryStack stack = new InventoryStack(item);
        this.add(stack);
    }

    public void add(InventoryItem item, int amt) {
        InventoryStack stack = new InventoryStack(item, amt);
        this.add(stack);
    }

    public Inventory setName(String name) {
        this.name = name;
        return this;
    }

    public boolean canAdd(InventoryStack stack) {
        InventoryStack currentStack = null;

        for (InventoryStack s : this.items) {
            if (s != null) {
                if (s.getHash().equalsIgnoreCase(stack.getHash())) {
                    currentStack = s;
                    break;
                }
            }
        }

        if (currentStack == null) {
            // None found add a new stack
            for (InventoryStack item : this.items) {
                if (item == null) {
                    return true;
                }
            }
        } else {
            return (currentStack.getAmount() + stack.getAmount()) <= this.maxStack;
        }

        return false;
    }

    public int getEmptySlotCount() {
        int empty = 0;
        for (InventoryStack s : this.items) {
            if (s == null) {
                empty++;
            }
        }

        return empty;
    }

    public boolean hasEmptySlot() {
        for (InventoryStack s : this.items) {
            if (s == null) {
                return true;
            }
        }

        return false;
    }

    public void addToEmptySlot(InventoryStack stack) {
        // None found add a new stack
        for (int i = 0; i < this.items.length; i++) {
            if (this.items[i] == null) {
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
        InventoryStack currentStack = null;

        for (InventoryStack s : this.items) {
            if (s != null) {
                if (s.getHash().equalsIgnoreCase(stack.getHash())) {
                    currentStack = s;
                    break;
                }
            }
        }

        if (currentStack == null) {
            // None found add a new stack
            for (int i = 0; i < this.items.length; i++) {
                if (this.items[i] == null) {
                    this.items[i] = stack;
                    break;
                }
            }
        } else {
            currentStack.setAmount(currentStack.getAmount() + stack.getAmount());
        }

        this.update();
    }

    public boolean isEmpty(int slot) {
        return this.items[slot] == null;
    }

    public boolean isEmpty() {
        int items = 0;
        for (InventoryStack s : this.items) {
            if (s != null) {
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
            if (s != null) {
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
            if (ss != null) {
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
            if (ss != null) {
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
                } else if (s.getAmount() == 0) {
                    this.items[i] = null;
                }
            }
            i++;
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

    public int getMaxStack() {
        return maxStack;
    }

    public InventoryStack[] getItems() {
        return items;
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

    public int canCraftAmt(CraftingRecipe recipe) {

        LinkedList<Float> values = new LinkedList<>();

        for (Map.Entry<Class<? extends InventoryItem>, Integer> e : recipe.getRequires().entrySet()) {
            float amtInIn = this.amtOfType(e.getKey());
            float canMake = (float) Math.floor(amtInIn / e.getValue());
            if (canMake > 0) {
                values.add(canMake);
            }
        }

        if (values.size() == 0) {
            return 0;
        }

        float minNumber = Float.MAX_VALUE;
        for (float v : values) {
            if (v < minNumber) {
                minNumber = v;
            }
        }

        return (int) minNumber;
    }
}
