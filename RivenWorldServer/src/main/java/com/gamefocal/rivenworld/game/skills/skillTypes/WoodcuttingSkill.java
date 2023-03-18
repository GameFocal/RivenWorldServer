package com.gamefocal.rivenworld.game.skills.skillTypes;

import com.badlogic.gdx.graphics.Color;
import com.gamefocal.rivenworld.game.ui.UIIcon;

public class WoodcuttingSkill extends SkillClass {
    @Override
    String name() {
        return "Wood Cutting";
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
