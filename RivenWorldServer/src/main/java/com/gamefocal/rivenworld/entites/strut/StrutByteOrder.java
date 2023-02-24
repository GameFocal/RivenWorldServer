package com.gamefocal.rivenworld.entites.strut;

public enum StrutByteOrder {
    BIG(java.nio.ByteOrder.BIG_ENDIAN),
    LITTLE(java.nio.ByteOrder.LITTLE_ENDIAN),
    INHERIT(null);

    private java.nio.ByteOrder bufferOrder;

    StrutByteOrder(java.nio.ByteOrder bufferOrder) {
        this.bufferOrder = bufferOrder;
    }

    public java.nio.ByteOrder getBufferOrder() {
        return bufferOrder;
    }
}
