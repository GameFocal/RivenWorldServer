package com.gamefocal.rivenworld.service;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.entites.service.HiveService;
import com.gamefocal.rivenworld.game.skills.skillTypes.*;
import com.google.auto.service.AutoService;
import com.google.gson.JsonObject;

import javax.inject.Singleton;
import java.util.Arrays;
import java.util.LinkedList;

@AutoService(HiveService.class)
@Singleton
public class SkillService implements HiveService {

    private LinkedList<SkillClass> skills = new LinkedList<>();

    @Override
    public void init() {
        this.registerSkill(
                new WoodcuttingSkill(),
                new MiningSkill(),
                new ForagingSkill(),
                new HuntingSkill(),
                new OneHandedCombatSkill(),
                new LongBowSkill(),
                new WoodWorkingSkill(),
                new MasonrySkill(),
                new BuildingSkill()
        );
    }

    public LinkedList<SkillClass> getSkills() {
        return skills;
    }

    public void registerSkill(SkillClass... skills) {
        this.skills.addAll(Arrays.asList(skills));
    }

    public int expNeededForLevel(int level) {
        if (level >= 1 && level <= 16) {
            return (int) (Math.pow(level, 2) + 6 * level);
        } else if (level >= 17 && level <= 31) {
            return (int) (2.5 * Math.pow(level, 2) - 40.5 * level + 360);
        } else if (level >= 32) {
            return (int) (4.5 * Math.pow(level, 2) - 162.5 * level + 2220);
        } else {
            return 0;
        }
    }

    public JsonObject getPlayerSkills(HiveNetConnection connection) {
        JsonObject o = new JsonObject();
        for (SkillClass skillClass : this.skills) {

//            try {
//                GamePlayerSkillsModel skillsModel = DataService.playerSkills.queryBuilder().where().eq("playerModel_uuid",connection.getUuid().toString());
//
//                // TODO: Pull the skills and gather data
//            } catch (SQLException throwables) {
//                throwables.printStackTrace();
//            }

        }
        return new JsonObject();
    }

}
