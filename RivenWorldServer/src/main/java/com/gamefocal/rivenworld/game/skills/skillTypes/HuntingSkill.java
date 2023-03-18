package com.gamefocal.rivenworld.game.skills.skillTypes;

import com.badlogic.gdx.graphics.Color;
import com.gamefocal.rivenworld.game.ui.UIIcon;

public class HuntingSkill extends SkillClass {
    @Override
    String name() {
        return "Hunting";
    }

    @Override
    String desc() {
        return "Killing animals in the world";
    }

    @Override
    UIIcon icon() {
        return UIIcon.DEER;
    }

    @Override
    Color color() {
        return Color.WHITE;
    }
}
