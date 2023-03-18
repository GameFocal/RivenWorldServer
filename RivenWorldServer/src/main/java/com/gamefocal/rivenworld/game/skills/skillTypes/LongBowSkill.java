package com.gamefocal.rivenworld.game.skills.skillTypes;

import com.badlogic.gdx.graphics.Color;
import com.gamefocal.rivenworld.game.ui.UIIcon;

public class LongBowSkill extends SkillClass {
    @Override
    String name() {
        return "Long Bow";
    }

    @Override
    String desc() {
        return "Your use of a long bow against animals and enemies";
    }

    @Override
    UIIcon icon() {
        return UIIcon.LONG_BOW;
    }

    @Override
    Color color() {
        return Color.WHITE;
    }
}
