package com.gamefocal.rivenworld.game.ui.radialmenu;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.interactable.InteractAction;
import com.gamefocal.rivenworld.game.ui.GameUI;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.Arrays;

public class DynamicRadialMenuUI extends GameUI {

    private ArrayList<RadialMenuOption> options = new ArrayList<>();

    private RadialMenuHandler handler;

    public DynamicRadialMenuUI(RadialMenuOption... opts) {
        this.transferControls = false;
        this.focus = false;
        this.lockLookInput = true;
        this.options.addAll(Arrays.asList(opts));
    }

    @Override
    public String name() {
        return "dynamic-radial";
    }

    public RadialMenuHandler getHandler() {
        return handler;
    }

    public void setHandler(RadialMenuHandler handler) {
        this.handler = handler;
    }

    @Override
    public JsonObject data(HiveNetConnection connection, Object obj) {

        JsonObject o = new JsonObject();
        JsonArray a = new JsonArray();

        for (RadialMenuOption m : this.options) {
            JsonObject m2 = new JsonObject();
            m2.addProperty("name", m.getName());
            m2.addProperty("icon", m.getIcon().name());
            m2.addProperty("action", m.getAction());

            a.add(m2);
        }

        o.add("a", a);

        System.out.println(o.toString());

        return o;
    }

    public ArrayList<RadialMenuOption> getOptions() {
        return options;
    }

    @Override
    public void onOpen(HiveNetConnection connection, Object object) {

    }

    @Override
    public void onClose(HiveNetConnection connection, Object object) {

    }

    @Override
    public void onAction(HiveNetConnection connection, InteractAction action, String tag, String[] data) {
        if (action == InteractAction.PRIMARY) {
            // Left Click Event
        }
        if (action == InteractAction.ALT) {
            // Right Click Event
        }
    }
}
