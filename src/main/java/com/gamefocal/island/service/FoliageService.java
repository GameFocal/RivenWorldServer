package com.gamefocal.island.service;

import com.gamefocal.island.entites.service.HiveService;
import com.gamefocal.island.game.GameEntity;
import com.gamefocal.island.game.entites.trees.TreeLarge;
import com.gamefocal.island.game.entites.trees.TreeMedium;
import com.gamefocal.island.game.entites.trees.TreeSapling;
import com.gamefocal.island.game.util.Location;
import com.google.auto.service.AutoService;
import org.apache.commons.codec.digest.DigestUtils;

import javax.inject.Singleton;
import java.util.Hashtable;

@Singleton
@AutoService(HiveService.class)
public class FoliageService implements HiveService<FoliageService> {

    private Hashtable<String, GameEntity> foliageEntites = new Hashtable<>();

    @Override
    public void init() {

    }

    public void register(String name, Location location) {
        String hash = DigestUtils.md5Hex(name + location);

        if (!foliageEntites.containsKey(hash)) {
            if (name.contains("TreeFirLarge")) {
                foliageEntites.put(hash, new TreeLarge());
            } else if (name.contains("TreeFirSaplings")) {
                foliageEntites.put(hash, new TreeSapling());
            } else if (name.contains("TreeFirMedium")) {
                foliageEntites.put(hash, new TreeMedium());
            } else {
                System.out.println("Unknown Tree: " + name);
            }
        }
    }
}
