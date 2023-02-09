package com.gamefocal.island.game;

import com.badlogic.gdx.math.Rectangle;
import com.gamefocal.island.entites.net.HiveNetConnection;
import com.gamefocal.island.game.util.Location;
import com.gamefocal.island.models.GameChunkModel;
import com.gamefocal.island.models.GameGuildModel;
import com.gamefocal.island.models.GameLandClaimModel;
import com.gamefocal.island.service.DataService;

import javax.xml.crypto.Data;
import java.sql.SQLException;

public class WorldChunk {

    World world;
    float x;
    float y;
    Location start;
    Rectangle box;
    Location center;

    public WorldChunk(World world, float x, float y) {
        this.world = world;
        this.x = x;
        this.y = y;

        float cellSize = 2400;

        float worldX = (this.x * cellSize);
        float worldY = (this.y * cellSize);

        Location realLoc = this.world.fromZeroBasedCords(new Location(worldX, worldY, 0));

        this.start = realLoc;
        this.center = start.cpy().addX((this.world.getChunkSize() * 100) / 2f).addY((this.world.getChunkSize() * 100) / 2f);
        this.box = new Rectangle(this.start.getX(), this.start.getY(), this.world.getChunkSize() * 100, this.world.getChunkSize() * 100);
    }

    public Location getChunkCords() {
        return new Location(this.x, this.y, 0);
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public Location getStart() {
        return start;
    }

    public Location getCenter() {
        return this.center;
    }

    public Rectangle getBox() {
        return box;
    }

    public WorldChunk north() {
        return this.world.getChunk(this.x, this.y + 1);
    }

    public WorldChunk east() {
        return this.world.getChunk(this.x + 1, this.y);
    }

    public WorldChunk south() {
        return this.world.getChunk(this.x, this.y - 1);
    }

    public WorldChunk west() {
        return this.world.getChunk(this.x - 1, this.y);
    }

    public WorldChunk[] neighbors() {
        return new WorldChunk[]{
                this.north(),
                this.east(),
                this.south(),
                this.west()
        };
    }

    public boolean canBuildInChunk(HiveNetConnection connection) {
        GameLandClaimModel landClaimModel = this.getClaim(connection);
        if (landClaimModel != null) {
            // Is Claimed

            // Is owner of this claim
            if (!landClaimModel.owner.uuid.equalsIgnoreCase(connection.getPlayer().uuid)) {
                // Is the owner
                return false;
            }

//            // Check if they are a member of the owning guild
//            GameGuildModel guildModel = landClaimModel.owner.guild;
//            if(guildModel != null && connection.getPlayer().guild != null) {
//                // Is in a guild
//                if(guildModel.id == connection.getPlayer().guild.id) {
//                    return true;
//                }
//            }

        }

        return true;
    }

    public GameLandClaimModel getClaim(HiveNetConnection connection) {
        try {
            GameChunkModel chunk = DataService.chunks.queryBuilder().where().eq("id", this.getChunkCords()).queryForFirst();
            if (chunk != null) {
                return chunk.claim;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return null;
    }

    public boolean isClaimed(HiveNetConnection connection) {
        return (this.getClaim(connection) != null);
    }

    @Override
    public String toString() {
        return this.getChunkCords().toString();
    }

    @Override
    public boolean equals(Object obj) {
        return this.getChunkCords().toString().equalsIgnoreCase(obj.toString());
    }
}
