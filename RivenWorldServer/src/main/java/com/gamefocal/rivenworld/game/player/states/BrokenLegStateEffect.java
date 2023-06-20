package com.gamefocal.rivenworld.game.player.states;

import com.gamefocal.rivenworld.game.player.PlayerStateEffect;
import com.gamefocal.rivenworld.game.ui.UIIcon;
import com.gamefocal.rivenworld.game.util.RandomUtil;

public class BrokenLegStateEffect extends PlayerStateEffect {

    public BrokenLegStateEffect() {
        super("broken-leg", "Broken Legs", UIIcon.FORAGING, 60 * 30, 30);
    }

    @Override
    public void run() {
        this.player.SetSpeed(400);
    }

    @Override
    public void onStart() {
        this.player.setSpeed(400);
    }

    @Override
    public void onComplete() {
        this.player.resetSpeed();
    }
}
