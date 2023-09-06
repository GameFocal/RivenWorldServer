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
            this.headtag = this.player.getPlayer().displayName;
        }

//        if (TimeUnit.MILLISECONDS.toSeconds(lastSpeach) <= 1) {
//            this.isSpeaking = true;
//        } else {
//            this.isSpeaking = false;
//        }

        this.location = this.player.getPlayer().location;

        this.isSpeaking = this.player.isSpeaking();

        this.hash = this.calcHash();

//        JsonObject eq = new JsonObject();
//        eq.add("eq",this.player.getPlayer().equipmentSlots.toJson());

        if (this.player.getPlayer().equipmentSlots != null) {
            this.equipmentString = this.player.getPlayer().equipmentSlots.toJson().toString();
        } else {
            this.equipmentString = new EquipmentSlots().toJson().toString();
        }

        this.blendState.isCapture = this.player.isCaptured();

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
                this.player.getUuid().toString(), //0
                String.valueOf(this.player.getVoiceId()), //1
                this.calcHash(), //2
                this.animation, //3
                String.valueOf(this.animStart), //4
                this.location.toString(), //5
                (this.isSpeaking ? "t" : "f"), //6
                (this.isSwimming ? "t" : "f"), //7
                this.equipmentString, //8
                String.valueOf(this.lastSpeach), //9
                String.valueOf(this.version), //10
                (this.blendState.isInAir ? "t" : "f"), //11
                String.valueOf(this.blendState.speed), //12
                (this.blendState.isAiming ? "t" : "f"), //13
                (this.blendState.isFishing ? "t" : "f"), //14
                (this.blendState.isSwimming ? "t" : "f"), //15
                String.valueOf(this.blendState.attackMode), //16
                (this.blendState.IsCrouching ? "t" : "f"), //17
                (this.blendState.hasTorch ? "t" : "f"), //18
                (this.blendState.IsMoving ? "t" : "f"), //19
                (this.blendState.hasBow ? "t" : "f"), //20
                String.valueOf(this.blendState.DeltaTimeX), //21
                this.blendState.Velocity.toString(), //22
                String.valueOf(this.blendState.GroundSpeed), //23
                this.blendState.BaseAimRotation.toString(), //24
                String.valueOf(this.blendState.YawOffset), //25
                (this.blendState.hasShield ? "t" : "f"), //26
                (this.blendState.twoHand ? "t" : "f"), //27
                (this.blendState.oneHand ? "t" : "f"), //28
                (this.blendState.hasSpear ? "t" : "f"), //29
                (this.blendState.isCapture ? "t" : "f"), //30
                String.valueOf(this.blendState.BlendWUpper), //31
                String.valueOf(this.blendState.BlendWRightArm), //32
                this.headtag, //33
                (this.isDead ? "t" : "f"), //34
                this.player.getNetAppearance().toString(), //35
                (this.player.isNetReplicationHasCollisions() ? "t" : "f"), // i36,
                this.player.getReportedVelocity().toString() //37
        };
        return message;
    }

}
