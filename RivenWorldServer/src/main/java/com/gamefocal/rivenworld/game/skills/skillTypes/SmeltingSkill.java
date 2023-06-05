package com.gamefocal.rivenworld.game.skills.skillTypes;

import com.badlogic.gdx.graphics.Color;
import com.gamefocal.rivenworld.game.ui.UIIcon;

public class SmeltingSkill extends SkillClass {
    @Override
    String name() {
        return "Smelting";
    }

    @Override
    String desc() {
        return "Your ability to smelt";
    }

    @Override
    UIIcon icon() {
        return UIIcon.GOLD_BARS;
    }

    @Override
    Color color() {
        return Color.WHITE;
    }
}
