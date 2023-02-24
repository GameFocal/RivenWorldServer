package com.gamefocal.rivenworld.game.ray.hit;

import com.gamefocal.rivenworld.DedicatedServer;
import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.ray.HitResult;
import com.gamefocal.rivenworld.game.util.Location;
import com.gamefocal.rivenworld.service.PlayerService;

import java.util.UUID;

public class PlayerHitResult implements HitResult<HiveNetConnection> {

    private UUID netPlayerUUID;

    private Location hitLocation;

    public PlayerHitResult(UUID netPlayerUUID, Location hitLocation) {
        this.netPlayerUUID = netPlayerUUID;
        this.hitLocation = hitLocation;
    }

    public UUID getNetPlayerUUID() {
        return netPlayerUUID;
    }

    public Location getHitLocation() {
        return hitLocation;
    }

    @Override
    public HiveNetConnection get() {
        return DedicatedServer.get(PlayerService.class).players.get(this.netPlayerUUID);
    }
}
