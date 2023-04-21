package com.gamefocal.rivenworld.game.weather;

public enum GameWeather {

    CLEAR,
    CLOUDY,
    FOGGY,
    OVERCAST,
    PARTLY_CLOUD,
    RAIN,
    RAIN_LIGHT,
    RAIN_THUNDERSTORM,
    DUST,
    DUST_STORM,
    SNOW,
    BLIZARD,
    SNOW_LIGHT;

    private int probability = 1;

    GameWeather() {
    }

    public float getProbability() {
        return probability;
    }

    public GameWeather setProbability(int probability) {
        this.probability = probability;
        return this;
    }
}
