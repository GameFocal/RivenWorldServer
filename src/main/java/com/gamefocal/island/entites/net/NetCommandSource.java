package com.gamefocal.island.entites.net;

public enum NetCommandSource {

    ANY("!ANY"),
    UNKNOWN("!UNKNOWN"),
    CONTAINER("c-"),
    INTERNAL("internal");

    private String channelPart;

    NetCommandSource(String channelPart) {
        this.channelPart = channelPart;
    }

    public String getChannelPart() {
        return channelPart;
    }
}
