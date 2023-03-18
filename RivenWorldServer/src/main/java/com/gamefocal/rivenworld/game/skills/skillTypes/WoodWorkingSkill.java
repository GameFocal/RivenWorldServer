package com.gamefocal.rivenworld.game.skills.skillTypes;

import com.badlogic.gdx.graphics.Color;
import com.gamefocal.rivenworld.game.ui.UIIcon;

public class WoodWorkingSkill extends SkillClass {
    @Override
    String name() {
        return "Wood Working";
    }

    @Override
    String desc() {
        return "Crafting items from lumber and wood";
    }

    @Override
    UIIcon icon() {
        return UIIcon.WOOD_LOG;
    }

    @Override
    Color color() {
        return Color.WHITE;
    }
}
