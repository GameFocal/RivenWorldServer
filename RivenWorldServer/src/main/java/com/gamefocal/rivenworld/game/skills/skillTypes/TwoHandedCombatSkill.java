package com.gamefocal.rivenworld.game.skills.skillTypes;

import com.badlogic.gdx.graphics.Color;
import com.gamefocal.rivenworld.game.ui.UIIcon;

public class TwoHandedCombatSkill extends SkillClass {
    @Override
    String name() {
        return "Two Handed Weapons";
    }

    @Override
    String desc() {
        return "Your use of two handed weapons against animals and enemies";
    }

    @Override
    UIIcon icon() {
        return UIIcon.SWORD;
    }

    @Override
    Color color() {
        return Color.WHITE;
    }
}
