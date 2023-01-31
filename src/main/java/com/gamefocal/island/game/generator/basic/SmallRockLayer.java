package com.gamefocal.island.game.generator.basic;

import com.gamefocal.island.entites.util.NoiseGenerator;
import com.gamefocal.island.game.World;
import com.gamefocal.island.game.entites.resources.rock.SmallRockEntity;
import com.gamefocal.island.game.generator.WorldLayerGenerator;
import com.gamefocal.island.game.util.Location;
import org.apache.commons.lang3.tuple.Pair;

public class SmallRockLayer implements WorldLayerGenerator {
    @Override
    public void generateLayer(World world) {

        /*
         * Place small rocks around the world
         * */
        NoiseGenerator generator = new NoiseGenerator();
        generator.setDefault_size((long) world.generator.getHeightmap().size());

        Location location = Location.fromString("24477.59,35776.832,15506.345,0.0,0.0,0.0");

        float h = world.generator.getHeightmap().getHeightFromLocation(location);

        location.setZ(world.generator.getHeightmap().getHeightFromLocation(location));

        System.out.println("Reported Real: 24477.59,35776.832,15506.345,0.0,0.0,0.0");
        System.out.println("Spawning at " + location);

//        world.spawn(new SmallRockEntity(),location);

//        for (Pair<Integer, Integer> cord : world.generator.getHeightmap()) {
//            float h = world.generator.getHeightmap().getHeightFromLocation(new Location(cord.getLeft(), cord.getRight(), 0));
//            if (h >= -80) {
//                float n = (float) generator.noise(cord.getLeft(), cord.getRight());
//                if (n >= .75) {
//                    // Spawn a small rock.
//                    world.spawn(new SmallRockEntity(), new Location(cord.getLeft(), cord.getRight(), h));
//                }
//            }
//        }

    }
}
