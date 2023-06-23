package com.gamefocal.rivenworld.game.ai.path;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.gamefocal.rivenworld.DedicatedServer;
import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.entites.generics.CollisionEntity;
import com.gamefocal.rivenworld.game.entites.generics.LightEmitter;
import com.gamefocal.rivenworld.game.util.Location;
import com.gamefocal.rivenworld.game.util.ShapeUtil;
import com.gamefocal.rivenworld.game.util.VectorUtil;
import com.gamefocal.rivenworld.game.util.WorldDirection;
import com.gamefocal.rivenworld.game.world.World;
import com.gamefocal.rivenworld.game.world.WorldMetaData;
import com.gamefocal.rivenworld.service.AiService;
import com.google.common.base.Objects;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class WorldCell {

    private World world;
    private WorldGrid grid;

    private int x;
    private int y;

    private boolean canTraverse = true;
    private float height = 0;
    private float lightValue = 0;
    private boolean isForest = false;

    public WorldCell(World world, WorldGrid grid, int x, int y) {
        this.world = world;
        this.grid = grid;
        this.x = x;
        this.y = y;

//        this.refresh();
    }

    public Location getGameLocation() {
        return new Location(Math.floor((this.x * 100) - 25180), Math.floor((this.y * 100) - 25180), 0);
    }

    public boolean canTravelFromCell(WorldCell from, AiPathValidator validator) {
        if (from != null) {
            float myHeight = DedicatedServer.instance.getWorld().getRawHeightmap().getHeightFromLocation(from.getCenterInGameSpace(true));
            float nHeight = DedicatedServer.instance.getWorld().getRawHeightmap().getHeightFromLocation(this.getCenterInGameSpace(true));

            float slope = Math.abs(nHeight - myHeight);

            // Leage slopes
            if (slope > 50) {
                return false;
            }
        }

        if (!this.isCanTraverse()) {
            return false;
        }

        // Below sea level
        if (this.getCenterInGameSpace(true).getZ() <= 3000) {
            return false;
        }

        // Check the validator
        if (validator != null && !validator.check(this)) {
            return false;
        }

        return true;
    }

    public Location getCenterInGameSpace(boolean atHeight) {
        Location location = this.getGameLocation();
        location.addX(50);
        location.addY(50);
        if (atHeight) {
            float height = this.world.getRawHeightmap().getHeightValue(this.x, this.y);
            location.setZ(height);
        }
        return location;
    }

    public Location getCenterInGameSpace(boolean atHeight, float addAddtionalHeight) {

        Location location = this.getCenterInGameSpace(atHeight);
        location.addZ(addAddtionalHeight);

        return location;
    }


    public BoundingBox getBoundingBox() {
        return ShapeUtil.makeBoundBox(this.getCenterInGameSpace(false).cpy().setZ(0).toVector(), 50, 90000);
    }

    public WorldCell getNeighborFromDirection(WorldDirection direction) {
        if (direction == WorldDirection.NORTH)
            return this.getNorth();
        if (direction == WorldDirection.NORTH_EAST)
            return this.getNorthEast();
        if (direction == WorldDirection.EAST)
            return this.getEast();
        if (direction == WorldDirection.SOUTH_EAST)
            return this.getSouthEast();
        if (direction == WorldDirection.SOUTH)
            return this.getSouth();
        if (direction == WorldDirection.SOUTH_WEST)
            return this.getSouthWest();
        if (direction == WorldDirection.WEST)
            return this.getWest();
        if (direction == WorldDirection.NORTH_WEST)
            return this.getNorthWest();

        return null;
    }

    public WorldCell getNeighborFromFwdVector(Vector3 fwd) {
        fwd.z = 0;
        double deg = VectorUtil.getDegrees(this.getCenterInGameSpace(false).toVector(), this.getCenterInGameSpace(false).cpy().toVector().mulAdd(fwd, 100)) + 180;
        WorldDirection direction = WorldDirection.getDirection(deg);
        return this.getNeighborFromDirection(direction);
    }

    public void refresh() {

        BoundingBox cellBox = this.getBoundingBox();

        Location realGameLoc = this.getCenterInGameSpace(false);
        try {
            this.height = DedicatedServer.instance.getWorld().getRawHeightmap().getHeightFromLocation(this.getCenterInGameSpace(false));
        } catch (Exception e) {
            this.height = 0;
        }

        float maxHeight = 0;
        for (GameEntity e : this.world.getCollisionManager().getNearbyEntities(realGameLoc)) {
            if (CollisionEntity.class.isAssignableFrom(e.getClass()) && cellBox.contains(e.getBoundingBox()) || cellBox.intersects(e.getBoundingBox())) {
                this.canTraverse = false;
                if (e.getBoundingBox().getHeight() > maxHeight) {
                    maxHeight = e.getBoundingBox().getHeight();
                }
            }
        }

        if (maxHeight > 0) {
            this.height += maxHeight;
        }

        // Is forest
        WorldMetaData worldMetaData = DedicatedServer.instance.getWorld().getRawHeightmap().getMetaDataFromXY(this.x, this.y);
        this.isForest = (worldMetaData.getForest() == 1);

        // Light
        this.lightValue = 0;
        for (GameEntity e : DedicatedServer.get(AiService.class).lightSources.values()) {
            if (LightEmitter.class.isAssignableFrom(e.getClass())) {
                LightEmitter lightEmitter = (LightEmitter) e;
                if (e.location.dist(this.getCenterInGameSpace(true)) <= lightEmitter.lightRadius()) {
                    this.lightValue = 1f;
                }
            }
        }
    }

    public float getLightValue() {
        return lightValue;
    }

    public boolean isForest() {
        return isForest;
    }

    public HashMap<String, WorldCell> getNeighborsWithDirections(boolean includeDiags) {
        HashMap<String, WorldCell> n = new HashMap<>();
        n.put("n", this.getNorth());
        if (includeDiags)
            n.put("ne", this.getNorthEast());
        n.put("e", this.getEast());
        if (includeDiags)
            n.put("se", this.getSouthEast());
        n.put("s", this.getSouth());
        if (includeDiags)
            n.put("sw", this.getSouthWest());
        n.put("w", this.getWest());
        if (includeDiags)
            n.put("nw", this.getNorthWest());
        return n;
    }

    public Collection<WorldCell> getNeighbors(boolean includeDiags) {
        ArrayList<WorldCell> n = new ArrayList<>();
        n.add(this.getNorth());
        if (includeDiags)
            n.add(this.getNorthEast());
        n.add(this.getEast());
        if (includeDiags)
            n.add(this.getSouthEast());
        n.add(this.getSouth());
        if (includeDiags)
            n.add(this.getSouthWest());
        n.add(this.getWest());
        if (includeDiags)
            n.add(this.getNorthWest());
        return n;
    }

    public WorldCell getNorth() {
        return this.grid.get(this.x, this.y + 1);
    }

    public WorldCell getNorthEast() {
        return this.grid.get(this.x + 1, this.y + 1);
    }

    public WorldCell getNorthWest() {
        return this.grid.get(this.x - 1, this.y + 1);
    }

    public WorldCell getSouth() {
        return this.grid.get(this.x, this.y - 1);
    }

    public WorldCell getSouthEast() {
        return this.grid.get(this.x + 1, this.y - 1);
    }

    public WorldCell getSouthWest() {
        return this.grid.get(this.x - 1, this.y - 1);
    }

    public WorldCell getEast() {
        return this.grid.get(this.x + 1, this.y);
    }

    public WorldCell getWest() {
        return this.grid.get(this.x - 1, this.y);
    }

    public World getWorld() {
        return world;
    }

    public void setWorld(World world) {
        this.world = world;
    }

    public WorldGrid getGrid() {
        return grid;
    }

    public void setGrid(WorldGrid grid) {
        this.grid = grid;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public boolean isCanTraverse() {
        return canTraverse;
    }

    public void setCanTraverse(boolean canTraverse) {
        this.canTraverse = canTraverse;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WorldCell cell = (WorldCell) o;
        return x == cell.x && y == cell.y;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(x, y);
    }

    public ArrayList<WorldCell> getRadiusCells(int radius) {
        ArrayList<WorldCell> cells = new ArrayList<>();

        // Iterate over the square that contains the circle of cells.
        for (int i = this.x - radius; i <= this.x + radius; i++) {
            for (int j = this.y - radius; j <= this.y + radius; j++) {

                // Exclude cells that fall outside of the circle by using the Pythagorean theorem.
                if (Math.sqrt(Math.pow(i - this.x, 2) + Math.pow(j - this.y, 2)) <= radius) {
                    WorldCell cell = this.grid.get(i, j);

                    // Check if the cell exists in the grid before adding it.
                    if (cell != null) {
                        cells.add(cell);
                    }
                }
            }
        }

        return cells;
    }

    public ArrayList<WorldCell> getBlockedRadiusCells(int radius) {
        ArrayList<WorldCell> cells = new ArrayList<>();

        // Iterate over the square that contains the circle of cells.
        for (int i = this.x - radius; i <= this.x + radius; i++) {
            for (int j = this.y - radius; j <= this.y + radius; j++) {

                // Exclude cells that fall outside of the circle by using the Pythagorean theorem.
                if (Math.sqrt(Math.pow(i - this.x, 2) + Math.pow(j - this.y, 2)) <= radius) {
                    WorldCell cell = this.grid.get(i, j);

                    // Check if the cell exists in the grid before adding it.
                    if (cell != null) {

                        if (!cell.isCanTraverse()) {
                            cells.add(cell);
                        }
                    }
                }
            }
        }

        return cells;
    }

}
