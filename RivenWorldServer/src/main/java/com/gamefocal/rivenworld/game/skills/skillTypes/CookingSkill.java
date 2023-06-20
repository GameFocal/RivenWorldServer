package com.gamefocal.rivenworld.game.skills.skillTypes;

import com.badlogic.gdx.graphics.Color;
import com.gamefocal.rivenworld.game.ui.UIIcon;

public class CookingSkill extends SkillClass {
    @Override
    String name() {
        return "Cooking";
    }

    @Override
    String desc() {
        return "Your ability to cook quality food";
    }

    @Override
    UIIcon icon() {
        return UIIcon.POT2;
    }

    @Override
    Color color() {
        return Color.WHITE;
    }
}
