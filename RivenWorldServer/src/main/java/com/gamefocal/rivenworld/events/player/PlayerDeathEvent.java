package com.gamefocal.rivenworld.events.player;

import com.gamefocal.rivenworld.entites.events.Event;
import com.gamefocal.rivenworld.entites.net.HiveNetConnection;

public class PlayerDeathEvent extends Event<PlayerDeathEvent> {

    private HiveNetConnection connection;

    private HiveNetConnection killedBy;

    private boolean dropInventory = true;

    private String deathMsg = "You have died";

    public PlayerDeathEvent(HiveNetConnection connection, HiveNetConnection killedBy) {
        this.connection = connection;
        this.killedBy = killedBy;
    }

    public boolean isDropInventory() {
        return dropInventory;
    }

    public void setDropInventory(boolean dropInventory) {
        this.dropInventory = dropInventory;
    }

    public void setDeathMsg(String deathMsg) {
        this.deathMsg = deathMsg;
    }

    public HiveNetConnection getConnection() {
        return connection;
    }

    public HiveNetConnection getKilledBy() {
        return killedBy;
    }

    public String getDeathMsg() {
        return deathMsg;
    }
}
