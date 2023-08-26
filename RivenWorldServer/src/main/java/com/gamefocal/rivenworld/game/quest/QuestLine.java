package com.gamefocal.rivenworld.game.quest;

import java.util.LinkedList;

public class QuestLine {

    private String name;

    private Quest root = null;

    private Quest currentQuest = null;

    public QuestLine(String name, Quest startingQuest) {
        this.name = name;
        this.root = startingQuest;
        this.currentQuest = this.root;

    }

    public void addQuestToEnd(Quest quest) {
    }

}
