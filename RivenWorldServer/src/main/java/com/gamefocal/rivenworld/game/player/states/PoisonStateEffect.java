package com.gamefocal.rivenworld.game.player.states;

import com.gamefocal.rivenworld.game.player.PlayerStateEffect;
import com.gamefocal.rivenworld.game.ui.UIIcon;
import com.gamefocal.rivenworld.game.util.RandomUtil;

public class PoisonStateEffect extends PlayerStateEffect {

    public PoisonStateEffect(int timeInSeconds) {
        super("poison", "Poisoned", UIIcon.FORAGING, 60L * timeInSeconds, 30);
    }

    @Override
    public void run() {
        this.player.takeDamage(RandomUtil.getRandomNumberBetween(1, 5));
    }

    @Override
    public void onStart() {

    }

    @Override
    public void onComplete() {

    }
}
