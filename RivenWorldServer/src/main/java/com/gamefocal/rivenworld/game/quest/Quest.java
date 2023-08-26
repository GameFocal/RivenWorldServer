package com.gamefocal.rivenworld.game.quest;

import java.util.LinkedList;
import java.util.List;

public class Quest {

    private Quest prev;
    private Quest next;

    private String name;

    private String desc;

    private LinkedList<QuestGoal> goals = new LinkedList<>();

    public Quest(Quest prev, Quest next, String name, String desc, QuestGoal ... goals) {
        this.prev = prev;
        this.next = next;
        this.name = name;
        this.desc = desc;
        this.goals.addAll(List.of(goals));
    }

    public Quest getPrev() {
        return prev;
    }

    public void setPrev(Quest prev) {
        this.prev = prev;
    }

    public Quest getNext() {
        return next;
    }

    public void setNext(Quest next) {
        this.next = next;
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
