package com.gamefocal.island.game.generator;

import com.gamefocal.island.game.util.Location;
import org.apache.commons.lang3.tuple.Pair;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Heightmap implements Iterable<Pair<Integer, Integer>> {

    private float cellSize = 0f;
    private float x = 0;
    private float y = 0;
    private HeightMapTile[][] cells = new HeightMapTile[0][0];

    public Heightmap() {
    }

    public float getHeightFromLocation(Location location) {
        HeightMapTile render = this.getCellFromLocation(location);
        if (render != null) {

            int localX = (int) ((location.getX()/100) % (this.cellSize/100));
            int localY = (int) ((location.getY()/100) % (this.cellSize/100));

            System.out.println(localX + "/" + localY);

            return render.getHeight(localX, localY);
        }

        return -1f;
    }

    public float size() {
        return (this.cellSize * this.cells.length);
    }

    public HeightMapTile getCellFromLocation(Location location) {
        float x = (float) Math.floor(location.getX() / cellSize);
        float y = (float) Math.floor(location.getY() / cellSize);

        System.out.println("Cell: " + x + ", " + y);

        try {
            return this.cells[(int) x][(int) y];
        } catch (Exception e) {
            return null;
        }
    }

    public void loadFromImageSet(int tilexy, String... paths) {

        this.cells = new HeightMapTile[tilexy][tilexy];

        Pattern p = Pattern.compile("(.*)x([0-9]*).*y([0-9]*).*", Pattern.MULTILINE | Pattern.CASE_INSENSITIVE | Pattern.COMMENTS);

        // Load image from the Jar
        for (String path : paths) {
            Matcher m = p.matcher(path);
            if (m.find()) {
                // Exist

                String worldName = m.group(1);
                String x = m.group(2);
                String y = m.group(3);

                File f = new File(getClass().getClassLoader().getResource(path).getFile());

//                FileHandle handle = new FileHandle(f);

//                FileHandle fh = new FileHandle(f);

                try {
                    BufferedImage io = ImageIO.read(f);

                    if (this.cellSize == 0) {
                        // Set the size here.
                        this.cellSize = (io.getWidth() * 100);
                    }

                    /*
                     * Load the tile set here using Heightmap read
                     * */
//                    HeightMapRender render = new HeightMapRender(io, .01f, .01f, false, 0);

                    HeightMapTile tile = new HeightMapTile(io);

                    this.cells[Integer.parseInt(x)][Integer.parseInt(y)] = tile;

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        this.x = this.cells.length;
        this.y = this.cells[0].length;
    }

    public float getCellSize() {
        return cellSize;
    }

    public float getXLen() {
        return x;
    }

    public float getYLen() {
        return y;
    }

    public HeightMapTile[][] getCells() {
        return cells;
    }

    @Override
    public Iterator<Pair<Integer, Integer>> iterator() {
        return new WorldLocationIterator(this,1);
    }

    static class WorldLocationIterator implements Iterator<Pair<Integer, Integer>> {

        private int x = 0;

        private int y = 0;

        private Heightmap heightmap;

        private int xMax = 0;

        private int yMax = 0;

        private int step = 1;

        public WorldLocationIterator(Heightmap heightmap, int step) {
            this.heightmap = heightmap;
            this.xMax = (int) (heightmap.getCellSize() * heightmap.getXLen()) - 1;
            this.yMax = (int) (heightmap.getCellSize() * heightmap.getYLen()) - 1;
            this.step = step;
        }

        @Override
        public boolean hasNext() {
            return (x <= xMax && y <= yMax);
        }

        @Override
        public Pair next() {

            x += this.step;

            if (x >= xMax) {
                x = 0;
                y += this.step;
            }

            return Pair.of(x, y);
        }
    }
}
