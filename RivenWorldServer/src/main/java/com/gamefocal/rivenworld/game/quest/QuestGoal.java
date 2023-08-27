package com.gamefocal.rivenworld.game.quest;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;

import java.io.Serializable;

public abstract class QuestGoal implements Serializable {

    private float percentComplete = 0;

    private String title;

    private String desc;

    public abstract void onWork(HiveNetConnection connection);
    public abstract void onComplete(HiveNetConnection connection);

}
