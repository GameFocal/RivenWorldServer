package com.gamefocal.island.game.ray.hit;

import com.gamefocal.island.DedicatedServer;
import com.gamefocal.island.entites.net.HiveNetConnection;
import com.gamefocal.island.game.ray.HitResult;
import com.gamefocal.island.game.util.Location;
import com.gamefocal.island.service.PlayerService;

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
