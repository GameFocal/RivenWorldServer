package com.gamefocal.rivenworld.events.inv;

import com.gamefocal.rivenworld.entites.events.Event;
import com.gamefocal.rivenworld.entites.net.HiveNetConnection;

public class HotBarUpdateEvent extends Event<HotBarUpdateEvent> {

    private HiveNetConnection connection;
    private int slot;

    public HotBarUpdateEvent(HiveNetConnection connection, int slot) {
        this.connection = connection;
        this.slot = slot;
    }

    public HiveNetConnection getConnection() {
        return connection;
    }

    public int getSlot() {
        return slot;
    }
}
