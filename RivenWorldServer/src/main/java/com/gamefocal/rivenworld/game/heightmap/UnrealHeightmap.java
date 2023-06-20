package com.gamefocal.rivenworld.game.heightmap;

import com.badlogic.gdx.math.MathUtils;
import com.gamefocal.rivenworld.game.util.Location;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferUShort;
import java.io.IOException;
import java.io.InputStream;

public class UnrealHeightmap {
    private int tileWidth;
    private int tileHeight;
    private int totalTiles;
    private int width;
    private int height;
    private int[][] heightData;

    public UnrealHeightmap(String[] filePaths, int tileWidth, int tileHeight, int totalTiles) {
        this.tileWidth = tileWidth;
        this.tileHeight = tileHeight;
        this.totalTiles = totalTiles;

        int tilesPerRow = (int) Math.ceil(Math.sqrt(totalTiles));
        this.width = tileWidth * tilesPerRow;
        this.height = tileHeight * tilesPerRow;

        this.heightData = new int[height][width];

        try {
            loadHeightmapData(filePaths);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int[][] getHeightData() {
        return heightData;
    }

    private void loadHeightmapData(String[] filePaths) throws IOException {
        int tilesPerRow = (int) Math.ceil(Math.sqrt(totalTiles));

        for (int tileIndex = 0; tileIndex < filePaths.length; tileIndex++) {
            try (InputStream fis = getClass().getClassLoader().getResourceAsStream(filePaths[tileIndex])) {
                BufferedImage image = ImageIO.read(fis);
                DataBufferUShort dataBuffer = (DataBufferUShort) image.getRaster().getDataBuffer();

                int tileY = tileIndex / tilesPerRow;
                int tileX = tileIndex % tilesPerRow;

                for (int y = 0; y < tileHeight; y++) {
                    for (int x = 0; x < tileWidth; x++) {
                        int heightValue = dataBuffer.getElem(y * tileWidth + x) & 0xFFFF;
                        heightData[tileY * tileHeight + y][tileX * tileWidth + x] = heightValue;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public int getHeightValue(int x, int y) {
        if (x >= 0 && x < width && y >= 0 && y < height) {
            return heightData[y][x];
        } else {
            throw new IllegalArgumentException("Invalid coordinates: x=" + x + ", y=" + y);
        }
    }

    public float getHeightFromLocation(Location location) {
        Location mapLoc = this.getMappedLocationFromGame(location);
        return this.getHeightValue(Math.round(mapLoc.getX()), Math.round(mapLoc.getY()));
    }

    public Location getMappedLocationFromGame(Location gameLoc) {
        return new Location(
                MathUtils.map(-25181.08f, 176573.27f, 0, this.width, gameLoc.getX()),
                MathUtils.map(-25181.08f, 176573.27f, 0, this.height, gameLoc.getY()),
                0f
        );
    }

    public Location getWorldLocationFrom2DMap(Location mapLocation) {
        return new Location(
                MathUtils.map(0, this.width, -25181.08f, 176573.27f, mapLocation.getX()),
                MathUtils.map(0, this.height, -25181.08f, 176573.27f, mapLocation.getY()),
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

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}