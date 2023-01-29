package com.gamefocal.island.service;

import com.gamefocal.island.DedicatedServer;
import com.gamefocal.island.entites.net.HiveNetConnection;
import com.gamefocal.island.entites.net.HiveNetMessage;
import com.gamefocal.island.entites.service.HiveService;
import com.gamefocal.island.game.tasks.HiveRepeatingTask;
import com.gamefocal.island.game.util.MathUtil;
import com.gamefocal.island.game.util.RandomUtil;
import com.gamefocal.island.game.weather.GameSeason;
import com.gamefocal.island.game.weather.GameWeather;
import com.gamefocal.island.models.GameMetaModel;
import com.google.auto.service.AutoService;

import javax.inject.Singleton;
import java.util.HashMap;

@AutoService(HiveService.class)
@Singleton
public class EnvironmentService implements HiveService<EnvironmentService> {

    private static final float dayMax = 2400f;
    private static final float secondsInDay = 15 * 60;
    private static final float daysForSeasons = 30;
    public float dayNumber = 0L;
    public Long lastTimeCalc = -1L;
    public float[] tempBounds = new float[]{32f, 99f};
    public float currentTemp = 32;
    private float gameTime = 0.00f;
    private GameWeather weather = GameWeather.CLEAR;
    private GameSeason season = GameSeason.SUMMER;
    private float seconds = 200L;
    private float tempStep = 0f;

    private float hummidity = 0f;

    private float nextWeatherEvent = 0L;

    @Override
    public void init() {
        // Load from the world file

        seconds = Float.parseFloat(GameMetaModel.getMetaValue("time", "0.0"));
        weather = GameWeather.valueOf(GameMetaModel.getMetaValue("weather", "CLEAR"));
        dayNumber = Float.parseFloat(GameMetaModel.getMetaValue("day", "0.0"));
        season = GameSeason.valueOf(GameMetaModel.getMetaValue("season", "SUMMER"));

        DedicatedServer.get(TaskService.class).registerTask(new HiveRepeatingTask("clock", 20L, 20L, false) {
            @Override
            public void run() {

                float diff = 1000;
                if (lastTimeCalc > 0) {
                    diff = System.currentTimeMillis() - lastTimeCalc;
                } else {
                    newDay();
                }

                seconds += (diff / 1000);

                if (seconds > secondsInDay) {
                    seconds = 0;
                    dayNumber++;

                    saveToWorld();
                    newDay();
                }

                float f = MathUtil.map(seconds, 0, secondsInDay, 0, 2400);

                gameTime = f;

                lastTimeCalc = System.currentTimeMillis();

                /*
                 * Temprature and weather stuff.
                 * */

                currentTemp += (tempStep * (diff / 1000));
                if (currentTemp > tempBounds[1]) {
                    tempStep = Math.abs(tempStep) * -1;
                } else if (currentTemp < tempBounds[0]) {
                    tempStep = Math.abs(tempStep);
                }

                if (seconds >= nextWeatherEvent) {
                    weather = weatherEvent();
                }

                for (HiveNetConnection c : DedicatedServer.get(PlayerService.class).players.values()) {
                    emitEnvironmentChange(c);
                }
            }
        });
    }

    public void setTime(float s) {
        seconds = s;
    }

    public void setDayPercent(float per) {
        float s = secondsInDay * per;
        seconds = s;
    }

    public void newDay() {
        // See if a new season should trigger
        if (this.dayNumber % daysForSeasons == 0) {
            // Is a season change event
            this.changeSeason();
        }

        float lowerTemp = RandomUtil.getRandomNumberBetween(this.season.getLowerTemp(), this.season.getUpperTemp());
        float upperTemp = RandomUtil.getRandomNumberBetween(lowerTemp, this.season.getUpperTemp());

        this.tempBounds = new float[]{lowerTemp, upperTemp};

        this.currentTemp = lowerTemp;

        float diff = upperTemp - lowerTemp;
        float scale = diff / secondsInDay;

        this.tempStep = scale;
        weather = weatherEvent();
    }

    public GameWeather weatherEvent() {
        hummidity = RandomUtil.getRandomNumberBetween(0, season.getRainChance());

        System.out.println("% Rain: " + hummidity + ", Current Season: " + season.name() + ", Temp: " + currentTemp);

        this.nextWeatherEvent += (secondsInDay / 6);
        if (this.nextWeatherEvent > secondsInDay) {
            this.nextWeatherEvent = 0;
        }

        System.out.println("Next Weather Event at " + nextWeatherEvent + "s");

        // See if it is going to rain.
        boolean rain = RandomUtil.getRandomChance(hummidity);
        if (rain) {
            if (currentTemp < 32) {
                // Snow
                if (weather == GameWeather.SNOW_LIGHT) {
                    return GameWeather.SNOW;
                } else if (weather == GameWeather.SNOW) {
                    return GameWeather.BLIZARD;
                } else {
                    return GameWeather.SNOW;
                }
            } else {
                if (weather == GameWeather.RAIN_LIGHT) {
                    return GameWeather.RAIN;
                } else if (weather == GameWeather.RAIN) {
                    return GameWeather.RAIN_THUNDERSTORM;
                } else {
                    return GameWeather.RAIN_LIGHT;
                }
            }
        } else {
            HashMap<GameWeather, Integer> randomWeather = new HashMap<>();
            randomWeather.put(GameWeather.CLEAR, 5);
            randomWeather.put(GameWeather.CLOUDY, 4);
            randomWeather.put(GameWeather.FOGGY, 2);
            randomWeather.put(GameWeather.PARTLY_CLOUD, 5);
            randomWeather.put(GameWeather.OVERCAST, 1);

            return RandomUtil.getRandomElementFromMap(randomWeather);
        }
    }

    public GameSeason nextSeason() {
        int i = 0;
        for (GameSeason s : GameSeason.values()) {
            if (s == season) {
                break;
            }
            i++;
        }

        if (i >= GameSeason.values().length) {
            i = 0;
        }

        return GameSeason.values()[i];
    }

    public void changeSeason() {
        season = this.nextSeason();
    }

    public void saveToWorld() {
        GameMetaModel.setMetaValue("time", String.valueOf(seconds));
        GameMetaModel.setMetaValue("day", String.valueOf(dayNumber));
        GameMetaModel.setMetaValue("weather", weather.name());
        GameMetaModel.setMetaValue("season", season.name());
    }

    public boolean showNorthernLights() {
        return season == GameSeason.SUMMER;
    }

    public void emitEnvironmentChange(HiveNetConnection connection) {
        this.emitEnvironmentChange(connection, false);
    }

    public void broadcastEnvChange(boolean syncTime) {
        for (HiveNetConnection connection : DedicatedServer.get(PlayerService.class).players.values()) {
            this.emitEnvironmentChange(connection, syncTime);
        }
    }

    public void emitEnvironmentChange(HiveNetConnection connection, boolean syncTime) {
        HiveNetMessage worldState = new HiveNetMessage();
        worldState.cmd = "env";
        worldState.args = new String[]{
                String.valueOf(dayNumber),
                String.valueOf(secondsInDay),
                String.valueOf(seconds),
                String.valueOf(gameTime),
                weather.name(),
                String.valueOf(currentTemp),
                (this.showNorthernLights() ? "1" : "0"),
                (syncTime) ? "1" : "0"
        };

        connection.sendTcp(worldState.toString());
    }
}
