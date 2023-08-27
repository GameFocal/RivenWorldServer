package com.gamefocal.rivenworld.game.quest;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

public class Quest implements Serializable {

    private String name;

    private String desc;

    private LinkedList<QuestGoal> goals = new LinkedList<>();

    private int currentGoalIndex = 0;

    public Quest(String name, String desc, QuestGoal ... goals) {
        this.name = name;
        this.desc = desc;
        this.goals.addAll(List.of(goals));
    }

    public int getCurrentGoalIndex() {
        return currentGoalIndex;
    }

    public void setCurrentGoalIndex(int currentGoalIndex) {
        this.currentGoalIndex = currentGoalIndex;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public LinkedList<QuestGoal> getGoals() {
        return goals;
    }

    public void setGoals(LinkedList<QuestGoal> goals) {
        this.goals = goals;
    }
}
