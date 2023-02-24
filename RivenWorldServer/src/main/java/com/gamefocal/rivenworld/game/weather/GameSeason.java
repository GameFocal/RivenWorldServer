package com.gamefocal.rivenworld.game.weather;

public enum GameSeason {

    SUMMER(102, 85, .35f),
    FALL(91, 45, .45f),
    WINTER(62, 28, .25f),
    SPRING(85, 65, .75f);

    private float upperTemp;

    private float lowerTemp;

    private float rainChance;

    GameSeason(float upperTemp, float lowerTemp, float rainChance) {
        this.upperTemp = upperTemp;
        this.lowerTemp = lowerTemp;
        this.rainChance = rainChance;
    }

    public float getUpperTemp() {
        return upperTemp;
    }

    public float getLowerTemp() {
        return lowerTemp;
    }

    public float getRainChance() {
        return rainChance;
    }
}
