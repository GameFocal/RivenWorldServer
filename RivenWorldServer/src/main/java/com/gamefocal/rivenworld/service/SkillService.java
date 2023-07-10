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

    public static void addExp(HiveNetConnection connection, Class<? extends SkillClass> skill, double exp) {
        connection.getSkillTree().addExp(skill, exp);
    }

    public static double getExpOfPlayer(HiveNetConnection connection, Class<? extends SkillClass> skill) {
        return connection.getSkillTree().getExp(skill);
    }

    public static double getLevelOfPlayer(HiveNetConnection connection, Class<? extends SkillClass> skill) {
        return connection.getSkillTree().getLevel(skill);
    }

    @Override
    public void init() {
        this.registerSkill(
                new CraftingSkill(),
                new WoodcuttingSkill(),
                new MiningSkill(),
                new SmeltingSkill(),
                new ForagingSkill(),
                new MetalWorkingSkill(),
                new WoodWorkingSkill(),
                new MasonrySkill(),
                new HuntingSkill(),
                new CookingSkill(),
                new OneHandedCombatSkill(),
                new LongBowSkill(),
                new TwoHandedCombatSkill(),
                new SpearCombatSkill(),
                new BlockingSkill()
        );
    }

    public LinkedList<SkillClass> getSkills() {
        return skills;
    }

    public void registerSkill(SkillClass... skills) {
        this.skills.addAll(Arrays.asList(skills));
    }

    public JsonArray getPlayerSkills(HiveNetConnection connection) {
        return connection.getSkillTree().skills();
    }

}
