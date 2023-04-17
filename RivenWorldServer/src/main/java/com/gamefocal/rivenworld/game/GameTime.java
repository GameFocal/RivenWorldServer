package com.gamefocal.rivenworld.game;

public class GameTime {

    private static final int MINUTES_IN_DAY = 24 * 60;
    private static final int DAY_DURATION = 45;
    private static final int NIGHT_DURATION = 15;
    private static final int CYCLE_DURATION = DAY_DURATION + NIGHT_DURATION;
    private static final int MINUTES_IN_GAME_DAY = 24 * CYCLE_DURATION;
    private static final long MILLISECONDS_PER_MINUTE = 60 * 1000;
    private static final int SUNRISE = 6 * 60 + 24;
    private static final int SUNSET = 18 * 60 + 48;

    private long gameTimeInMilliseconds;

    public GameTime() {
        gameTimeInMilliseconds = getGameTimeInMilliseconds(SUNRISE);
    }

    public void advanceTime(int minutes) {
        int gameTimeInMinutes = (int) (gameTimeInMilliseconds / MILLISECONDS_PER_MINUTE);
        int elapsedCycles = gameTimeInMinutes / CYCLE_DURATION;
        int cyclePosition = gameTimeInMinutes % CYCLE_DURATION;

        int minutesToAdd = 0;
        if (cyclePosition < DAY_DURATION) {
            minutesToAdd = Math.min(DAY_DURATION - cyclePosition, minutes);
        } else {
            minutesToAdd = Math.min(NIGHT_DURATION - (cyclePosition - DAY_DURATION), minutes);
        }

        gameTimeInMilliseconds += minutesToAdd * MILLISECONDS_PER_MINUTE;
        gameTimeInMilliseconds %= MINUTES_IN_GAME_DAY * MILLISECONDS_PER_MINUTE;
    }

    public void advanceTimeMillis(long millis) {
        int gameTimeInMinutes = (int) (gameTimeInMilliseconds / MILLISECONDS_PER_MINUTE);
        int elapsedCycles = gameTimeInMinutes / CYCLE_DURATION;
        int cyclePosition = gameTimeInMinutes % CYCLE_DURATION;

        long millisToAdd = 0;
        if (cyclePosition < DAY_DURATION) {
            millisToAdd = Math.min(DAY_DURATION - cyclePosition, millis);
        } else {
            millisToAdd = Math.min(NIGHT_DURATION - (cyclePosition - DAY_DURATION), millis);
        }

        gameTimeInMilliseconds += millisToAdd;
        gameTimeInMilliseconds %= MINUTES_IN_GAME_DAY * MILLISECONDS_PER_MINUTE;
    }

    public void setTimePercentage(float percentage) {
        if (percentage < 0.0f || percentage > 1.0f) {
            throw new IllegalArgumentException("Percentage must be between 0.0 and 1.0.");
        }
        int gameTimeInMinutes = (int) (MINUTES_IN_GAME_DAY * percentage);
        gameTimeInMilliseconds = getGameTimeInMilliseconds(gameTimeInMinutes);
    }

    private long getGameTimeInMilliseconds(int gameTimeInMinutes) {
        int elapsedCycles = gameTimeInMinutes / CYCLE_DURATION;
        int cyclePosition = gameTimeInMinutes % CYCLE_DURATION;

        long gameTimeInMillis = 0;

        gameTimeInMillis += elapsedCycles * (DAY_DURATION + NIGHT_DURATION) * MILLISECONDS_PER_MINUTE;
        gameTimeInMillis += Math.min(cyclePosition, DAY_DURATION) * MILLISECONDS_PER_MINUTE;
        gameTimeInMillis += Math.max(0, cyclePosition - DAY_DURATION) * MILLISECONDS_PER_MINUTE * ((float) CYCLE_DURATION / NIGHT_DURATION);

        return gameTimeInMillis;
    }

    public int getTime() {
        int gameTimeInMinutes = 0;
        long remainingMillis = gameTimeInMilliseconds;

        while (remainingMillis > 0) {
            if (remainingMillis >= CYCLE_DURATION * MILLISECONDS_PER_MINUTE) {
                gameTimeInMinutes += CYCLE_DURATION;
                remainingMillis -= CYCLE_DURATION * MILLISECONDS_PER_MINUTE;
            } else if (remainingMillis >= DAY_DURATION * MILLISECONDS_PER_MINUTE) {
                gameTimeInMinutes += DAY_DURATION;
                remainingMillis -= DAY_DURATION * MILLISECONDS_PER_MINUTE;
                gameTimeInMinutes += (int) (remainingMillis / MILLISECONDS_PER_MINUTE * ((float) NIGHT_DURATION / CYCLE_DURATION));
                break;
            } else {
                gameTimeInMinutes += (int) (remainingMillis / MILLISECONDS_PER_MINUTE);
                break;
            }
        }

        int hours = gameTimeInMinutes / 60;
        int minutes = gameTimeInMinutes % 60;

        int gameTimeIn24HourFormat = hours * 100 + minutes;

        return gameTimeIn24HourFormat;
    }
}