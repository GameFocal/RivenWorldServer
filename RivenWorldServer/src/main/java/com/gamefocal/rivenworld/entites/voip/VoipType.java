package com.gamefocal.rivenworld.entites.voip;

public enum VoipType {

    GLOBAL(0x00),
    PROXIMITY_WHISPER(0X01),
    PROXIMITY_NORMAL(0x02),
    PROXIMITY_YELL(0x03),
    INIT(0xFF);

    private byte type;

    VoipType(byte type) {
        this.type = type;
    }

    VoipType(int i) {
        this.type = (byte) i;
    }

    public byte getType() {
        return type;
    }
}
