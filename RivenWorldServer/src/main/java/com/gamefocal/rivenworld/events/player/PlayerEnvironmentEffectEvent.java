package com.gamefocal.rivenworld.events.player;

import com.gamefocal.rivenworld.entites.events.Event;
import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.enviroment.player.PlayerEnvironmentEffect;

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
