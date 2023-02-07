package com.gamefocal.island.service;

import com.gamefocal.island.entites.service.HiveService;
import com.gamefocal.island.models.*;
import com.google.auto.service.AutoService;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcPooledConnectionSource;
import com.j256.ormlite.table.TableUtils;

import javax.inject.Singleton;
import java.sql.SQLException;

@AutoService(HiveService.class)
@Singleton
public class DataService implements HiveService<DataService> {

    public static Dao<PlayerModel, String> players;
    public static Dao<GameEntityModel,Long> gameEntities;
    public static Dao<GameMetaModel,String> gameMeta;
    public static Dao<GameFoliageModel,String> gameFoliage;
    public static Dao<GameResourceNode,String> resourceNodes;
    public static Dao<GameGuildMemberModel,String> guildMembers;
    public static Dao<GameGuildModel,String> guilds;
    public static Dao<GameLandClaimModel,String> landClaims;

    private JdbcPooledConnectionSource source;

    @Override
    public void init() {
        /*
         * Load the ORM
         * */
        try {
            this.source = new JdbcPooledConnectionSource("jdbc:sqlite:world");

            // DAO
            players = DaoManager.createDao(this.source, PlayerModel.class);
            gameEntities = DaoManager.createDao(this.source, GameEntityModel.class);
            gameMeta = DaoManager.createDao(this.source, GameMetaModel.class);
            gameFoliage = DaoManager.createDao(this.source, GameFoliageModel.class);
            resourceNodes = DaoManager.createDao(this.source, GameResourceNode.class);
            guildMembers = DaoManager.createDao(this.source, GameGuildMemberModel.class);
            guilds = DaoManager.createDao(this.source, GameGuildModel.class);
            landClaims = DaoManager.createDao(this.source, GameLandClaimModel.class);

            // Generate
            TableUtils.createTableIfNotExists(this.source, PlayerModel.class);
            TableUtils.createTableIfNotExists(this.source, GameEntityModel.class);
            TableUtils.createTableIfNotExists(this.source, GameMetaModel.class);
            TableUtils.createTableIfNotExists(this.source, GameFoliageModel.class);
            TableUtils.createTableIfNotExists(this.source, GameResourceNode.class);
            TableUtils.createTableIfNotExists(this.source, GameGuildMemberModel.class);
            TableUtils.createTableIfNotExists(this.source, GameGuildModel.class);
            TableUtils.createTableIfNotExists(this.source, GameLandClaimModel.class);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
