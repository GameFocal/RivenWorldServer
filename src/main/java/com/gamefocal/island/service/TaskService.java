package com.gamefocal.island.service;

import com.gamefocal.island.DedicatedServer;
import com.gamefocal.island.entites.service.HiveService;
import com.gamefocal.island.game.tasks.HiveDelayedTask;
import com.gamefocal.island.game.tasks.HiveRepeatingTask;
import com.gamefocal.island.game.tasks.HiveTask;
import com.google.auto.service.AutoService;

import javax.inject.Singleton;
import java.util.Hashtable;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

@AutoService(HiveService.class)
@Singleton
public class TaskService implements HiveService<TaskService> {

    private ConcurrentHashMap<UUID, HiveTask> tasks = new ConcurrentHashMap<>();

    private Long nextTaskTrigger = 0L;

    private Long tps = 20L;

    private ExecutorService asyncPool;

    @Override
    public void init() {
        this.asyncPool = Executors.newFixedThreadPool(3);
    }

    public static void scheduledDelayTask(Runnable runnable, Long delay, boolean isAsync) {
        HiveDelayedTask d = new HiveDelayedTask(UUID.randomUUID().toString(), delay, isAsync) {
            @Override
            public void run() {
                runnable.run();
            }
        };
        DedicatedServer.get(TaskService.class).registerTask(d);
    }

    public static void scheduleRepeatingTask(Runnable runnable, Long delay, Long period, boolean isAsync) {
        HiveRepeatingTask d = new HiveRepeatingTask(UUID.randomUUID().toString(), delay, period, isAsync) {
            @Override
            public void run() {
                runnable.run();
            }
        };
        DedicatedServer.get(TaskService.class).registerTask(d);
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

    public void tick() {
        if (System.currentTimeMillis() >= this.nextTaskTrigger) {
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
                        this.asyncPool.submit(new Runnable() {
                            @Override
                            public void run() {
                                t.run();
                                t.tick();
                            }
                        });
                    } else {
                        t.run();
                        t.tick();
                    }
                }
            }

            this.nextTaskTrigger = (System.currentTimeMillis() + 50);
        }
    }
}
