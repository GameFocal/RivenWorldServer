package com.gamefocal.rivenworld.game.skills.skillTypes;

import com.badlogic.gdx.graphics.Color;
import com.gamefocal.rivenworld.game.ui.UIIcon;

public class BlockingSkill extends SkillClass {
    @Override
    String name() {
        return "Blocking";
    }

    @Override
    String desc() {
        return "Your ability to block";
    }

    @Override
    UIIcon icon() {
        return UIIcon.PALISADE;
    }

    @Override
    Color color() {
        return Color.WHITE;
    }
}
