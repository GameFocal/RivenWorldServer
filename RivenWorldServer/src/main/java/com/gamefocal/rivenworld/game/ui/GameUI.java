package com.gamefocal.rivenworld.game.ui;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.entites.net.HiveNetMessage;
import com.gamefocal.rivenworld.game.interactable.InteractAction;
import com.google.gson.JsonObject;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.UUID;

public abstract class GameUI<T> {

    protected UUID uuid;

    private T attached = null;

    private HiveNetConnection owner = null;

    protected boolean focus = true;
    protected boolean transferControls = true;
    protected boolean uiOnlyMode = false;
    protected boolean lockLookInput = false;
    protected boolean lockMoveInput = false;

    public abstract String name();

    public abstract JsonObject data(HiveNetConnection connection, T obj);

    public abstract void onOpen(HiveNetConnection connection, T object);

    public abstract void onClose(HiveNetConnection connection, T object);

    public abstract void onAction(HiveNetConnection connection, InteractAction action, String tag, String[] data);

    public void open(HiveNetConnection connection, T object) {
        this.attached = object;
        this.owner = connection;

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
        o.addProperty("_uiom", this.uiOnlyMode);

        String payload = o.toString();

        HiveNetMessage msg = new HiveNetMessage();
        msg.cmd = "noui";
        msg.args = new String[]{
                this.name(),
                this.uuid.toString(),
                Base64.getEncoder().encodeToString(payload.getBytes(StandardCharsets.UTF_8))
        };

        this.onOpen(connection, object);

        connection.sendTcp(msg.toString());

        connection.setOpenUI(this);
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

//        System.out.println("UPDATE UI");

        connection.sendTcp(msg.toString());
    }

    public void close(HiveNetConnection connection) {
        this.owner = null;

        HiveNetMessage message = new HiveNetMessage();
        message.cmd = "ncui";
        message.args = new String[]{
                this.uuid.toString()
        };

        this.onClose(connection, this.attached);

        connection.setOpenUI(null);

        connection.sendTcp(message.toString());
    }

    public HiveNetConnection getOwner() {
        return owner;
    }

    @Override
    public boolean equals(Object obj) {
        if (GameUI.class.isAssignableFrom(obj.getClass())) {
            return (((GameUI<?>) obj).uuid == this.uuid);
        }

        return false;
    }

    @Override
    public String toString() {
        return this.uuid.toString();
    }
}
