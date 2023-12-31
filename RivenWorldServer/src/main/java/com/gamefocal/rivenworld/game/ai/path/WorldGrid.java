package com.gamefocal.rivenworld.game.ai.path;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.gamefocal.rivenworld.game.util.Location;
import com.gamefocal.rivenworld.game.world.World;

import java.util.ArrayList;
import java.util.List;

public class WorldGrid {

    private World world;

    private int worldSize = 201753;

    private WorldCell[][] cells = new WorldCell[0][0];

    public WorldGrid(World world) {
        this.world = world;
        this.cells = new WorldCell[(int) Math.ceil(this.worldSize / 100f)][(int) Math.ceil(this.worldSize / 100f)];

        System.out.println("Generating AI Grid " + this.cells.length + "x" + this.cells.length);

        for (int x = 0; x < this.cells.length; x++) {
            for (int y = 0; y < this.cells.length; y++) {
                this.cells[x][y] = new WorldCell(this.world, this, x, y);
            }
        }

        System.out.println("Finished generating AI Grid.");
    }

    public void assignWaterValues() {
        for (WorldCell[] cells : this.cells) { // Assuming you have a method to get all cells
            for (WorldCell cell : cells) {
                if (cell.isWaterSource()) {
                    propagateWaterValue(cell, 1.0f);
                }
            }
        }
    }

    private void propagateWaterValue(WorldCell sourceCell, float currentValue) {
        if (currentValue <= 0.0f) {
            return;
        }

        for (WorldCell neighbor : sourceCell.getNeighbors(true)) {
            if (neighbor != null) {
                if (!neighbor.isWaterSource() && neighbor.getWaterValue() < currentValue - 0.025f) {
                    neighbor.setWaterValue(currentValue - 0.025f);
                    propagateWaterValue(neighbor, currentValue - 0.025f);
                }
            }
        }
    }

    public List<WorldCell> getOverlappingCells(BoundingBox boundingBox) {
        Vector3 minCorner = boundingBox.min;
        Vector3 maxCorner = boundingBox.max;

        // Get WorldCell indices for the bounding box corners
        int startX = (int) Math.floor((minCorner.x + 25180) / 100);
        int startY = (int) Math.floor((minCorner.y + 25180) / 100);
        int endX = (int) Math.floor((maxCorner.x + 25180) / 100);
        int endY = (int) Math.floor((maxCorner.y + 25180) / 100);

        List<WorldCell> overlappingCells = new ArrayList<>();

        // Iterate through the WorldCells within the bounding box indices
        for (int x = startX; x <= endX; x++) {
            for (int y = startY; y <= endY; y++) {
                WorldCell cell = this.get(x, y);
                if (cell != null && (cell.getBoundingBox().contains(boundingBox) || cell.getBoundingBox().intersects(boundingBox))) {
                    overlappingCells.add(cell);
                }
            }
        }

        return overlappingCells;
    }

    public WorldCell getNextTraversableCell(WorldCell currentCell, Vector3 direction, int maxSteps) {
        int currentX = currentCell.getX();
        int currentY = currentCell.getY();

        WorldCell nextCell = currentCell;
        for (int i = 1; i <= maxSteps; i++) {
            nextCell = nextCell.getNeighborFromFwdVector(direction);
            if (nextCell != null && nextCell.isCanTraverse() && canEntityReach(currentCell, nextCell)) {
                return nextCell;
            }
        }

        // If no traversable and reachable cell is found in the given direction within maxSteps, return null.
        return null;
    }

    public boolean canEntityReach(WorldCell start, WorldCell goal) {
        AStarSearch starSearch = new AStarSearch(start, goal, null, start.getRadiusCells(20), 500);
        return (starSearch.findPathTo() != null);
    }

    public ArrayList<WorldCell> getCellsInRectangle(WorldCell cell1, WorldCell cell2) {
        ArrayList<WorldCell> cells = new ArrayList<>();

        int xStart = Math.min(cell1.getX(), cell2.getX());
        int yStart = Math.min(cell1.getY(), cell2.getY());
        int xEnd = Math.max(cell1.getX(), cell2.getX());
        int yEnd = Math.max(cell1.getY(), cell2.getY());

        for (int x = xStart; x <= xEnd; x++) {
            for (int y = yStart; y <= yEnd; y++) {
                cells.add(this.get(x, y));
            }
        }

        return cells;
    }

    public void refreshOverlaps(BoundingBox boundingBox) {
        for (WorldCell c : this.getOverlappingCells(boundingBox)) {
            if (c.getBoundingBox().contains(boundingBox) || c.getBoundingBox().intersects(boundingBox)) {
                c.refresh();
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
