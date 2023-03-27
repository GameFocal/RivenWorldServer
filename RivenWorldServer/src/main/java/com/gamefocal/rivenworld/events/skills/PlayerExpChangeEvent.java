package com.gamefocal.rivenworld.events.skills;

import com.gamefocal.rivenworld.entites.events.Event;
import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.skills.skillTypes.SkillClass;

public class PlayerExpChangeEvent extends Event<PlayerExpChangeEvent> {

    private HiveNetConnection connection;

    private Class<? extends SkillClass> skill;

    private double amt;

    public PlayerExpChangeEvent(HiveNetConnection connection, Class<? extends SkillClass> skill, double amt) {
        this.connection = connection;
        this.skill = skill;
        this.amt = amt;
    }

    public void setAmt(double amt) {
        this.amt = amt;
    }

    public HiveNetConnection getConnection() {
        return connection;
    }

    public Class<? extends SkillClass> getSkill() {
        return skill;
    }

    public double getAmt() {
        return amt;
    }
}
