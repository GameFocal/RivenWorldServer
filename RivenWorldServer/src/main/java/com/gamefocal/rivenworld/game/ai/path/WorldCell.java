package com.gamefocal.rivenworld.game.ai.path;

import com.badlogic.gdx.math.collision.BoundingBox;
import com.gamefocal.rivenworld.DedicatedServer;
import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.entites.generics.LightEmitter;
import com.gamefocal.rivenworld.game.world.World;
import com.gamefocal.rivenworld.game.entites.generics.CollisionEntity;
import com.gamefocal.rivenworld.game.util.Location;
import com.gamefocal.rivenworld.game.util.ShapeUtil;
import com.gamefocal.rivenworld.game.world.WorldMetaData;
import com.gamefocal.rivenworld.service.AiService;
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
        return new Location(((this.x * 100) - 25180), ((this.y * 100) - 25180), 0);
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
