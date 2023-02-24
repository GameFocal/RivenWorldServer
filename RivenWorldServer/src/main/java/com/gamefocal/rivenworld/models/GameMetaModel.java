package com.gamefocal.rivenworld.models;

import com.gamefocal.rivenworld.service.DataService;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.sql.SQLException;

@DatabaseTable
public class GameMetaModel {

    @DatabaseField(id = true)
    public String name;

    @DatabaseField
    public String value;

    public static String getMetaValue(String name, String def) {
        try {
            GameMetaModel model = DataService.gameMeta.queryForId(name);
            if (model != null) {
                return model.value;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return def;
    }

    public static boolean hasMeta(String name) {
        try {
            return DataService.gameMeta.idExists(name);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void setMetaValue(String name, String val) {
        GameMetaModel model = new GameMetaModel();
        model.name = name;
        model.value = val;

        try {
            DataService.gameMeta.createOrUpdate(model);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

}
