package com.gamefocal.rivenworld.service;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.entites.service.HiveService;
import com.gamefocal.rivenworld.events.skills.PlayerExpChangeEvent;
import com.gamefocal.rivenworld.events.skills.PlayerLevelUpEvent;
import com.gamefocal.rivenworld.game.skills.skillTypes.*;
import com.gamefocal.rivenworld.models.GamePlayerSkillsModel;
import com.google.auto.service.AutoService;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import javax.inject.Singleton;
import java.sql.SQLException;
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
                new MasonrySkill()
        );
    }

    public LinkedList<SkillClass> getSkills() {
        return skills;
    }

    public void registerSkill(SkillClass... skills) {
        this.skills.addAll(Arrays.asList(skills));
    }

    /**
     * The formulas for figuring out how many experience orbs you need to get to the next level are as follows
     *
     * @param level
     * @return
     */
    private static int getExpToNext(int level) {
        if (level > 30) {
            return 9 * level - 158;
        }
        if (level > 15) {
            return 5 * level - 38;
        }
        return 2 * level + 7;
    }

    /**
     * Calculates total experience based on level.
     *
     * @param level
     * @return
     */
    public static int getExpFromLevel(int level) {
        if (level > 30) {
            return (int) (4.5 * level * level - 162.5 * level + 2220);
        }
        if (level > 15) {
            return (int) (2.5 * level * level - 40.5 * level + 360);
        }
        return level * level + 6 * level;
    }

//    public static int getExpToNext(HiveNetConnection connection) {
//        return getExpFromLevel(player.getLevel() + 1) - getExp(player);
//    }

    /**
     * Calculates level based on total experience
     *
     * @param exp
     * @return
     */
    public static double getLevelFromExp(double exp) {
        if (exp > 1395) {
            return (Math.sqrt(72 * exp - 54215) + 325) / 18;
        }
        if (exp > 315) {
            return Math.sqrt(40 * exp - 7839) / 10 + 8.1;
        }
        if (exp > 0) {
            return Math.sqrt(exp + 9) - 3;
        }
        return 0;
    }

    public static void addExp(HiveNetConnection connection, Class<? extends SkillClass> skill, double exp) {
        try {
            GamePlayerSkillsModel skillsModel = DataService.playerSkills.queryBuilder()
                    .where()
                    .eq("player_uuid", connection.getPlayer().uuid)
                    .and()
                    .eq("skill", skill.getSimpleName()).queryForFirst();

            double currentLevel = 1;

            if (skillsModel == null) {
                skillsModel = new GamePlayerSkillsModel();
                skillsModel.player = connection.getPlayer();
                skillsModel.skill = skill.getSimpleName();
                skillsModel.currentExp = exp;
            } else {
                currentLevel = getLevelFromExp(skillsModel.currentExp);
                skillsModel.currentExp += exp;
            }

            PlayerExpChangeEvent changeEvent = new PlayerExpChangeEvent(connection, skill, exp).call();
            if (changeEvent.isCanceled()) {
                return;
            }

            double newLevel = getLevelFromExp(skillsModel.currentExp);

            if (currentLevel > newLevel) {
                PlayerLevelUpEvent levelUpEvent = new PlayerLevelUpEvent(connection, skill, newLevel).call();
            }

            DataService.playerSkills.createOrUpdate(skillsModel);

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public JsonArray getPlayerSkills(HiveNetConnection connection) {
        JsonArray a = new JsonArray();
        for (SkillClass skillClass : this.skills) {

            JsonObject s = skillClass.toJson();

            double base = getExpFromLevel(1);
            double exp = base;
            try {
                GamePlayerSkillsModel skillsModel = DataService.playerSkills.queryBuilder()
                        .where()
                        .eq("player_uuid", connection.getPlayer().uuid)
                        .and()
                        .eq("skill", skillClass.getClass().getSimpleName()).queryForFirst();

                if (skillsModel != null) {
                    exp += skillsModel.currentExp;
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }

            double currentLevel = getLevelFromExp(exp);
            double nextLevelExp = getExpFromLevel((int) (Math.floor(currentLevel) + 1));
            double neededExp = getExpToNext((int) (Math.floor(currentLevel) + 1)) - exp;
            double expInLevel = exp - getExpFromLevel((int) Math.floor(currentLevel));


            s.addProperty("level", currentLevel);
            s.addProperty("exp", exp);
            s.addProperty("needed", neededExp);
            s.addProperty("percent", expInLevel / nextLevelExp);

            a.add(s);
        }

        return a;
    }

}
