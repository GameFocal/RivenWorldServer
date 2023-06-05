package com.gamefocal.rivenworld.game.skills.skillTypes;

import com.badlogic.gdx.graphics.Color;
import com.gamefocal.rivenworld.game.ui.UIIcon;

public class CraftingSkill extends SkillClass {
    @Override
    String name() {
        return "Crafting";
    }

    @Override
    String desc() {
        return "Your ability to craft items";
    }

    @Override
    UIIcon icon() {
        return UIIcon.HAMMER;
    }

    @Override
    Color color() {
        return Color.WHITE;
    }
}
