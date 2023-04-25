package com.gamefocal.rivenworld.game.heightmap;

import com.badlogic.gdx.math.MathUtils;
import com.gamefocal.rivenworld.game.util.Location;

import java.io.InputStream;
import java.nio.ByteBuffer;

public class RawHeightmap {
    private int size = 0;
    private String worldFile;
    private short[][] heightData;

    public RawHeightmap(int worldSize, String dataFile) {
        this.size = worldSize;
        this.worldFile = dataFile;

        this.heightData = new short[this.size][this.size];
        this.loadHeightmapData(this.worldFile);
    }

    public short[][] getHeightData() {
        return heightData;
    }

    private void loadHeightmapData(String datFile) {

        try (InputStream fis = getClass().getClassLoader().getResourceAsStream(datFile)) {

            byte[] b = fis.readAllBytes();
            ByteBuffer buffer = ByteBuffer.wrap(b);

            for (int x = 0; x < this.size; x++) {
                for (int y = 0; y < this.size; y++) {
                    if (buffer.hasRemaining()) {
                        this.heightData[x][y] = buffer.getShort();
                    } else {
                        System.err.println("Buffer out of space");
                        break;
                    }
                }
            }

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

    public int getHeightValue(int x, int y) {
        return this.heightData[x][y];
    }

    public int getHeightFromLocation(Location location) {
        Location mapLoc = this.getMappedLocationFromGame(location);
        return this.getHeightValue(Math.round(mapLoc.getX()), Math.round(mapLoc.getY()));
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
        int heightValue = getHeightValue(x, y);
        return heightValue * zScale;
    }

    public int getSize() {
        return size;
    }

    public String getWorldFile() {
        return worldFile;
    }
}