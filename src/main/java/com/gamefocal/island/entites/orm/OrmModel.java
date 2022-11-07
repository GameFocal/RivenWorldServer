package com.gamefocal.island.entites.orm;

import com.gamefocal.island.DedicatedServer;
import com.google.gson.JsonElement;
import org.apache.cayenne.Cayenne;
import org.apache.cayenne.CayenneDataObject;
import org.apache.cayenne.query.ObjectSelect;
import org.joda.time.DateTime;

import java.util.Collections;
import java.util.List;

public abstract class OrmModel extends CayenneDataObject {

    protected Object pkIdOverride = null;

    public Object getPkId() {
        if (this.pkIdOverride != null) {
            return this.pkIdOverride;
        } else {
            return Cayenne.longPKForObject(this);
        }
    }

    public static <T extends OrmModel> T getFromId(Class<T> type, Object id) {
        return (T) Cayenne.objectForPK(DedicatedServer.getOrm().getDbContext(), type, id);
    }

    public <T extends OrmModel> T refresh(Class<T> type) {
        return OrmModel.getFromId(type, this.getPkId());
    }

    public void setPkIdOverride(Object pkIdOverride) {
        this.pkIdOverride = pkIdOverride;
    }

    public static <T extends OrmModel> T newObject(Class<T> type) {
        return (T) DedicatedServer.getOrm().getDbContext().newObject(type);
    }

    public abstract JsonElement toJson();

    public static <T> List<T> paginate(List<T> objects, int page, int perPage) {
        if (page < 0) {
            page = 0;
        }
        if (perPage == 0) {
            perPage = 10;
        }

        int offset = page * perPage;
        int max = offset + perPage;

        if (max > objects.size()) {
            max = (objects.size());
        }

        if (max < 0) {
            max = 0;
        }

        return objects.subList(offset, max);
    }

    public void save() {
        DedicatedServer.getOrm().getDbContext().commitChanges();
    }

    public static String timestamp() {
        return timestamp(new DateTime());
    }

    public static String timestamp(DateTime dt) {
        java.text.SimpleDateFormat sdf =
                new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        return sdf.format(dt);
    }

}
