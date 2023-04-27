package com.gamefocal.rivenworld.game.ai.path;

import com.badlogic.gdx.math.collision.BoundingBox;
import com.gamefocal.rivenworld.game.World;
import com.gamefocal.rivenworld.game.util.Location;

public class WorldGrid {

    private World world;

    private int worldSize = 201753;

    private WorldCell[][] cells = new WorldCell[0][0];

    public WorldGrid(World world) {
        this.world = world;
        this.cells = new WorldCell[this.worldSize / 100][this.worldSize / 100];

        System.out.println("Generating AI Grid " + this.cells.length + "x" + this.cells.length);

        for (int x = 0; x < this.cells.length; x++) {
            for (int y = 0; y < this.cells.length; y++) {
                this.cells[x][y] = new WorldCell(this.world, this, x, y);
            }
        }

        System.out.println("Finished generating AI Grid.");

    }

    public void refreshOverlaps(BoundingBox boundingBox) {
        for (WorldCell[] cells : this.cells) {
            for (WorldCell c : cells) {
                if (c.getBoundingBox().contains(boundingBox) || c.getBoundingBox().intersects(boundingBox)) {
                    c.refresh();
                }
            }
        }
    }

    public WorldCell get(int x, int y) {
        try {
            return this.cells[x][y];
        } catch (ArrayIndexOutOfBoundsException e) {
            return null;
        }
    }

    public void syncGrid() {
        for (WorldCell[] cells : this.cells) {
            for (WorldCell cell : cells) {
                cell.refresh();
            }
        }
    }

    public WorldCell getCellFromGameLocation(Location location) {
        int x = (int) Math.floor((location.getX() + 25180) / 100);
        int y = (int) Math.floor((location.getY() + 25180) / 100);
        return this.get(x, y);
    }

    public World getWorld() {
        return world;
    }

    public int getWorldSize() {
        return worldSize;
    }

    public WorldCell[][] getCells() {
        return cells;
    }
}
