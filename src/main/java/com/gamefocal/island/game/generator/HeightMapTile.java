package com.gamefocal.island.game.generator;

import com.badlogic.gdx.math.MathUtils;
import com.gamefocal.island.game.util.MathUtil;

import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;

public class HeightMapTile {

    BufferedImage image;

    float[][] heightmap;

    public HeightMapTile(BufferedImage image) {
        this.image = image;

        this.heightmap = new float[image.getWidth()][image.getHeight()];

        WritableRaster raster = image.getRaster();

        float maxValue = (1 << image.getColorModel().getComponentSize(0)) - 1;

        System.out.println("MAX VALUE: " + maxValue);

        for (int x = 0; x < heightmap.length; x++) {
            for (int y = 0; y < heightmap[0].length; y++) {
                heightmap[x][y] = raster.getSample(x, y, 0) / maxValue;
            }
        }
    }

    public float getHeight(int x, int y) {
        float h = this.heightmap[x][y];
        return h*51200;
//        return MathUtils.map(0, 65535, 0, 51200, h);
    }

    public BufferedImage getImage() {
        return image;
    }

    public float[][] getHeightmap() {
        return heightmap;
    }
}
