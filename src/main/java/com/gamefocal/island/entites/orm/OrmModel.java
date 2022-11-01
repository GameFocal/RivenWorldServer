package com.gamefocal.island.entites.orm;

import com.google.gson.JsonElement;
import org.apache.cayenne.Cayenne;
import org.apache.cayenne.CayenneDataObject;
import org.apache.cayenne.query.ObjectSelect;

import java.util.Collections;
import java.util.List;

public abstract class OrmModel extends CayenneDataObject {

    protected Long pkIdOverride = null;

    public long getPkId() {
        if (this.pkIdOverride != null) {
            return this.pkIdOverride;
        } else {
            return Cayenne.longPKForObject(this);
        }
    }

    public <T extends OrmModel> T getFromId(long id, ORM orm) {
        return getFromId((int) id, orm);
    }

    public <T extends OrmModel> T getFromId(int id, ORM orm) {
        return (T) Cayenne.objectForPK(orm.getDbContext(), getClass(), id);
    }

    public <T extends OrmModel> T refresh(ORM orm) {
        return this.getFromId(this.getPkId(), orm);
    }

    public void setPkIdOverride(Long pkIdOverride) {
        this.pkIdOverride = pkIdOverride;
    }

    public <T extends OrmModel> T newObject(ORM orm) {
        return (T) orm.getDbContext().newObject(getClass());
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

    public void save(ORM orm) {
        orm.getDbContext().commitChanges();
    }

    public Long nextId(ORM orm) {
        List<? extends OrmModel> models = ObjectSelect.query(getClass()).select(orm.getDbContext());
        Collections.reverse(models);
        OrmModel model = models.get(0);
        if (model != null) {
            return model.getPkId() + 1;
        }

        return null;
    }

}
