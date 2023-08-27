package com.gamefocal.rivenworld.game.quest;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;

public abstract class QuestReward {
    abstract public void onReward(HiveNetConnection connection);
}
