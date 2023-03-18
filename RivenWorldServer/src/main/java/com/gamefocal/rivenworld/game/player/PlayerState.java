package com.gamefocal.rivenworld.game.player;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.entites.net.HiveNetMessage;
import com.gamefocal.rivenworld.game.inventory.equipment.EquipmentSlots;
import com.gamefocal.rivenworld.game.util.Location;
import org.apache.commons.codec.digest.DigestUtils;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;

public class PlayerState implements Serializable {

    public transient HiveNetConnection player;

    public String hash;

    public String animation = null;

    public long animStart = 0L;

    public Location location = new Location(0, 0, 0);

    public boolean isSpeaking = false;

    public boolean isSwimming = false;

    public transient EquipmentSlots equipment = null;

    public String equipmentString = "none";

    public Long lastSpeach = 0L;

    public Long version = 0L;

    public PlayerBlendState blendState = new PlayerBlendState();

    public String headtag = null;

    public boolean isDead = false;

    public void tick() {

        if (this.headtag == null) {
            this.headtag = "A New Player (TODO)";
        }

        if (TimeUnit.MILLISECONDS.toSeconds(lastSpeach) <= 1) {
            this.isSpeaking = true;
        } else {
            this.isSpeaking = false;
        }

        this.hash = this.calcHash();

//        JsonObject eq = new JsonObject();
//        eq.add("eq",this.player.getPlayer().equipmentSlots.toJson());

        if (this.player.getPlayer().equipmentSlots != null) {
            this.equipmentString = this.player.getPlayer().equipmentSlots.toJson().toString();
        } else {
            this.equipmentString = new EquipmentSlots().toJson().toString();
        }

//        System.out.println(this.equipmentString);

//        if (this.inHand != null) {
//            this.inHandItem = this.inHand.slug();
//        } else {
//            this.inHandItem = "Empty";
//        }
    }

    public void markDirty() {
        this.version = System.currentTimeMillis();
    }

    public String calcHash() {
        return DigestUtils.md5Hex(this.animation + this.location.toString() + this.isSpeaking + this.isSwimming + this.equipmentString + this.version + this.animStart);
    }

    public HiveNetMessage getNetPacket() {
        HiveNetMessage message = new HiveNetMessage();
        message.cmd = "ps";

        message.args = new String[]{
                this.player.getUuid().toString(),
                String.valueOf(this.player.getVoiceId()),
                this.calcHash(),
                this.animation,
                String.valueOf(this.animStart),
                this.location.toString(),
                (this.isSpeaking ? "t" : "f"),
                (this.isSwimming ? "t" : "f"),
                this.equipmentString,
                String.valueOf(this.lastSpeach),
                String.valueOf(this.version),
                (this.blendState.isInAir ? "t" : "f"),
                String.valueOf(this.blendState.speed),
                this.blendState.aimRotator.toString(),
                (this.blendState.isAiming ? "t" : "f"),
                String.valueOf(this.blendState.rotation),
                (this.blendState.isFishing ? "t" : "f"),
                (this.blendState.isSwimming ? "t" : "f"),
                this.headtag,
                this.blendState.attackAngle.toString(),
                String.valueOf(this.blendState.attackMode),
                String.valueOf(this.blendState.attackDirection),
                (this.isDead ? "t" : "f"),
                this.player.getNetAppearance().toString(),
                (this.blendState.IsCrouching ? "t" : "f"),
                (this.blendState.hasTorch ? "t" : "f"),
                (this.blendState.IsMoving ? "t" : "f"),
                (this.blendState.hasBow ? "t" : "f"),
                String.valueOf(this.blendState.DeltaTimeX),
                this.blendState.Velocity.toString(),
                String.valueOf(this.blendState.GroundSpeed),
                this.blendState.BaseAimRotation.toString(),
                String.valueOf(this.blendState.YawOffset),
        };
        return message;
    }

}
