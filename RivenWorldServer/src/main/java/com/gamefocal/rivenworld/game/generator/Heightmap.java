package com.gamefocal.rivenworld.game.generator;

import com.badlogic.gdx.math.MathUtils;
import com.gamefocal.rivenworld.game.util.Location;
import org.apache.commons.lang3.tuple.Pair;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Heightmap implements Iterable<Pair<Integer, Integer>> {

    private float cellSize = 0f;
    private float x = 0;
    private float y = 0;
    private HeightMapTile[][] cells = new HeightMapTile[0][0];
    private boolean useTiles = false;
    private Location offset = new Location(0, 0, 0);
    private BufferedImage bufferedImage;

    public Heightmap() {
    }

    public void setOffset(Location offset) {
        this.offset = offset;
    }

    public float getHeightFromLocation(Location location) {
        if (this.useTiles) {
            HeightMapTile render = this.getCellFromLocation(location);
            if (render != null) {

                int localX = (int) ((location.getX() / 100) % (this.cellSize / 100));
                int localY = (int) ((location.getY() / 100) % (this.cellSize / 100));

//                System.out.println(localX + "/" + localY);

                return render.getHeight(localX, localY);
            }
        } else {
            // Single Heightmap Data

            Location map = this.getMappedLocationFromGame(location);

            return (this.cells[0][0].getHeight((int) map.getX(), (int) map.getY()) - 4500);
        }

        return -1f;
    }

    public float getHeightFrom2DLocation(Location location) {
        return (this.cells[0][0].getHeight((int) location.getX(), (int) location.getY()) - 4500);
    }

    public float size() {
        if (this.useTiles) {
            return (this.cellSize * this.cells.length);
        } else {
            return this.cellSize;
        }
    }

    public HeightMapTile getCellFromLocation(Location location) {
        float x = (float) Math.floor(location.getX() / cellSize);
        float y = (float) Math.floor(location.getY() / cellSize);

//        System.out.println("Cell: " + x + ", " + y);

        try {
            return this.cells[(int) x][(int) y];
        } catch (Exception e) {
            return null;
        }
    }

    public Location getMappedLocationFromGame(Location gameLoc) {
        return new Location(
                MathUtils.map(-25181.08f, 176573.27f, 0, 1008, gameLoc.getX()),
                MathUtils.map(-25181.08f, 176573.27f, 0, 1008, gameLoc.getY()),
                0f
        );
    }

    public Location getWorldLocationFrom2DMap(Location mapLocation) {
        return new Location(
                MathUtils.map(0, 1007, -25181.08f, 176573.27f, mapLocation.getX()),
                MathUtils.map(0, 1007, -25181.08f, 176573.27f, mapLocation.getY()),
                this.getHeightFrom2DLocation(mapLocation)
        );
    }

    public void loadFromImageSet(String path) {
        this.cells = new HeightMapTile[1][1];

        File f = new File(path);

        try {

//            ByteBuffer buffer = ByteBuffer.wrap(Files.readAllBytes(f.toPath()));
//
//            BufferedImage io = new BufferedImage(1008, 1008, BufferedImage.TYPE_USHORT_GRAY);
//            short[] data = ((DataBufferUShort) io.getRaster().getDataBuffer()).getData();
//            System.arraycopy(realData, 0, data, 0, realData.length);
//
//            int i = 0;
//            while (buffer.hasRemaining()) {
//                data[i++] = (short) BufferUtil.getUnsignedShort(buffer);
//            }

            bufferedImage = ImageIO.read(f);

            if (this.cellSize == 0) {
                // Set the size here.
                this.cellSize = (bufferedImage.getWidth() * 100);
            }

            /*
             * Load the tile set here using Heightmap read
             * */
//                    HeightMapRender render = new HeightMapRender(io, .01f, .01f, false, 0);

            HeightMapTile tile = new HeightMapTile(bufferedImage, 1, 1, 4);

            this.cells[0][0] = tile;

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public BufferedImage getBufferedImage() {
        return bufferedImage;
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

                    HeightMapTile tile = new HeightMapTile(io, 100, 100, 4);

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
        return new WorldLocationIterator(this, 1);
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
