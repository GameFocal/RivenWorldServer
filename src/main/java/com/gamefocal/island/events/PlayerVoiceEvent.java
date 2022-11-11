package com.gamefocal.island.events;

import com.gamefocal.island.entites.events.Event;
import com.gamefocal.island.entites.net.HiveNetConnection;
import com.gamefocal.island.entites.voip.VoipType;

import java.util.ArrayList;
import java.util.List;

public class PlayerVoiceEvent extends Event<PlayerVoiceEvent> {

    private HiveNetConnection speaker;

    private short meta;

    private VoipType type;

    private byte[] data;

    private List<HiveNetConnection> recivers = new ArrayList<>();

    public PlayerVoiceEvent(HiveNetConnection speaker, short meta, VoipType type, byte[] data) {
        this.speaker = speaker;
        this.meta = meta;
        this.type = type;
        this.data = data;
    }

    public HiveNetConnection getSpeaker() {
        return speaker;
    }

    public short getMeta() {
        return meta;
    }

    public VoipType getType() {
        return type;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public List<HiveNetConnection> getRecivers() {
        return recivers;
    }

    public void setRecivers(List<HiveNetConnection> recivers) {
        this.recivers = recivers;
    }
}
