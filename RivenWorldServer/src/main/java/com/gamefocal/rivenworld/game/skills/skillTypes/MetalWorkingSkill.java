package com.gamefocal.rivenworld.game.skills.skillTypes;

import com.badlogic.gdx.graphics.Color;
import com.gamefocal.rivenworld.game.ui.UIIcon;

public class MetalWorkingSkill extends SkillClass {
    @Override
    String name() {
        return "Metal Working";
    }

    @Override
    String desc() {
        return "Your ability to work with metal items";
    }

    @Override
    UIIcon icon() {
        return UIIcon.ANVIL;
    }

    @Override
    Color color() {
        return Color.WHITE;
    }
}
