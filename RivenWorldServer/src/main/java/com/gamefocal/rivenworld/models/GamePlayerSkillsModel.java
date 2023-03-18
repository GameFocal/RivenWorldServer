package com.gamefocal.rivenworld.models;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable
public class GamePlayerSkillsModel {

    @DatabaseField(generatedId = true)
    public int id;

    @DatabaseField(foreign = true, foreignAutoRefresh = true, canBeNull = false, foreignColumnName = "uuid")
    public PlayerModel playerModel;

    @DatabaseField
    public String skill;

    @DatabaseField
    public float expLevel = 0f;

    @DatabaseField
    public int currentLevel = 1;

    @DatabaseField
    public int nextLevelExp = 0;

}
