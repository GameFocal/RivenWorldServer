package com.gamefocal.island.game.ray.hit;

import com.gamefocal.island.DedicatedServer;
import com.gamefocal.island.game.foliage.FoliageState;
import com.gamefocal.island.game.ray.HitResult;
import com.gamefocal.island.game.util.Location;
import com.gamefocal.island.models.GameFoliageModel;
import com.gamefocal.island.service.DataService;
import com.gamefocal.island.service.FoliageService;

import java.sql.SQLException;

public class FoliageHitResult implements HitResult<GameFoliageModel> {

    private Location hitLocation;

    private int index;

    private Location foliageLocation;

    private String name;

    public FoliageHitResult(Location hitLocation, int index, Location foliageLocation, String name) {
        this.hitLocation = hitLocation;
        this.index = index;
        this.foliageLocation = foliageLocation;
        this.name = name;
    }

    public Location getHitLocation() {
        return hitLocation;
    }

    public int getIndex() {
        return index;
    }

    public Location getFoliageLocation() {
        return foliageLocation;
    }

    public String getName() {
        return name;
    }

    @Override
    public GameFoliageModel get() {

//        String hash = FoliageService.getHash(name, this.foliageLocation.toString());
//        try {
//            GameFoliageModel f = DataService.gameFoliage.queryForId(hash);
//            if (f == null) {
//                f = new GameFoliageModel();
//                f.uuid = hash;
//                f.modelName = name;
//                f.foliageIndex = this.index;
//                f.foliageState = FoliageState.GROWN;
//                f.health = DedicatedServer.get(FoliageService.class).getStartingHealth(name);
//                f.growth = 100;
//                f.location = this.foliageLocation;
//
//                DataService.gameFoliage.createOrUpdate(f);
//            }
//
//            return f;
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }

        return null;
    }
}
