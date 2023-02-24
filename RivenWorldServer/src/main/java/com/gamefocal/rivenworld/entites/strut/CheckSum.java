package com.gamefocal.rivenworld.entites.strut;

public enum CheckSum {
    NONE(0, 0, null),
    CRC16(8 * 2, 0, null);

    private int offset = 0;
    private int length = 0;
    private Class type = null;
    private Object value;

    CheckSum(int offset, int length, Class type) {
        this.offset = offset;
        this.length = length;
        this.type = type;
    }

    public CheckSum config(int offset, int length, Class type) {
        this.offset = offset;
        this.length = length;
        this.type = type;
        return this;
    }

    public CheckSum setOffset(int offset) {
        this.offset = offset;
        return this;
    }

    public CheckSum setLength(int length) {
        this.length = length;
        return this;
    }

    public int getOffset() {
        return offset;
    }

    public int getLength() {
        return length;
    }

    public Class getType() {
        return type;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Class type, Object value) {
        this.type = type;
        this.value = value;
    }
}
