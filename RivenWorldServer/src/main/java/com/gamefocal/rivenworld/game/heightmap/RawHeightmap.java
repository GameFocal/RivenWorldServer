package com.gamefocal.rivenworld.game.heightmap;

import com.badlogic.gdx.math.MathUtils;
import com.gamefocal.rivenworld.game.util.BufferUtil;
import com.gamefocal.rivenworld.game.util.Location;

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

        int index = BufferUtil.getPositionInByteBuffer((this.worldSizeInUU / 100), (this.worldSizeInUU / 100), x, y);

        return this.heightData.getFloat(index);
    }

    public float getHeightFromLocation(Location location) {
//        Location mapLoc = this.getMappedLocationFromGame(location);

        float x = location.getX() + 25180;
        float y = location.getY() + 25180;

        return this.getHeightValue(Math.round(x) / 100, Math.round(y) / 100);
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