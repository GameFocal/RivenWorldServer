package com.gamefocal.rivenworld.game.quest;

import java.io.Serializable;
import java.util.LinkedList;

public class QuestLine extends LinkedList<Quest> implements Serializable {
    private String name;

    public QuestLine(String name, Quest startingQuest) {
        this.name = name;
        this.add(startingQuest);
    }
}
