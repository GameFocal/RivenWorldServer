package com.gamefocal.rivenworld.game.weather;

import java.util.Random;

public class WeatherRandomizer {
    private final Random random = new Random();
    // Probability matrix for transitioning from one weather event to another
    private final float[][] probabilityMatrix = {
            {0.35f, 0.15f, 0.05f, 0.05f, 0.15f, 0.10f, 0.05f, 0.02f, 0.02f, 0.01f, 0.03f, 0.02f, 0.00f}, // CLEAR
            {0.15f, 0.30f, 0.10f, 0.10f, 0.20f, 0.10f, 0.05f, 0.02f, 0.01f, 0.01f, 0.02f, 0.02f, 0.02f}, // CLOUDY
            {0.05f, 0.10f, 0.30f, 0.10f, 0.10f, 0.10f, 0.10f, 0.05f, 0.02f, 0.02f, 0.05f, 0.01f, 0.00f}, // FOGGY
            {0.05f, 0.10f, 0.10f, 0.25f, 0.10f, 0.10f, 0.10f, 0.05f, 0.02f, 0.02f, 0.05f, 0.01f, 0.00f}, // OVERCAST
            {0.15f, 0.20f, 0.10f, 0.10f, 0.30f, 0.05f, 0.05f, 0.02f, 0.01f, 0.01f, 0.02f, 0.02f, 0.02f}, // PARTLY_CLOUD
            {0.10f, 0.15f, 0.10f, 0.10f, 0.05f, 0.25f, 0.20f, 0.05f, 0.02f, 0.01f, 0.02f, 0.02f, 0.01f}, // RAIN
            {0.05f, 0.10f, 0.10f, 0.10f, 0.05f, 0.20f, 0.30f, 0.05f, 0.02f, 0.01f, 0.02f, 0.01f, 0.02f}, // RAIN_LIGHT
            {0.02f, 0.05f, 0.05f, 0.05f, 0.02f, 0.10f, 0.20f, 0.30f, 0.02f, 0.02f, 0.10f, 0.04f, 0.03f}, // RAIN_THUNDERSTORM
            {0.01f, 0.02f, 0.02f, 0.02f, 0.01f, 0.01f, 0.02f, 0.02f, 0.50f, 0.25f, 0.05f, 0.01f, 0.01f}, // DUST
            {0.01f, 0.02f, 0.02f, 0.02f, 0.01f, 0.01f, 0.02f, 0.02f, 0.25f, 0.50f, 0.10f, 0.02f, 0.01f}, // DUST_STORM
            {0.03f, 0.05f, 0.05f, 0.05f, 0.02f, 0.05f, 0.10f, 0.10f, 0.05f, 0.02f, 0.30f, 0.05f, 0.03f}, // SNOW
            {0.02f, 0.05f, 0.01f, 0.01f, 0.02f, 0.05f, 0.01f, 0.04f, 0.01f, 0.01f, 0.05f, 0.60f, 0.10f}, // BLIZARD
            {0.00f, 0.02f, 0.00f, 0.00f, 0.02f, 0.01f, 0.01f, 0.03f, 0.01f, 0.01f, 0.03f, 0.10f, 0.76f} // SNOW_LIGHT
    };
    private GameWeather currentWeatherEvent;
    private GameSeason currentSeason;
    private int currentTemp;

    public WeatherRandomizer() {
        this.currentWeatherEvent = GameWeather.CLEAR;
        this.currentSeason = GameSeason.SUMMER;
        this.currentTemp = (int) currentSeason.getUpperTemp();
    }

    public void nextDay() {
        // randomly choose a weather event based on the probability matrix
        GameWeather newWeatherEvent = chooseWeatherEvent();

        // calculate the new temperature
        int newTemp = calculateTemperature();

        // smoothly transition to the new weather and temperature
        smoothlyTransition(newWeatherEvent, newTemp);
    }

    private GameWeather chooseWeatherEvent() {
        float rand = random.nextFloat();
        float cumulativeProb = 0;

        int currentWeatherIndex = currentWeatherEvent.ordinal();
        float[] probabilities = probabilityMatrix[currentWeatherIndex];
        for (int i = 0; i < probabilities.length; i++) {
            cumulativeProb += probabilities[i];
            if (rand <= cumulativeProb) {
                return GameWeather.values()[i];
            }
        }

        // should never happen
        return GameWeather.CLEAR;
    }

    private int calculateTemperature() {
        int maxTemp = currentSeason == GameSeason.SUMMER ? 102 :
                currentSeason == GameSeason.FALL ? 91 :
                        currentSeason == GameSeason.WINTER ? 62 : 85;
        int minTemp = currentSeason == GameSeason.SUMMER ? 85 :
                currentSeason == GameSeason.FALL ? 45 :
                        currentSeason == GameSeason.WINTER ? 28 : 65;
        // calculate a random temperature within the season's temperature range
        int range = maxTemp - minTemp;
        int temp = random.nextInt(range + 1) + minTemp;

        // gradually change the temperature towards the season's temperature range
        float temperatureChangeFactor = 0.05f;
        if (temp < currentTemp) {
            currentTemp = (int) (currentTemp * (1 - temperatureChangeFactor) + temp * temperatureChangeFactor);
        } else if (temp > currentTemp) {
            currentTemp = (int) (currentTemp * (1 - temperatureChangeFactor) + temp * temperatureChangeFactor);
        }

        return currentTemp;
    }

    private void smoothlyTransition(GameWeather newWeatherEvent, int newTemp) {
        // gradually change the weather and temperature towards the new values
        float transitionFactor = 0.05f;
        if (newWeatherEvent != currentWeatherEvent) {
            currentWeatherEvent = newWeatherEvent;
        }

        if (newTemp < currentTemp) {
            currentTemp = (int) (currentTemp * (1 - transitionFactor) + newTemp * transitionFactor);
        } else if (newTemp > currentTemp) {
            currentTemp = (int) (currentTemp * (1 - transitionFactor) + newTemp * transitionFactor);
        }
    }

    public GameWeather getCurrentWeatherEvent() {
        return currentWeatherEvent;
    }

    public GameSeason getCurrentSeason() {
        return currentSeason;
    }

    public int getCurrentTemp() {
        return currentTemp;
    }

}




