package com.gamefocal.island.service;

import com.gamefocal.island.entites.service.HiveService;
import com.gamefocal.island.models.Player;
import com.google.auto.service.AutoService;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcPooledConnectionSource;
import com.j256.ormlite.table.TableUtils;

import javax.inject.Singleton;
import java.sql.SQLException;
import java.util.Hashtable;

@AutoService(HiveService.class)
@Singleton
public class DataService implements HiveService<DataService> {

    public static Dao<Player, String> players;
    private JdbcPooledConnectionSource source;

    @Override
    public void init() {
        /*
         * Load the ORM
         * */
        try {
            this.source = new JdbcPooledConnectionSource("jdbc:sqlite:world");

            // DAO
            players = DaoManager.createDao(this.source, Player.class);

            // Generate
            TableUtils.createTableIfNotExists(this.source, Player.class);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
