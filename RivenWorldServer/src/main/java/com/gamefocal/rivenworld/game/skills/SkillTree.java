package com.gamefocal.rivenworld.game.skills;

import com.gamefocal.rivenworld.DedicatedServer;
import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.skills.skillTypes.SkillClass;
import com.gamefocal.rivenworld.models.GamePlayerSkillsModel;
import com.gamefocal.rivenworld.service.DataService;
import com.gamefocal.rivenworld.service.SkillService;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.sql.SQLException;
import java.util.concurrent.ConcurrentHashMap;

public class SkillTree {

    private final HiveNetConnection connection;

    private final ConcurrentHashMap<Class<? extends SkillClass>, GamePlayerSkillsModel> skills = new ConcurrentHashMap<>();

    public SkillTree(HiveNetConnection connection) {
        this.connection = connection;
        this.loadFromDB();
    }

    /**
     * The formulas for figuring out how many experience orbs you need to get to the next level are as follows
     *
     * @param level
     * @return
     */
    private int getExpToNext(int level) {
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
    public int getExpFromLevel(int level) {
        if (level > 30) {
            return (int) (4.5 * level * level - 162.5 * level + 2220);
        }
        if (level > 15) {
            return (int) (2.5 * level * level - 40.5 * level + 360);
        }
        return level * level + 6 * level;
    }

    /**
     * Calculates level based on total experience
     *
     * @param exp
     * @return
     */
    public double getLevelFromExp(double exp) {
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

    public double getExp(Class<? extends SkillClass> skill) {
        if (this.skills.containsKey(skill)) {
            return this.skills.get(skill).currentExp;
        }

        return 0;
    }

    public void addExp(Class<? extends SkillClass> skill, double exp) {
        this.skills.get(skill).currentExp += exp;
    }

    public JsonArray skills() {
        JsonArray a = new JsonArray();
        for (SkillClass skillClass : DedicatedServer.get(SkillService.class).getSkills()) {

            JsonObject s = skillClass.toJson();

            double exp = getExpFromLevel(1);
            if (this.skills.containsKey(skillClass.getClass())) {
                exp += this.skills.get(skillClass.getClass()).currentExp;
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

    public int getLevel(Class<? extends SkillClass> skill) {
        return (int) Math.round(this.getLevelFromExp(this.getExp(skill)));
    }

    public void loadFromDB() {
        SkillService skillService = DedicatedServer.get(SkillService.class);

        for (SkillClass skillClass : skillService.getSkills()) {
            try {
                GamePlayerSkillsModel skillsModel = DataService.playerSkills.queryBuilder()
                        .where()
                        .eq("player_uuid", connection.getPlayer().uuid)
                        .and()
                        .eq("skill", skillClass.getClass().getSimpleName()).queryForFirst();

                if (skillsModel == null) {
                    skillsModel = new GamePlayerSkillsModel();
                    skillsModel.player = this.connection.getPlayer();
                    skillsModel.currentExp = 0;
                    skillsModel.skill = skillClass.getClass().getSimpleName();
                }

                this.skills.put(skillClass.getClass(), skillsModel);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }

    public void saveToDB() {
        for (GamePlayerSkillsModel skillsModel : this.skills.values()) {
            try {
                DataService.playerSkills.createOrUpdate(skillsModel);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }
}
