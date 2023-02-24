package com.gamefocal.rivenworld.game.ui.radialmenu;

import com.gamefocal.rivenworld.game.ui.UIIcon;

public class RadialMenuOption {

    private String name;

    private UIIcon icon;

    private String action;

    public RadialMenuOption(String name, String action, UIIcon icon) {
        this.name = name;
        this.icon = icon;
        this.action = action;
    }

    public String getName() {
        return name;
    }

    public UIIcon getIcon() {
        return icon;
    }

    public String getAction() {
        return action;
    }
}
