package com.gamefocal.rivenworld.game.ui.sign;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.entites.signs.ReadableEntity;
import com.gamefocal.rivenworld.game.interactable.InteractAction;
import com.gamefocal.rivenworld.game.ui.GameUI;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class RivenSignUI extends GameUI<ReadableEntity> {

    public RivenSignUI() {
    }

    @Override
    public String name() {
        return "net-sign";
    }

    @Override
    public JsonObject data(HiveNetConnection connection, ReadableEntity obj) {

        JsonArray lines = new JsonArray();
        for (Object s : obj.getLines()) {
            String ss = (String) s;
            lines.add(ss);
        }

        JsonObject o = new JsonObject();
        o.add("lines", lines);

        return o;
    }

    @Override
    public void onOpen(HiveNetConnection connection, ReadableEntity object) {

    }

    @Override
    public void onClose(HiveNetConnection connection, ReadableEntity object) {

    }

    @Override
    public void onAction(HiveNetConnection connection, InteractAction action, String tag, String[] data) {
        this.close(connection);
    }

}
