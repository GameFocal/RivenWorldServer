package com.gamefocal.rivenworld.game.entites;

public enum NetworkUpdateFrequency {

    REALTIME(5),
    NORMAL(25),
    LOW(50),
    LOWEST(100);

    private long milli = 25;

    NetworkUpdateFrequency(long milli) {
        this.milli = milli;
    }

    public long getMilli() {
        return milli;
    }
}
