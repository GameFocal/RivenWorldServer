package com.gamefocal.island.entites.net;

public abstract class HiveMessage {
    public String id;
    public String from;
    public String to;
    public String cmd;
    public String type;
    public String channel = "unknown";
}
