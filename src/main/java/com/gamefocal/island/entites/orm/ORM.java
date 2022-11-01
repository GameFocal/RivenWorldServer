package com.gamefocal.island.entites.orm;

import org.apache.cayenne.ObjectContext;
import org.apache.cayenne.configuration.server.ServerRuntime;
import org.apache.cayenne.log.JdbcEventLogger;
import org.apache.cayenne.log.NoopJdbcEventLogger;
import org.sqlite.SQLiteDataSource;

public class ORM {

    private ServerRuntime DBRunTime;
    private ObjectContext dbContext;
    private SQLiteDataSource dataSource;

    public ORM(String worldName) {
        SQLiteDataSource mysql = new SQLiteDataSource();
        mysql.setDatabaseName(worldName);
//        mysql.setServerName(Property.get("dbHostName"));
//        mysql.setAutoReconnect(true);
//        mysql.setTcpKeepAlive(true);

        this.dataSource = mysql;

        ServerRuntime runtime = ServerRuntime.builder()
//                .jdbcDriver("com.mysql.jdbc.jdbc2.optional.MysqlDataSource")
                .url(mysql.getUrl())
                .minConnections(250)
                .maxConnections(500)
                .dataSource(mysql)
                .addConfig("cayenne-rivenworlds_dedicated.xml")
                .addModule(binder -> binder.bind(JdbcEventLogger.class).to(NoopJdbcEventLogger.class))
                .build();

        this.dbContext = runtime.newContext();
        this.DBRunTime = runtime;
    }

    public ServerRuntime getDBRunTime() {
        return DBRunTime;
    }

    public ObjectContext getDbContext() {
        return dbContext;
    }

    public SQLiteDataSource getDataSource() {
        return dataSource;
    }
}
