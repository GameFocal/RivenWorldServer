package com.gamefocal.island.service;

import com.cronutils.model.Cron;
import com.cronutils.model.CronType;
import com.cronutils.model.definition.CronDefinition;
import com.cronutils.model.definition.CronDefinitionBuilder;
import com.cronutils.model.time.ExecutionTime;
import com.cronutils.parser.CronParser;
import com.gamefocal.island.entites.service.HiveService;
import com.gamefocal.island.entites.task.CronTask;
import com.gamefocal.island.entites.task.HiveTask;
import com.gamefocal.island.entites.task.RunLater;
import com.gamefocal.island.entites.task.ScheduleTask;
import com.google.auto.service.AutoService;
import com.google.inject.Singleton;
import org.reflections.Reflections;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Set;
import java.util.TimerTask;
import java.util.concurrent.*;

@Singleton
@AutoService(HiveService.class)
public class TaskService implements HiveService<TaskService> {
    //    private Timer timer = new Timer();
    private ScheduledExecutorService timer = Executors.newScheduledThreadPool(6);

    private ConcurrentHashMap<String, ScheduledFuture> futures = new ConcurrentHashMap<>();

    public void runLater(HiveTask runnable, long delay) {
        System.out.println("Scheduled a Run Later Task (" + runnable.getFriendlyName() + ")");
//        this.timer.schedule(runnable, delay);
        ScheduledFuture f = this.timer.schedule(runnable, delay, TimeUnit.MILLISECONDS);
        this.futures.put(runnable.getFriendlyName(), f);
    }

    public void scheduleRepeatingTask(HiveTask task, long delay, long timeBetween) {
        System.out.println("Scheduling Repeating Task (" + task.getFriendlyName() + " period)");
//        this.timer.scheduleAtFixedRate(task, delay, timeBetween);
        ScheduledFuture f = this.timer.scheduleAtFixedRate(task, delay, timeBetween, TimeUnit.MILLISECONDS);
        this.futures.put(task.getFriendlyName(), f);
    }

    @Override
    public void init() {
        this.loadTasks();
    }

    public void cancelByName(String firnedlyName) {
        if (this.futures.containsKey(firnedlyName)) {
            this.futures.get(firnedlyName).cancel(true);
            this.futures.remove(firnedlyName);
        }
    }

    public void loadTasks() {

        // Load any annotations of a TimerTask in the project
        Set<Class<?>> runLater = new Reflections("com.gamefocal").getTypesAnnotatedWith(RunLater.class);
        Set<Class<?>> scheduleTask = new Reflections("com.gamefocal").getTypesAnnotatedWith(ScheduleTask.class);
        Set<Class<?>> cronTasks = new Reflections("com.gamefocal").getTypesAnnotatedWith(CronTask.class);

        int runLaterC = 0;
        int scheduleC = 0;

        for (Class c : runLater) {
            try {
                RunLater ann = (RunLater) c.getAnnotation(RunLater.class);
                HiveTask task = (HiveTask) c.newInstance();
                task.setFriendlyName(ann.name());
                this.runLater(task, ann.delay());
                runLaterC++;
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        for (Class c : scheduleTask) {
            try {
                ScheduleTask ann = (ScheduleTask) c.getAnnotation(ScheduleTask.class);
                HiveTask task = (HiveTask) c.newInstance();
                task.setFriendlyName(ann.name());
                this.scheduleRepeatingTask(task, ann.delay(), ann.period());
                scheduleC++;
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        for (Class c : cronTasks) {
            try {
                CronTask ann = (CronTask) c.getAnnotation(CronTask.class);
                HiveTask task = (HiveTask) c.newInstance();

                CronDefinition definition = CronDefinitionBuilder.instanceDefinitionFor(CronType.UNIX);
                CronParser parser = new CronParser(definition);
                Cron cron = parser.parse(ann.cronExpression());

                ZonedDateTime now = ZonedDateTime.now().toLocalDateTime().atZone(ZoneId.of("UTC"));
                ExecutionTime execTime = ExecutionTime.forCron(cron);

                this.timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        task.run();
                        ZonedDateTime now = ZonedDateTime.now().toLocalDateTime().atZone(ZoneId.of("UTC"));
                        ExecutionTime execTime = ExecutionTime.forCron(cron);
//                        timer.schedule(this, TimeUtil.convertToDateViaInstant(execTime.nextExecution(now).get().toLocalDateTime()));

                        long diff = System.currentTimeMillis() - (execTime.nextExecution(now).get().toEpochSecond() * 1000);

                        timer.schedule(this, diff, TimeUnit.MILLISECONDS);
                    }
                }, (System.currentTimeMillis() - (execTime.nextExecution(now).get().toEpochSecond() * 1000)), TimeUnit.SECONDS);

            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        System.out.println("\tRun Later Tasks Imported: " + runLaterC);
        System.out.println("\tScheduled Repeating Tasks Imported: " + scheduleC);
    }
}
