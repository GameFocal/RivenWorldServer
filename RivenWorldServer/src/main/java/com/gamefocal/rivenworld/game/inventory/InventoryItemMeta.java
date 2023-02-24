package com.gamefocal.rivenworld.game.inventory;

import java.util.ArrayList;
import java.util.HashMap;

public class InventoryItemMeta {

    private String name;

    private String desc;

    private ArrayList<String> attributes = new ArrayList<>();

    private HashMap<String,String> tags = new HashMap<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public ArrayList<String> getAttributes() {
        return attributes;
    }

    public void setAttributes(ArrayList<String> attributes) {
        this.attributes = attributes;
    }

    public HashMap<String, String> getTags() {
        return tags;
    }

    public void setTags(HashMap<String, String> tags) {
        this.tags = tags;
    }
}
