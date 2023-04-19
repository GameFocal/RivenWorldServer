package com.gamefocal.rivenworld.service;

import com.badlogic.gdx.math.MathUtils;
import com.gamefocal.rivenworld.DedicatedServer;
import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.entites.net.HiveNetMessage;
import com.gamefocal.rivenworld.entites.service.HiveService;
import com.gamefocal.rivenworld.events.player.PlayerEnvironmentEffectEvent;
import com.gamefocal.rivenworld.events.world.SundownEvent;
import com.gamefocal.rivenworld.events.world.SunriseEvent;
import com.gamefocal.rivenworld.game.entites.storage.DropBag;
import com.gamefocal.rivenworld.game.enviroment.player.PlayerDataState;
import com.gamefocal.rivenworld.game.enviroment.player.PlayerEnvironmentEffect;
import com.gamefocal.rivenworld.game.sounds.GameSounds;
import com.gamefocal.rivenworld.game.tasks.HiveRepeatingTask;
import com.gamefocal.rivenworld.game.util.MathUtil;
import com.gamefocal.rivenworld.game.util.RandomUtil;
import com.gamefocal.rivenworld.game.util.TickUtil;
import com.gamefocal.rivenworld.game.weather.GameSeason;
import com.gamefocal.rivenworld.game.weather.GameWeather;
import com.gamefocal.rivenworld.game.weather.WeatherRandomizer;
import com.gamefocal.rivenworld.models.GameMetaModel;
import com.google.auto.service.AutoService;
import org.joda.time.DateTime;

import javax.inject.Singleton;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

@AutoService(HiveService.class)
@Singleton
public class EnvironmentService implements HiveService<EnvironmentService> {

    public static final float sunsetPercent = .82f;
    public static final float sunrisePercent = .26f;
    private static final float dayMax = 2400f;
    private static final float daysForSeasons = 30;
    public static GameSounds[] daySongs = new GameSounds[]{GameSounds.BG1, GameSounds.BG2};
    public static GameSounds[] nightSongs = new GameSounds[]{GameSounds.Night};
    public static GameSounds currentWorldAmbient = GameSounds.BG2;
    private static float secondsInDay = 30 * 60;
    private static boolean freezeTime = false;
    private static boolean autoWeather = true;
    public float dayNumber = 0L;
    public Long lastTimeCalc = -1L;
    public float[] tempBounds = new float[]{32f, 99f};
    public float currentTemp = 32;
    public boolean isDay = false;
    private float gameTime = 0.00f;
    private GameWeather weather = GameWeather.CLEAR;
    private GameSeason season = GameSeason.SUMMER;
    private float seconds = 2400 * sunrisePercent;
    private float tempStep = 0f;
    private float hummidity = 0f;
    private float nextWeatherEvent = 0L;
    private long daySeconds = 0L;
    private long nightSeconds = 0L;
    private float tick = 0;
    private LinkedList<GameWeather> weatherSequence = new LinkedList<>();
    public HashMap<GameWeather, GameWeather[]> options = new HashMap<>();

    public static float getSecondsInDay() {
        return secondsInDay;
    }

    public static void setSecondsInDay(float secondsInDay) {
        EnvironmentService.secondsInDay = secondsInDay;
    }

    public static void resetSecondsInDay() {
        secondsInDay = 15 * 60;
    }

    public static boolean isFreezeTime() {
        return freezeTime;
    }

    public static void setFreezeTime(boolean freezeTime) {
        EnvironmentService.freezeTime = freezeTime;
    }

    public static boolean isAutoWeather() {
        return autoWeather;
    }

    public static void setAutoWeather(boolean autoWeather) {
        EnvironmentService.autoWeather = autoWeather;
    }

    @Override
    public void init() {
        // Load from the world file

        seconds = Float.parseFloat(GameMetaModel.getMetaValue("time", "0.0"));
        weather = GameWeather.valueOf(GameMetaModel.getMetaValue("weather", "CLEAR"));
        dayNumber = Float.parseFloat(GameMetaModel.getMetaValue("day", "0.0"));
        season = GameSeason.valueOf(GameMetaModel.getMetaValue("season", "SUMMER"));

        // Rain
        options.put(GameWeather.CLEAR, new GameWeather[]{GameWeather.FOGGY, GameWeather.PARTLY_CLOUD});
        options.put(GameWeather.PARTLY_CLOUD, new GameWeather[]{GameWeather.CLOUDY, GameWeather.CLEAR});
        options.put(GameWeather.CLOUDY, new GameWeather[]{GameWeather.OVERCAST, GameWeather.PARTLY_CLOUD});
        options.put(GameWeather.OVERCAST, new GameWeather[]{GameWeather.RAIN_LIGHT, GameWeather.CLOUDY, GameWeather.SNOW_LIGHT});
        options.put(GameWeather.RAIN_LIGHT, new GameWeather[]{GameWeather.RAIN, GameWeather.OVERCAST});
        options.put(GameWeather.RAIN, new GameWeather[]{GameWeather.RAIN_THUNDERSTORM, GameWeather.RAIN_LIGHT, GameWeather.OVERCAST});
        options.put(GameWeather.RAIN_THUNDERSTORM, new GameWeather[]{GameWeather.RAIN, GameWeather.RAIN_LIGHT, GameWeather.OVERCAST, GameWeather.CLOUDY, GameWeather.PARTLY_CLOUD});
        options.put(GameWeather.FOGGY, new GameWeather[]{GameWeather.RAIN_LIGHT, GameWeather.OVERCAST});

        // Snow
        options.put(GameWeather.SNOW_LIGHT, new GameWeather[]{GameWeather.SNOW, GameWeather.OVERCAST});
        options.put(GameWeather.SNOW, new GameWeather[]{GameWeather.BLIZARD, GameWeather.SNOW});

        // Snow

        // Clock
        DedicatedServer.get(TaskService.class).registerTask(new HiveRepeatingTask("clock", 20L, 20L, false) {
            @Override
            public void run() {

                if (freezeTime) {
                    lastTimeCalc = System.currentTimeMillis();
                    return;
                }

                float diff = 1000;
                if (lastTimeCalc > 0) {
                    diff = System.currentTimeMillis() - lastTimeCalc;
                } else {
                    newDay();
                }

                seconds += (diff / 1000);

                long totalSecondsInNight = (DedicatedServer.settings.minutesInNight * 60);
                long totalSecondsInDay = (DedicatedServer.settings.minutesInDay * 60);

                secondsInDay = totalSecondsInDay + totalSecondsInNight;

                long startOfNight = 0;
                long startOfDay = totalSecondsInNight / 2;
                long startOfNight2 = startOfDay + totalSecondsInDay;
                long totalSecondsInCycle = totalSecondsInDay + totalSecondsInNight;

                isDay = (seconds > startOfDay && seconds < startOfNight2);
                if (isDay) {
                    // Daylight add

                    /*
                     * Map Day
                     * */
                    tick = MathUtils.map(startOfDay, startOfNight2, sunrisePercent * 2400, sunsetPercent * 2400, seconds);
                } else {
                    // Night time add
                    if (seconds > startOfNight && seconds < startOfDay) {
                        // Night 1
                        tick = MathUtils.map(startOfNight, startOfDay, 0, 2400 * sunrisePercent, seconds);
                    } else if (seconds > startOfNight2) {
                        tick = MathUtils.map(startOfNight2, totalSecondsInCycle, 2400 * sunsetPercent, 2400, seconds);
                    }
                }

//                System.out.println(seconds + "/" + totalSecondsInCycle + ": " + (isDay ? "DAY" : "NIGHT") + ", TICK: " + tick);

                checkForDayNightChange();

                if (seconds > secondsInDay) {
                    seconds = 0;
                    dayNumber++;

                    saveToWorld();
                    newDay();
                }

//                float f = MathUtil.map(seconds, 0, secondsInDay, 0, 2400);

//                gameTime = f;

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
                    emitEnvironmentChange(c, true);
                }
            }
        });

        // Enviroment Sync
        TaskService.scheduleRepeatingTask(() -> {

            /*
             * Sync environment for each player
             * */
            for (HiveNetConnection connection : DedicatedServer.get(PlayerService.class).players.values()) {

                if (!connection.isVisible()) {
                    continue;
                }

                PlayerEnvironmentEffect effect = calcConsumptionsPerTick(connection);

                PlayerEnvironmentEffectEvent event = new PlayerEnvironmentEffectEvent(connection, effect);

                if (event.isCanceled()) {
                    continue;
                }

                if (!connection.getState().isDead) {
                    // Now apply the adjustments to each section
                    connection.getPlayer().playerStats.hunger -= (event.getEnvironmentEffect().hungerConsumptionPerTick);
                    connection.getPlayer().playerStats.thirst -= (event.getEnvironmentEffect().waterConsumptionPerTick);
                    connection.getPlayer().playerStats.health -= (event.getEnvironmentEffect().healthConsumptionPerTick);
                    connection.getPlayer().playerStats.energy -= (event.getEnvironmentEffect().energyConsumptionPerTick);

                    if (connection.getPlayer().playerStats.health > 100) {
                        connection.getPlayer().playerStats.health = 100f;
                    }
                    if (connection.getPlayer().playerStats.energy > 100) {
                        connection.getPlayer().playerStats.energy = 100f;
                    }
                    if (connection.getPlayer().playerStats.thirst > 100) {
                        connection.getPlayer().playerStats.energy = 100f;
                    }
                    if (connection.getPlayer().playerStats.thirst < 0) {
                        connection.getPlayer().playerStats.thirst = 0;
                    }
                    if (connection.getPlayer().playerStats.hunger < 0) {
                        connection.getPlayer().playerStats.hunger = 0;
                    }
                    if (connection.getPlayer().playerStats.energy < 0) {
                        connection.getPlayer().playerStats.energy = 0;
                    }

                    for (PlayerDataState e : event.getEnvironmentEffect().addStates) {
                        connection.addEffect(e);
                    }

                    for (PlayerDataState e : event.getEnvironmentEffect().removeStates) {
                        connection.removeEffect(e);
                    }
                }
            }
        }, 20L, 20L, false);

        // Drop Bag Cleanup
        TaskService.scheduleRepeatingTask(() -> {
            List<DropBag> models = DedicatedServer.instance.getWorld().getEntitesOfType(DropBag.class);
            for (DropBag bag : models) {
                DateTime now = DateTime.now();
                DateTime del = bag.getModel().createdAt.plusMinutes(30);

                if (now.isAfter(del)) {
                    // Can delete
                    DedicatedServer.instance.getWorld().despawn(bag.uuid);
                }
            }
        }, TickUtil.MINUTES(5), TickUtil.MINUTES(5), false);

    }

    public void checkForDayNightChange() {
        if (this.isDay()) {
            // Is Day
            if (!this.isDay) {
                this.isDay = true;
                this.worldSongChange();
                new SunriseEvent().call();
            }
        } else {
            // Is Night
            if (this.isDay) {
                this.isDay = false;
                this.worldSongChange();
                new SundownEvent().call();
            }
        }
    }

    public boolean isDay() {
        return isDay;
    }

    public void worldSongChange() {
        if (this.isDay) {
            currentWorldAmbient = RandomUtil.getRandomElementFromArray(daySongs);
        } else {
            currentWorldAmbient = RandomUtil.getRandomElementFromArray(nightSongs);
        }
    }

    public PlayerEnvironmentEffect calcConsumptionsPerTick(HiveNetConnection connection) {
        PlayerEnvironmentEffect e = new PlayerEnvironmentEffect();

        // Hunger
        e.hungerConsumptionPerTick = 0.01f;
        e.waterConsumptionPerTick = 0.02f;
        e.healthConsumptionPerTick = 0.0f;
        e.energyConsumptionPerTick = 0.0f;

        if (connection.getPlayer().playerStats.hunger >= 25f || connection.getPlayer().playerStats.thirst >= 25f) {
            e.healthConsumptionPerTick += -.05f;
        }

        if (connection.getSpeed() <= 100) {
            e.energyConsumptionPerTick += -5f;
        } else if (connection.getSpeed() <= 800) {
            e.energyConsumptionPerTick += -1f;
        } else if (connection.getSpeed() > 800) {
            e.energyConsumptionPerTick += 5f;
        }

//        System.out.println("SPEED: " + connection.getSpeed() + ", " + e.energyConsumptionPerTick);

        if (connection.getPlayer().playerStats.hunger <= 0 || connection.getPlayer().playerStats.thirst <= 0) {
            e.healthConsumptionPerTick = 1;
        } else if (connection.getPlayer().playerStats.hunger <= 10 || connection.getPlayer().playerStats.thirst <= 10) {
            e.healthConsumptionPerTick = Math.max(0, e.energyConsumptionPerTick);
            e.energyConsumptionPerTick = Math.max(0, e.healthConsumptionPerTick);
        }

//        if (connection.getSpeed()) {
//            e.energyConsumptionPerTick = .25f;
//        }

        return e;
    }

    public void setTime(float s) {
        seconds = s;
    }

    public float getDayPercent() {
        return seconds / secondsInDay;
    }

    public void setDayPercent(float per) {
        float s = secondsInDay * per;
        seconds = s;
    }

    public float getDaySecondsFromPercent(float per) {
        return (secondsInDay * per);
    }

    public GameWeather getWeather() {
        return weather;
    }

    public void setWeather(GameWeather weather) {
        this.weather = weather;
    }

    public GameSeason getSeason() {
        return season;
    }

    public void setSeason(GameSeason season) {
        this.season = season;
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

        if (autoWeather) {
            weather = weatherEvent();
        }
    }

    public GameWeather weatherEvent() {
        hummidity = RandomUtil.getRandomNumberBetween(0, season.getRainChance());

        System.out.println("% Rain: " + hummidity + ", Current Season: " + season.name() + ", Temp: " + currentTemp);

        this.nextWeatherEvent += (secondsInDay / 2);
        if (this.nextWeatherEvent > secondsInDay) {
            this.nextWeatherEvent = 0;
        }

        /*
         * Find weather changes
         * */

        return GameWeather.CLEAR;

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
                String.valueOf(2400),
                String.valueOf(tick),
                String.valueOf(gameTime),
                weather.name(),
                String.valueOf(currentTemp),
                (this.showNorthernLights() ? "1" : "0"),
                (syncTime) ? "1" : "0"
        };

        connection.sendTcp(worldState.toString());
    }

    public void buildWeatherSequence() {
        this.weatherSequence.clear();

        /*
         * Find the starting weather for the day
         * */

    }

    public void generateNextWeatherSequence() {
        GameWeather last = this.weatherSequence.getLast();
    }

    public void emitOverrideEnvironmentChange(HiveNetConnection connection, boolean syncTime, float dayPercent, GameWeather weather) {
        float seconds = this.getDaySecondsFromPercent(dayPercent);
        float f = MathUtil.map(seconds, 0, secondsInDay, 0, 2400);

        HiveNetMessage worldState = new HiveNetMessage();
        worldState.cmd = "env";
        worldState.args = new String[]{
                String.valueOf(1),
                String.valueOf(secondsInDay),
                String.valueOf(seconds),
                String.valueOf(f),
                weather.name(),
                String.valueOf(currentTemp),
                (this.showNorthernLights() ? "1" : "0"),
                (syncTime) ? "1" : "0"
        };

        connection.sendTcp(worldState.toString());
    }
}
