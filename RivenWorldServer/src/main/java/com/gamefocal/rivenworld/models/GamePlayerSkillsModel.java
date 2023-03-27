package com.gamefocal.rivenworld.models;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable
public class GamePlayerSkillsModel {

    @DatabaseField(generatedId = true)
    public int id;

    @DatabaseField(foreign = true, foreignAutoRefresh = true, canBeNull = false, foreignColumnName = "uuid")
    public PlayerModel player;

    @DatabaseField
    public String skill;

    @DatabaseField
    public double currentExp = 0f;

}
