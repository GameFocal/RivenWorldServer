package com.gamefocal.rivenworld.game.skills.skillTypes;

import com.badlogic.gdx.graphics.Color;
import com.gamefocal.rivenworld.game.ui.UIIcon;

public class MiningSkill extends SkillClass {
    @Override
    String name() {
        return "Mining";
    }

    @Override
    String desc() {
        return "Gathering resources from rock and mineral nodes around the world";
    }

    @Override
    UIIcon icon() {
        return UIIcon.PICKAXE;
    }

    @Override
    Color color() {
        return Color.WHITE;
    }
}
