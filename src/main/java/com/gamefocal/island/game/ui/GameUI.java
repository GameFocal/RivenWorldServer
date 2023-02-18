package com.gamefocal.island.game.ui;

import com.gamefocal.island.entites.net.HiveNetConnection;
import com.gamefocal.island.entites.net.HiveNetMessage;
import com.gamefocal.island.game.interactable.InteractAction;
import com.google.gson.JsonObject;

import java.util.UUID;

public abstract class GameUI<T> {

    protected UUID uuid;

    private T attached = null;

    protected boolean focus = true;
    protected boolean transferControls = true;
    protected boolean lockLookInput = false;
    protected boolean lockMoveInput = false;

    public abstract String name();

    public abstract JsonObject data(HiveNetConnection connection, T obj);

    public abstract void onOpen(HiveNetConnection connection, T object);

    public abstract void onClose(HiveNetConnection connection, T object);

    public abstract void onAction(InteractAction action, String tag);

    public void open(HiveNetConnection connection, T object) {

        if (this.uuid == null) {
            this.uuid = UUID.randomUUID();
        }

        if (connection.getOpenUI() != null) {
            connection.getOpenUI().close(connection);
        }

        JsonObject o = this.data(connection, object);

        if (o == null) {
            o = new JsonObject();
        }

        o.addProperty("_tc", this.transferControls);
        o.addProperty("_f", this.focus);
        o.addProperty("_ll", this.lockLookInput);
        o.addProperty("_lm", this.lockMoveInput);

        String payload = o.toString();

        HiveNetMessage msg = new HiveNetMessage();
        msg.cmd = "noui";
        msg.args = new String[]{
                this.name(),
                this.uuid.toString(),
                payload
        };

        this.onOpen(connection, object);

        connection.sendTcp(msg.toString());

        connection.setOpenUI(this);
        this.attached = object;
    }

    public T getAttached() {
        return attached;
    }

    public void setAttached(T attached) {
        this.attached = attached;
    }

    public void update(HiveNetConnection connection) {

        JsonObject o = this.data(connection, this.attached);

        if (o == null) {
            o = new JsonObject();
        }

        o.addProperty("_tc", this.transferControls);
        o.addProperty("_f", this.transferControls);
        o.addProperty("_ll", this.lockLookInput);
        o.addProperty("_lm", this.lockMoveInput);

        String payload = o.toString();

        HiveNetMessage msg = new HiveNetMessage();
        msg.cmd = "nuui";
        msg.args = new String[]{
                this.uuid.toString(),
                payload
        };

        System.out.println("UPDATE UI");

        connection.sendTcp(msg.toString());
    }

    public void close(HiveNetConnection connection) {

        HiveNetMessage message = new HiveNetMessage();
        message.cmd = "ncui";
        message.args = new String[]{
                this.uuid.toString()
        };

        this.onClose(connection, this.attached);

        connection.sendTcp(message.toString());
    }

}
