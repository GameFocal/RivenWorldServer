package com.gamefocal.rivenworld.game.skills.skillTypes;

import com.badlogic.gdx.graphics.Color;
import com.gamefocal.rivenworld.game.ui.UIIcon;

public abstract class SkillClass {
    abstract String name();

    abstract String desc();

    abstract UIIcon icon();

    abstract Color color();
}
