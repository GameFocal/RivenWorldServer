package com.gamefocal.island.game.inventory;

import com.gamefocal.island.entites.net.HiveNetConnection;
import com.gamefocal.island.game.exceptions.InventoryOwnedAlreadyException;
import org.apache.commons.codec.digest.DigestUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Inventory implements Serializable {

    private int storageSpace = 16;

    private int maxStack = 64;

    private UUID uuid;

    private transient HiveNetConnection owner;

    private InventoryStack[] items = new InventoryStack[0];

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

    public void add(InventoryItem item) {
        InventoryStack stack = new InventoryStack(item);
        this.add(stack);
    }

    public void add(InventoryItem item, int amt) {
        InventoryStack stack = new InventoryStack(item, amt);
        this.add(stack);
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

    public InventoryStack get(int index) {
        return this.items[index];
    }

    public void clear(int index) {
        this.items[index] = null;
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

        for (InventoryStack s : this.items) {
            if (s != null) {
                if (s.getAmount() > this.maxStack) {
                    s.setAmount(this.maxStack);
                } else if (s.getAmount() == 0) {

                }
            }
        }

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
}
