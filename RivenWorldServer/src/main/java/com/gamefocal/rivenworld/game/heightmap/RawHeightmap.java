package com.gamefocal.rivenworld.game.heightmap;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.gamefocal.rivenworld.game.util.BufferUtil;
import com.gamefocal.rivenworld.game.util.Location;
import com.gamefocal.rivenworld.game.world.LandscapeType;
import com.gamefocal.rivenworld.game.world.WorldMetaData;

import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class RawHeightmap {
    private int size = 0;
    private String worldFile;
    private ByteBuffer heightData;
    private int worldSizeInUU = 201753;

    public RawHeightmap(int worldSize, String dataFile) {
        this.size = worldSize;
        this.worldFile = dataFile;
        this.loadHeightmapData(this.worldFile);
    }

    public static float calculateSlope(Location locA, Location locB) {
        Vector3 pointA = locA.toVector();
        Vector3 pointB = locB.toVector();

        Vector3 delta = new Vector3(pointB).sub(pointA);
        float distanceXY = (float) Math.sqrt(delta.x * delta.x + delta.y * delta.y);
        return delta.z / distanceXY;
    }

    public Location getWorldLocationFromXY(int x, int y) {
        return new Location((x * 100) - 25180, (y * 100) - 25180, this.getHeightValue(x, y));
    }

    public ByteBuffer getHeightData() {
        return heightData;
    }

    private void loadHeightmapData(String datFile) {

        try (InputStream fis = getClass().getClassLoader().getResourceAsStream(datFile)) {

            byte[] b = fis.readAllBytes();
            heightData = ByteBuffer.wrap(b);
            heightData.order(ByteOrder.BIG_ENDIAN);

//            while (buffer.hasRemaining()) {
//                short s = buffer.getShort();
//
//                this.heightData[x][y] = s;
//
//                if (++x >= this.size) {
//                    y++;
//                    x = 0;
//                }
//            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public float getHeightValue(int x, int y) {
        try {
            int index = BufferUtil.getPositionInByteBuffer((int) Math.ceil((this.worldSizeInUU / 100f)), (int) Math.ceil((this.worldSizeInUU / 100f)), x, y);
            return this.heightData.getFloat(index + 2);
        } catch (IllegalArgumentException e) {
            return 0;
        }
    }

    public WorldMetaData getMetaDataFromXY(Location location) {
        float x = location.getX() + 25180;
        float y = location.getY() + 25180;
        return this.getMetaDataFromXY((int) Math.floor(x / 100), (int) Math.floor(y / 100));
    }

    public WorldMetaData getMetaDataFromXY(int x, int y) {
        int index = BufferUtil.getPositionInByteBuffer((int) Math.ceil((this.worldSizeInUU / 100f)), (int) Math.ceil((this.worldSizeInUU / 100f)), x, y);

        byte biome = this.heightData.get(index);
        byte forest = this.heightData.get(index + 1);
        float height = this.heightData.getFloat(index + 2);

        return new WorldMetaData(new Location(x, y, 0), biome, forest, height);
    }

    public WorldMetaData findClosestCellWithLandscapeType(Location startLocation, LandscapeType desiredType, float maxSearchRadius) {
        int searchRadius = 1; // Start with a close radius and expand
        while (searchRadius <= maxSearchRadius) { // This could be limited by a max search radius to prevent infinite loops in edge cases
            for (int x = -searchRadius; x <= searchRadius; x++) {
                for (int y = -searchRadius; y <= searchRadius; y++) {
                    // Check only the perimeter of the square at the current radius
                    if (Math.abs(x) == searchRadius || Math.abs(y) == searchRadius) {
                        Location currentLocation = new Location(startLocation.getX() + x * 100, startLocation.getY() + y * 100, 0);
                        WorldMetaData metaData = getMetaDataFromXY(currentLocation);
                        if (metaData.getLandscapeType() == desiredType) {
                            return metaData; // Found the closest cell with the desired type
                        }
                    }
                }
            }
            searchRadius++; // Increase the radius for the next iteration
        }

        return null;
    }

    public float getHeightFromLocation(Location location) {
//        Location mapLoc = this.getMappedLocationFromGame(location);

        float x = location.getX() + 25180;
        float y = location.getY() + 25180;

        return this.getHeightValueInterpolated(x / 100, y / 100);
    }

    public float getHeightValueInterpolated(float x, float y) {
        int x1 = (int) x - 1;
        int x2 = (int) x + 1;
        int y1 = (int) y - 1;
        int y2 = (int) y + 1;

        float q11 = getHeightValue(x1, y1);
        float q12 = getHeightValue(x1, y2);
        float q21 = getHeightValue(x2, y1);
        float q22 = getHeightValue(x2, y2);

        float r1 = ((x2 - x) / (x2 - x1)) * q11 + ((x - x1) / (x2 - x1)) * q21;
        float r2 = ((x2 - x) / (x2 - x1)) * q12 + ((x - x1) / (x2 - x1)) * q22;

        float result = ((y2 - y) / (y2 - y1)) * r1 + ((y - y1) / (y2 - y1)) * r2;

        return result;
    }

    public Location getHeightLocationFromLocation(Location location) {
        location.setZ(this.getHeightFromLocation(location));
        return location;
    }

    public Vector3 getHeightVectorFromVector(Vector3 location) {
        location.z = this.getHeightFromLocation(Location.fromVector(location));
        return location;
    }

    public Location getMappedLocationFromGame(Location gameLoc) {
        return new Location(
                MathUtils.map(-25181.08f / 100, 176573.27f / 100, 0, this.size, gameLoc.getX() / 100),
                MathUtils.map(-25181.08f / 100, 176573.27f / 100, 0, this.size, gameLoc.getY() / 100),
                0f
        );
    }

    public Location getWorldLocationFrom2DMap(Location mapLocation) {
        return new Location(
                MathUtils.map(0, this.size, -25181.08f / 100, 176573.27f / 100, mapLocation.getX()) * 100,
                MathUtils.map(0, this.size, -25181.08f / 100, 176573.27f / 100, mapLocation.getY()) * 100,
                this.getHeightFrom2DLocation(mapLocation)
        );
    }

    public float getHeightFrom2DLocation(Location location) {
        return this.getHeightValueWithScale(Math.round(location.getX()), Math.round(location.getY()), 200, 200, 125);
    }

    public float getHeightValueWithScale(int x, int y, float xScale, float yScale, float zScale) {
        float heightValue = getHeightValue(x, y);
        return heightValue * zScale;
    }

    public int getSize() {
        return size;
    }

    public String getWorldFile() {
        return worldFile;
    }
}