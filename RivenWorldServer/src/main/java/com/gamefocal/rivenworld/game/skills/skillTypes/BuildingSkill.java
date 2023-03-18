package com.gamefocal.rivenworld.game.skills.skillTypes;

import com.badlogic.gdx.graphics.Color;
import com.gamefocal.rivenworld.game.ui.UIIcon;

public class BuildingSkill extends SkillClass {
    @Override
    String name() {
        return "Building";
    }

    @Override
    String desc() {
        return "Cutting down trees around the world";
    }

    @Override
    UIIcon icon() {
        return UIIcon.AXE;
    }

    @Override
    Color color() {
        return Color.WHITE;
    }
}
