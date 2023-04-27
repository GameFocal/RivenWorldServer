package com.gamefocal.rivenworld.game.ai.path;

import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.World;
import com.gamefocal.rivenworld.game.util.Location;
import com.gamefocal.rivenworld.game.util.ShapeUtil;
import com.google.common.base.Objects;

import java.util.ArrayList;
import java.util.Collection;

public class WorldCell {

    private World world;
    private WorldGrid grid;

    private int x;
    private int y;

    private boolean canTraverse = true;
    private float height = 0;

    public WorldCell(World world, WorldGrid grid, int x, int y) {
        this.world = world;
        this.grid = grid;
        this.x = x;
        this.y = y;

//        this.refresh();
    }

    public Location getCenterInGameSpace(boolean atHeight) {
        Location location = new Location(((this.x * 100) - 25180), ((this.y * 100) - 25180), 0);
        if (atHeight) {
            this.world.getRawHeightmap().getHeightLocationFromLocation(location);
        }
        return location;
    }

    public BoundingBox getBoundingBox() {
        return ShapeUtil.makeBoundBox(this.getCenterInGameSpace(false).cpy().addX(50).addY(50).setZ(0).toVector(), 50, 90000);
    }

    public void refresh() {

        BoundingBox cellBox = this.getBoundingBox();

        Location realGameLoc = this.getCenterInGameSpace(false);
        this.height = this.world.getRawHeightmap().getHeightFromLocation(this.getCenterInGameSpace(false));

        float maxHeight = 0;
        for (GameEntity e : this.world.getCollisionManager().getNearbyEntities(realGameLoc)) {
            if (cellBox.contains(e.getBoundingBox()) || cellBox.intersects(e.getBoundingBox())) {
                this.canTraverse = false;

                System.out.println("CAN NOT TRAVERSE THIS CELL");

                if (e.getBoundingBox().getHeight() > maxHeight) {
                    maxHeight = e.getBoundingBox().getHeight();
                }
            }
        }

        if (maxHeight > 0) {
            this.height += maxHeight;
        }
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
}
