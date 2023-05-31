package com.gamefocal.rivenworld.game.player;

import com.badlogic.gdx.graphics.Color;
import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.tasks.HiveTask;
import com.gamefocal.rivenworld.game.util.Location;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

public abstract class InterruptingTask extends HiveTask {

    private HiveNetConnection connection;
    private long startedAt = 0L;
    private long timespanInSeconds = 0L;
    private long completeAt = 0L;
    private String progressText = "Task Name";
    private Color progressColor = Color.GREEN;
    private Location initialLoc;
    private float initialHealth;

    public InterruptingTask(HiveNetConnection connection, String progressTitle, Color progressColor, long timespanInSeconds) {
        super(UUID.randomUUID().toString(), false);
        this.timespanInSeconds = timespanInSeconds;
        this.connection = connection;
        this.progressText = progressTitle;
        this.progressColor = progressColor;
        this.initialLoc = connection.getPlayer().location.cpy();
        this.initialHealth = connection.getPlayer().playerStats.health;
        this.nextRun = (System.currentTimeMillis() + 50);
    }

    public abstract void onSuccess();

    public abstract void onCanceled();

    @Override
    public void run() {
        // Do nothing
    }

    @Override
    public void tick() {
        super.tick();
        this.nextRun = (System.currentTimeMillis() + 20);

        if (this.completeAt == 0) {
            System.out.println("Task Started.");
            this.startedAt = System.currentTimeMillis();
            this.completeAt = (this.startedAt + TimeUnit.SECONDS.toMillis(this.timespanInSeconds));
        }

        // Update the progress bar
        float timeIn = (System.currentTimeMillis() - this.startedAt);
        float percent = (timeIn / (this.timespanInSeconds * 1000));

        System.out.println(timeIn + ", " + percent);

        this.connection.setProgressBar(this.progressText, percent, this.progressColor);

        // See if this should cancel
        if (this.initialLoc.dist(this.connection.getPlayer().location) >= 50) {
            System.out.println("Task closed due to dist.");
            this.onCanceled();
            this.cancel();
            return;
        }

        // See if we should trigger the runnable
        if (System.currentTimeMillis() > this.completeAt) {
            System.out.println("Task run.");
            this.onSuccess();
            this.cancel();
            return;
        }
    }
}
