package com.gamefocal.rivenworld.game.skills.skillTypes;

import com.badlogic.gdx.graphics.Color;
import com.gamefocal.rivenworld.game.ui.UIIcon;
import com.google.gson.JsonObject;

public abstract class SkillClass {
    abstract String name();

    abstract String desc();

    abstract UIIcon icon();

    abstract Color color();

    public JsonObject toJson() {
        JsonObject o = new JsonObject();
        o.addProperty("name", name());
        o.addProperty("desc", desc());
        o.addProperty("color", color().toString());
        o.addProperty("icon", icon().name());
        return o;
    }
}
