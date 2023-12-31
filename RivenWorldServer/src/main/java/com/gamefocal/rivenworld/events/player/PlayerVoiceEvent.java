package com.gamefocal.rivenworld.events.player;

import com.gamefocal.rivenworld.entites.events.Event;
import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.entites.voip.VoipType;

import java.util.ArrayList;
import java.util.List;

public class PlayerVoiceEvent extends Event<PlayerVoiceEvent> {

    private HiveNetConnection speaker;

    private short meta;

    private VoipType type;

    private String data;

    private List<HiveNetConnection> recivers = new ArrayList<>();

    public PlayerVoiceEvent(HiveNetConnection speaker, short meta, VoipType type, String data) {
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

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public List<HiveNetConnection> getRecivers() {
        return recivers;
    }

    public void setRecivers(List<HiveNetConnection> recivers) {
        this.recivers = recivers;
    }
}
