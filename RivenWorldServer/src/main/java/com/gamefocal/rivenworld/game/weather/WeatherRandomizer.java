package com.gamefocal.rivenworld.game.weather;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class WeatherRandomizer {
    private GameSeason currentSeason;
    private GameWeather currentWeather;

    public WeatherRandomizer(GameSeason initialSeason, GameWeather initialWeather) {
        this.currentSeason = initialSeason;
        this.currentWeather = initialWeather;
    }

    public GameSeason getCurrentSeason() {
        return currentSeason;
    }

    public void setCurrentSeason(GameSeason currentSeason) {
        this.currentSeason = currentSeason;
    }

    public GameWeather getCurrentWeather() {
        return currentWeather;
    }

    public void setCurrentWeather(GameWeather currentWeather) {
        this.currentWeather = currentWeather;
    }

    public void updateWeather() {
        List<GameWeather> availableWeathers = getAvailableWeathersForSeason(currentSeason);

        GameWeather nextWeather = getRandomElement(availableWeathers);
        while (!canTransition(currentWeather, nextWeather)) {
            nextWeather = getRandomElement(availableWeathers);
        }

        setCurrentWeather(nextWeather);
    }

    private List<GameWeather> getAvailableWeathersForSeason(GameSeason season) {
        switch (season) {
            case SUMMER:
                return Arrays.asList(GameWeather.CLEAR, GameWeather.CLOUDY, GameWeather.FOGGY,
                        GameWeather.OVERCAST, GameWeather.PARTLY_CLOUD, GameWeather.RAIN,
                        GameWeather.RAIN_LIGHT, GameWeather.RAIN_THUNDERSTORM, GameWeather.DUST,
                        GameWeather.DUST_STORM);
            case FALL:
                return Arrays.asList(GameWeather.CLEAR, GameWeather.CLOUDY, GameWeather.FOGGY,
                        GameWeather.OVERCAST, GameWeather.PARTLY_CLOUD, GameWeather.RAIN,
                        GameWeather.RAIN_LIGHT, GameWeather.RAIN_THUNDERSTORM);
            case WINTER:
                return Arrays.asList(GameWeather.CLEAR, GameWeather.CLOUDY, GameWeather.FOGGY,
                        GameWeather.OVERCAST, GameWeather.PARTLY_CLOUD, GameWeather.SNOW,
                        GameWeather.BLIZARD, GameWeather.SNOW_LIGHT);
            case SPRING:
                return Arrays.asList(GameWeather.CLEAR, GameWeather.CLOUDY, GameWeather.FOGGY,
                        GameWeather.OVERCAST, GameWeather.PARTLY_CLOUD, GameWeather.RAIN,
                        GameWeather.RAIN_LIGHT, GameWeather.RAIN_THUNDERSTORM);
            default:
                throw new IllegalArgumentException("Invalid season");
        }
    }

    private boolean canTransition(GameWeather currentWeather, GameWeather nextWeather) {
        if (currentWeather == nextWeather) {
            return false;
        }

        switch (nextWeather) {
            case RAIN:
                return currentWeather == GameWeather.RAIN_LIGHT || currentWeather == GameWeather.OVERCAST;
            case RAIN_LIGHT:
                return currentWeather == GameWeather.OVERCAST || currentWeather == GameWeather.PARTLY_CLOUD;
            case OVERCAST:
                return currentWeather == GameWeather.CLOUDY || currentWeather == GameWeather.FOGGY;
            default:
                return true;
        }
    }

    private <T> T getRandomElement(List<T> list) {
        Random random = new Random();
        return list.get(random.nextInt(list.size()));
    }
}
