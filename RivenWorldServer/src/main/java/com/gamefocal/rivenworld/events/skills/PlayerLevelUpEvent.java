package com.gamefocal.rivenworld.events.skills;

import com.gamefocal.rivenworld.entites.events.Event;
import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.skills.skillTypes.SkillClass;

public class PlayerLevelUpEvent extends Event<PlayerLevelUpEvent> {

    private HiveNetConnection connection;

    private Class<? extends SkillClass> skill;

    private double level;

    public PlayerLevelUpEvent(HiveNetConnection connection, Class<? extends SkillClass> skill, double level) {
        this.connection = connection;
        this.skill = skill;
        this.level = level;
    }

    public void setLevel(double level) {
        this.level = level;
    }

    public HiveNetConnection getConnection() {
        return connection;
    }

    public Class<? extends SkillClass> getSkill() {
        return skill;
    }

    public double getLevel() {
        return level;
    }
}
