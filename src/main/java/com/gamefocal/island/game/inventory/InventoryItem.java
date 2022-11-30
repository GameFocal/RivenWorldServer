package com.gamefocal.island.game.inventory;

import org.apache.commons.codec.digest.DigestUtils;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public abstract class InventoryItem implements Serializable {

    protected float weight = 0.00f;

    protected boolean isStackable = true;

    protected Map<String, String> meta = new HashMap<>();

    protected float version = 1.0f;

    public String hash() {
        StringBuilder b = new StringBuilder();
        b.append(this.getClass().getSimpleName()).append(this.slug());
        for (Map.Entry<String, String> m : this.meta.entrySet()) {
            b.append(m.getKey()).append(m.getValue());
        }
        b.append(this.version);

        return DigestUtils.md5Hex(b.toString());
    }

    public abstract String slug();

    public float getWeight() {
        return weight;
    }

    public boolean isStackable() {
        return isStackable;
    }

    public Map<String, String> getMeta() {
        return meta;
    }

    public float getVersion() {
        return version;
    }
}
