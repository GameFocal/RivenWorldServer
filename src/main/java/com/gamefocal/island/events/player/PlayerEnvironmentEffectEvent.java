package com.gamefocal.island.events.player;

import com.gamefocal.island.entites.events.Event;
import com.gamefocal.island.entites.net.HiveNetConnection;
import com.gamefocal.island.game.enviroment.player.PlayerEnvironmentEffect;

public class PlayerEnvironmentEffectEvent extends Event<PlayerEnvironmentEffect> {
    private PlayerEnvironmentEffect environmentEffect;
    private HiveNetConnection connection;

    public PlayerEnvironmentEffectEvent(HiveNetConnection connection, PlayerEnvironmentEffect environmentEffect) {
        this.environmentEffect = environmentEffect;
        this.connection = connection;
    }

    public PlayerEnvironmentEffect getEnvironmentEffect() {
        return environmentEffect;
    }
}
