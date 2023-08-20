package com.gamefocal.rivenworld.entites.prefab;

import com.badlogic.gdx.math.Vector3;
import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.util.Location;

import java.io.Serializable;
import java.util.ArrayList;

public class Prefab implements Serializable {
    private Location baseLocation = new Location(0, 0, 0);
    private ArrayList<PrefabRecord> entities = new ArrayList<>();

    public Prefab(Location baseLocation) {
        this.baseLocation = baseLocation;
    }

    public Location getBaseLocation() {
        return baseLocation;
    }

    public boolean hasData() {
        return this.entities.size() > 0;
    }

    public void addEntity(GameEntity entity) {
        Location loc = entity.location.cpy().getRelativeLocation(this.baseLocation);
        PrefabRecord record = new PrefabRecord(entity, loc);
        this.entities.add(record);
    }

    public ArrayList<GameEntity> getEntitesInWorldSpace() {
        ArrayList<GameEntity> e = new ArrayList<>();
        for (PrefabRecord r : this.entities) {
            Location location = r.getRelative().cpy();
            Location ws = this.baseLocation.toWorldSpace(location);
            GameEntity ee = r.getGameEntity();
            ee.location = ws;
            e.add(ee);
        }
        return e;
    }

    public ArrayList<PrefabRecord> getEntities() {
        return entities;
    }
}
