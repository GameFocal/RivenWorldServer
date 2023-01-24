package com.gamefocal.island.game.player;

import com.gamefocal.island.entites.net.HiveNetConnection;
import com.gamefocal.island.game.inventory.InventoryItem;
import com.gamefocal.island.game.inventory.InventoryStack;
import com.gamefocal.island.game.util.InventoryUtil;
import com.gamefocal.island.game.util.Location;
import com.google.gson.JsonObject;
import org.apache.commons.codec.digest.DigestUtils;

import java.io.Serializable;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class PlayerState implements Serializable {

    public transient HiveNetConnection player;

    public String hash;

    public String animation = null;

    public long animStart = 0L;

    public Location location = new Location(0, 0, 0);

    public boolean isSpeaking = false;

    public boolean isSwimming = false;

    public transient InventoryItem inHand = null;

    public JsonObject inHandItem = new JsonObject();

    public Long lastSpeach = 0L;

    public Long version = 0L;

    public PlayerBlendState blendState = new PlayerBlendState();

    public String headtag = null;

    public void tick() {

        if(this.headtag == null) {
            this.headtag = "A New Player (TODO)";
        }

        if (TimeUnit.MILLISECONDS.toSeconds(lastSpeach) <= 1) {
            this.isSpeaking = true;
        } else {
            this.isSpeaking = false;
        }

        this.hash = this.calcHash();

        if (this.inHand != null) {
            this.inHandItem = InventoryUtil.itemToJson(new InventoryStack(inHand, 1), 0);
        } else {
            this.inHandItem = new JsonObject();
        }
    }

    public void markDirty() {
        this.version = System.currentTimeMillis();
    }

    public String calcHash() {
        return DigestUtils.md5Hex(this.animation + this.location.toString() + this.isSpeaking + this.isSwimming + (this.inHand != null ? this.inHand.hash() : "none") + this.version + this.animStart);
    }

}
