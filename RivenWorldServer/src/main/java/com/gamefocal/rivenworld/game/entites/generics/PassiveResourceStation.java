package com.gamefocal.rivenworld.game.entites.generics;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.entites.placable.PlaceableEntity;
import com.gamefocal.rivenworld.game.inventory.Inventory;

import java.util.concurrent.TimeUnit;

public abstract class PassiveResourceStation<T> extends PlaceableEntity<T> implements EntityStorageInterface, TickEntity {

    protected Inventory fuel;

    protected String producesName;
    protected String producesDesc;

    protected long maxAmt = 64 * 2;

    protected long amt = 0;

    protected long timeSpanInSeconds = 60;

    protected long amtPerTimeSpan = 1;

    protected boolean requireFuel = false;

    private long lastGeneration = 0L;


    @Override
    public void onSpawn() {

    }

    @Override
    public void onDespawn() {

    }

    @Override
    public void onTick() {
        long diff = TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - this.lastGeneration);
        if (diff >= this.timeSpanInSeconds) {
            // Generate
            this.amt += this.amtPerTimeSpan;
            if (this.amt > this.maxAmt) {
                this.amt = this.maxAmt;
            }

            // Mark as generated
            this.lastGeneration = System.currentTimeMillis();
        }
    }

    public abstract void onGather(HiveNetConnection connection, long amt);

    @Override
    public void onInventoryUpdated() {

    }

    @Override
    public void onInventoryOpen() {

    }

    @Override
    public void onInventoryClosed() {

    }

    public Inventory getFuel() {
        return fuel;
    }

    public String getProducesName() {
        return producesName;
    }

    public String getProducesDesc() {
        return producesDesc;
    }

    public long getMaxAmt() {
        return maxAmt;
    }

    public long getAmt() {
        return amt;
    }

    public long getTimeSpanInSeconds() {
        return timeSpanInSeconds;
    }

    public long getAmtPerTimeSpan() {
        return amtPerTimeSpan;
    }

    public boolean isRequireFuel() {
        return requireFuel;
    }

    public long getLastGeneration() {
        return lastGeneration;
    }
}
