package com.gamefocal.rivenworld.service;

import com.gamefocal.rivenworld.DedicatedServer;
import com.gamefocal.rivenworld.entites.exception.RWThreadFactory;
import com.gamefocal.rivenworld.entites.service.HiveService;
import com.gamefocal.rivenworld.models.*;
import com.google.auto.service.AutoService;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.table.TableUtils;

import javax.inject.Singleton;
import java.sql.SQLException;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@AutoService(HiveService.class)
@Singleton
public class DataService implements HiveService<DataService> {

    public static Dao<PlayerModel, String> players;
    public static Dao<GameEntityModel, Long> gameEntities;
    public static Dao<GameMetaModel, String> gameMeta;
    public static Dao<GameFoliageModel, String> gameFoliage;
    public static Dao<GameResourceNode, String> resourceNodes;
    public static Dao<GameGuildMemberModel, String> guildMembers;
    public static Dao<GameGuildModel, String> guilds;
    public static Dao<GameLandClaimModel, String> landClaims;
    public static Dao<GameChunkModel, String> chunks;
    public static Dao<GamePlayerSkillsModel, String> playerSkills;
    public static Dao<GameShopModel, String> shopItems;
    public static Dao<GameNpcModel, String> npcModels;
    public static Dao<PlayerBedModel, UUID> playerBedModels;
    public static Dao<GameChunkVersionModel, String> chunkVersions;

    private JdbcConnectionSource source;

    private ExecutorService dbExecutor;

    @Override
    public void init() {

        this.dbExecutor = Executors.newFixedThreadPool(1, new RWThreadFactory());

        /*
         * Load the ORM
         * */
        try {
            this.source = new JdbcConnectionSource("jdbc:sqlite:world");

            // DAO
            players = DaoManager.createDao(this.source, PlayerModel.class);
            gameEntities = DaoManager.createDao(this.source, GameEntityModel.class);
            gameMeta = DaoManager.createDao(this.source, GameMetaModel.class);
            gameFoliage = DaoManager.createDao(this.source, GameFoliageModel.class);
            resourceNodes = DaoManager.createDao(this.source, GameResourceNode.class);
            guildMembers = DaoManager.createDao(this.source, GameGuildMemberModel.class);
            guilds = DaoManager.createDao(this.source, GameGuildModel.class);
            landClaims = DaoManager.createDao(this.source, GameLandClaimModel.class);
            chunks = DaoManager.createDao(this.source, GameChunkModel.class);
            playerSkills = DaoManager.createDao(this.source, GamePlayerSkillsModel.class);
            shopItems = DaoManager.createDao(this.source, GameShopModel.class);
            npcModels = DaoManager.createDao(this.source, GameNpcModel.class);
            playerBedModels = DaoManager.createDao(this.source, PlayerBedModel.class);
            chunkVersions = DaoManager.createDao(this.source, GameChunkVersionModel.class);

            // Generate
            TableUtils.createTableIfNotExists(this.source, PlayerModel.class);
            TableUtils.createTableIfNotExists(this.source, GameEntityModel.class);
            TableUtils.createTableIfNotExists(this.source, GameMetaModel.class);
            TableUtils.createTableIfNotExists(this.source, GameFoliageModel.class);
            TableUtils.createTableIfNotExists(this.source, GameResourceNode.class);
            TableUtils.createTableIfNotExists(this.source, GameGuildMemberModel.class);
            TableUtils.createTableIfNotExists(this.source, GameGuildModel.class);
            TableUtils.createTableIfNotExists(this.source, GameLandClaimModel.class);
            TableUtils.createTableIfNotExists(this.source, GameChunkModel.class);
            TableUtils.createTableIfNotExists(this.source, GamePlayerSkillsModel.class);
            TableUtils.createTableIfNotExists(this.source, GameShopModel.class);
            TableUtils.createTableIfNotExists(this.source, GameNpcModel.class);
            TableUtils.createTableIfNotExists(this.source, PlayerBedModel.class);
            TableUtils.createTableIfNotExists(this.source, GameChunkVersionModel.class);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public JdbcConnectionSource getSource() {
        return source;
    }

    public static void exec(Runnable task) {
        DedicatedServer.get(DataService.class).dbExecutor.submit(task);
    }
}
