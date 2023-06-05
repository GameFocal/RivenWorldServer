package com.gamefocal.rivenworld.game.skills.skillTypes;

import com.badlogic.gdx.graphics.Color;
import com.gamefocal.rivenworld.game.ui.UIIcon;

public class SpearCombatSkill extends SkillClass {
    @Override
    String name() {
        return "Spear Weapons";
    }

    @Override
    String desc() {
        return "Your use of spear weapons against animals and enemies";
    }

    @Override
    UIIcon icon() {
        return UIIcon.KNIFE;
    }

    @Override
    Color color() {
        return Color.WHITE;
    }
}
