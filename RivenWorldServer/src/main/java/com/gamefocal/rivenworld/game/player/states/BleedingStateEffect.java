package com.gamefocal.rivenworld.game.player.states;

import com.gamefocal.rivenworld.game.player.PlayerStateEffect;
import com.gamefocal.rivenworld.game.ui.UIIcon;
import com.gamefocal.rivenworld.game.util.RandomUtil;

public class BleedingStateEffect extends PlayerStateEffect {

    public BleedingStateEffect() {
        super("bleeding", "Bleeding", UIIcon.FORAGING, 60 * 10, 60);
    }

    @Override
    public void run() {
        this.player.takeDamage(5);
    }

    @Override
    public void onStart() {

    }

    @Override
    public void onComplete() {

    }
}
