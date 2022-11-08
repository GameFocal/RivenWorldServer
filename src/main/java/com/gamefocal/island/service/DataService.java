package com.gamefocal.island.service;

import com.gamefocal.island.entites.data.DataSource;
import com.gamefocal.island.entites.service.HiveService;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcPooledConnectionSource;
import com.j256.ormlite.table.TableUtils;
import org.reflections.Reflections;

import java.sql.SQLException;
import java.util.Hashtable;
import java.util.Set;

public class DataService implements HiveService<DataService> {

    private Hashtable<Class, Dao<Class, Class>> models = new Hashtable<>();

    private JdbcPooledConnectionSource source;

    @Override
    public void init() {
        /*
         * Load the ORM
         * */
        try {
            this.source = new JdbcPooledConnectionSource("jdbc:sqlite:world");

            // Load the commands
            Set<Class<?>> sources = new Reflections("com.gamefocal").getTypesAnnotatedWith(DataSource.class);
            for (Class c : sources) {
                DataSource s = (DataSource) c.getAnnotation(DataSource.class);
                if (s != null) {
                    models.put(c, DaoManager.createDao(this.source, c));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        for (Class c : this.models.keySet()) {
            try {
                TableUtils.createTableIfNotExists(this.source, c);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public <A> A get(Class<A> type, Object id) {
        if(this.models.containsKey(type)) {
            try {
                return (A) this.models.get(type).queryForId((Class) id);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return null;
    }
}
