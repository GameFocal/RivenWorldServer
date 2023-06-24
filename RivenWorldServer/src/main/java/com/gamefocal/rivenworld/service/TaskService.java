package com.gamefocal.rivenworld.service;

import com.badlogic.gdx.graphics.Color;
import com.gamefocal.rivenworld.DedicatedServer;
import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.entites.service.HiveService;
import com.gamefocal.rivenworld.game.player.InterruptingTask;
import com.gamefocal.rivenworld.game.tasks.*;
import com.gamefocal.rivenworld.game.tasks.seqence.SequenceAction;
import com.google.auto.service.AutoService;

import javax.inject.Singleton;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@AutoService(HiveService.class)
@Singleton
public class TaskService implements HiveService<TaskService> {

    private ConcurrentHashMap<UUID, HiveTask> tasks = new ConcurrentHashMap<>();

    private Long nextTaskTrigger = 0L;

    private Long tps = 20L;

    private ExecutorService asyncPool;

    public static HiveTask schedulePlayerInterruptTask(Runnable runnable, Long timeInSeconds, String progressTitle, Color progressColor, HiveNetConnection player) {
        if (player.getPlayerInteruptTask() != null) {
            player.getPlayerInteruptTask().cancel();
            player.setPlayerInteruptTask(null);
        }

        InterruptingTask task = new InterruptingTask(player, progressTitle, progressColor, timeInSeconds) {
            @Override
            public void onSuccess() {
                runnable.run();
                player.clearProgressBar();
                player.cancelPlayerAnimation();
                player.setPlayerInteruptTask(null);
            }

            @Override
            public void onCanceled() {
                player.clearProgressBar();
                player.cancelPlayerAnimation();
                player.setPlayerInteruptTask(null);
            }
        };
        player.setPlayerInteruptTask(task);
        DedicatedServer.get(TaskService.class).registerTask(task);
        return task;
    }

    public static HiveTask scheduledDelayTask(Runnable runnable, Long delay, boolean isAsync) {
        HiveDelayedTask d = new HiveDelayedTask(UUID.randomUUID().toString(), delay, isAsync) {
            @Override
            public void run() {
                try {
                    runnable.run();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        DedicatedServer.get(TaskService.class).registerTask(d);
        return d;
    }

    public static HiveTask sync(Runnable runnable) {
        HiveDelayedTask t = new HiveDelayedTask(UUID.randomUUID().toString(), 1L, false) {
            @Override
            public void run() {
                try {
                    runnable.run();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        DedicatedServer.get(TaskService.class).registerTask(t);
        return t;
    }

    public static HiveTask async(Runnable runnable) {
        HiveDelayedTask t = new HiveDelayedTask(UUID.randomUUID().toString(), 1L, true) {
            @Override
            public void run() {
                try {
                    runnable.run();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        DedicatedServer.get(TaskService.class).registerTask(t);
        return t;
    }

    public static HiveTask scheduleRepeatingTask(Runnable runnable, Long delay, Long period, boolean isAsync) {
        HiveRepeatingTask d = new HiveRepeatingTask(UUID.randomUUID().toString(), delay, period, isAsync) {
            @Override
            public void run() {
                try {
                    runnable.run();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        DedicatedServer.get(TaskService.class).registerTask(d);
        return d;
    }

    public static HiveTask scheduleRepeatingLimitedTask(Runnable runnable, Long delay, Long period, int timesToRepeat, boolean isAsync) {
        HiveLimitedRepeatingTask d = new HiveLimitedRepeatingTask(UUID.randomUUID().toString(), delay, period, timesToRepeat, isAsync) {
            @Override
            public void run() {
                try {
                    runnable.run();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        DedicatedServer.get(TaskService.class).registerTask(d);
        return d;
    }

    public static HiveTask scheduleTaskSequence(boolean isAsync, SequenceAction... actions) {
        HiveTaskSequence sequence = new HiveTaskSequence(isAsync);
        for (SequenceAction a : actions) {
            sequence.add(a);
        }
        DedicatedServer.get(TaskService.class).registerTask(sequence);
        return sequence;
    }

    public static void scheduleTaskSequence(HiveTaskSequence sequence) {
        DedicatedServer.get(TaskService.class).registerTask(sequence);
    }

    public static void scheduleConditionalRepeatingTask(HiveConditionalRepeatingTask hiveConditionalRepeatingTask) {
        DedicatedServer.get(TaskService.class).registerTask(hiveConditionalRepeatingTask);
    }

    @Override
    public void init() {
        this.asyncPool = Executors.newFixedThreadPool(2);
    }

    public void registerTask(HiveTask task) {
        this.tasks.put(task.getUuid(), task);
    }

    public void cancelTask(UUID uuid) {
        this.tasks.get(uuid).cancel();
    }

    public void removeTask(UUID uuid) {
        this.tasks.remove(uuid);
    }

    public Long milliFromTps() {
        return 1000 / this.tps;
    }

    public void tick() {
        if (System.currentTimeMillis() >= this.nextTaskTrigger) {

            Long tickStart = System.currentTimeMillis();
            Long nextTick = System.currentTimeMillis() + milliFromTps();

            // Trigger a task check
            for (HiveTask t : this.tasks.values()) {
                if (t.isCanceled()) {
                    this.tasks.remove(t.getUuid());
                    continue;
                }

                if (t.isReady()) {
                    // Should run.
                    if (t.isAsync()) {
                        // Submit to execute
                        this.asyncPool.submit(() -> {
                            t.run();
                            t.tick();
                        });
                    } else {
                        t.run();
                        t.tick();
                    }
                }
            }

            this.nextTaskTrigger = nextTick;
        }
    }
}
