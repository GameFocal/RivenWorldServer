package com.gamefocal.rivenworld.game.skills.skillTypes;

import com.badlogic.gdx.graphics.Color;
import com.gamefocal.rivenworld.game.ui.UIIcon;

public class ForagingSkill extends SkillClass {
    @Override
    String name() {
        return "Foraging";
    }

    @Override
    String desc() {
        return "Gathering resources from the land and trees";
    }

    @Override
    UIIcon icon() {
        return UIIcon.FORAGING;
    }

    @Override
    Color color() {
        return Color.WHITE;
    }
}
