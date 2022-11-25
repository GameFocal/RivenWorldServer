package com.gamefocal.island.service;

import com.gamefocal.island.DedicatedServer;
import com.gamefocal.island.entites.net.HiveNetConnection;
import com.gamefocal.island.entites.net.HiveNetMessage;
import com.gamefocal.island.entites.service.HiveService;
import com.gamefocal.island.game.tasks.HiveRepeatingTask;
import com.gamefocal.island.game.weather.GameWeather;
import com.gamefocal.island.models.GameMetaModel;
import com.google.auto.service.AutoService;

import javax.inject.Singleton;
import java.sql.SQLException;

@AutoService(HiveService.class)
@Singleton
public class EnvironmentService implements HiveService<EnvironmentService> {

    private Float gameTime = 0.00f;

    private Long dayMax = 2400L;

    private GameWeather weather = GameWeather.CLEAR;

    private Long lastTick = 0L;

    private Long tps = 20L;

    private Long ticksPerDay = 1800L;

    private Long elapsedTicks = 0L;

    @Override
    public void init() {
        // Load from the world file
        try {
            GameMetaModel savedTime = DataService.gameMeta.queryForId("time");
            GameMetaModel savedWeather = DataService.gameMeta.queryForId("weather");

            if (savedTime != null) {
                gameTime = Float.parseFloat(savedTime.value);
            }
            if (savedWeather != null) {
                weather = GameWeather.valueOf(savedWeather.value);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        DedicatedServer.get(TaskService.class).registerTask(new HiveRepeatingTask("clock", 20L, 20L, false) {
            @Override
            public void run() {
                elapsedTicks++;

//                gameTime += diff;
                if (elapsedTicks > ticksPerDay) {
                    elapsedTicks = 0L;
                }

                gameTime = ((float) elapsedTicks / (float) ticksPerDay) * (float) dayMax;

                GameMetaModel model = new GameMetaModel();
                model.name = "time";
                model.value = String.valueOf(gameTime);

                try {
                    DataService.gameMeta.createOrUpdate(model);
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }

//                emitEnvironmentChange();
            }
        });
    }

    public void emitEnvironmentChange(HiveNetConnection connection) {
        HiveNetMessage worldState = new HiveNetMessage();
        worldState.cmd = "env";
        worldState.args = new String[]{
                String.valueOf(tps),
                String.valueOf(ticksPerDay),
                String.valueOf(gameTime),
                weather.name()
        };

        connection.sendUdp(worldState.toString());
    }
}
