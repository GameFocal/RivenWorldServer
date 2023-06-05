package com.gamefocal.rivenworld.events.combat;

import com.gamefocal.rivenworld.entites.events.Event;
import com.gamefocal.rivenworld.entites.net.HiveNetConnection;

public class PlayerBlockEvent extends Event<PlayerBlockEvent> {

    private HiveNetConnection connection;
    private HiveNetConnection attackedBy;

    public PlayerBlockEvent(HiveNetConnection connection, HiveNetConnection attackedBy) {
        this.connection = connection;
        this.attackedBy = attackedBy;
    }

    public HiveNetConnection getConnection() {
        return connection;
    }

    public HiveNetConnection getAttackedBy() {
        return attackedBy;
    }
}
