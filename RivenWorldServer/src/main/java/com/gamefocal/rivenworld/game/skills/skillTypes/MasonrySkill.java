package com.gamefocal.rivenworld.game.skills.skillTypes;

import com.badlogic.gdx.graphics.Color;
import com.gamefocal.rivenworld.game.ui.UIIcon;

public class MasonrySkill extends SkillClass {
    @Override
    String name() {
        return "Masonry";
    }

    @Override
    String desc() {
        return "Crafting items from stone";
    }

    @Override
    UIIcon icon() {
        return UIIcon.ROCKS;
    }

    @Override
    Color color() {
        return Color.WHITE;
    }
}
