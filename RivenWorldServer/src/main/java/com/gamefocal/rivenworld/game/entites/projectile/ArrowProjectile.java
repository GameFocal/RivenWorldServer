package com.gamefocal.rivenworld.game.entites.projectile;

import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.math.collision.Ray;
import com.gamefocal.rivenworld.DedicatedServer;
import com.gamefocal.rivenworld.entites.combat.CombatHitResult;
import com.gamefocal.rivenworld.entites.combat.hits.CombatEntityHitResult;
import com.gamefocal.rivenworld.entites.combat.hits.CombatPlayerHitResult;
import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.WorldChunk;
import com.gamefocal.rivenworld.game.entites.generics.CollisionEntity;
import com.gamefocal.rivenworld.game.entites.generics.TickEntity;
import com.gamefocal.rivenworld.game.util.Location;
import com.gamefocal.rivenworld.game.util.VectorUtil;
import com.gamefocal.rivenworld.models.GameEntityModel;
import com.gamefocal.rivenworld.service.PlayerService;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class ArrowProjectile extends FlyingProjectile<ArrowProjectile> {

    public ArrowProjectile(HiveNetConnection firedBy, float speed) {
        super(firedBy, speed);
        this.type = "NetArrow";
        this.damage = 10;
        this.despawnOnHit = false;
    }
}
