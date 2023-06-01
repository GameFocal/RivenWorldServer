package com.gamefocal.rivenworld.game.player;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.tasks.HiveTask;
import com.gamefocal.rivenworld.game.ui.UIIcon;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

public abstract class PlayerStateEffect extends HiveTask {

    private String name;
    private String desc;
    private UIIcon icon;
    protected HiveNetConnection player;
    private long started = 0;
    private long timeInSeconds = 0;
    private long endAt = 0;
    private long periodDelayInSeconds = 0;

    public PlayerStateEffect(String name, String desc, UIIcon icon, long timeInSeconds, long periodDelay) {
        super(UUID.randomUUID().toString(), false);
        this.name = name;
        this.desc = desc;
        this.icon = icon;
        this.timeInSeconds = timeInSeconds;
        this.periodDelayInSeconds = periodDelay;
    }

    public String getName() {
        return name;
    }

    public String getDesc() {
        return desc;
    }

    public UIIcon getIcon() {
        return icon;
    }

    public void attachToPlayer(HiveNetConnection connection) {
        this.player = connection;
        this.nextRun = System.currentTimeMillis() + 50;
    }

    public abstract void onStart();
    public abstract void onComplete();

    @Override
    public void tick() {
        super.tick();

        if (this.started == 0) {
            this.started = System.currentTimeMillis();
            this.endAt = (this.started + TimeUnit.SECONDS.toMillis(this.timeInSeconds));
            this.onStart();
        } else if (this.timeInSeconds > 0 && System.currentTimeMillis() > this.endAt) {
            this.cancel();
            this.onComplete();
        }

        this.run();
        this.nextRun = (System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(this.periodDelayInSeconds));
    }
}
