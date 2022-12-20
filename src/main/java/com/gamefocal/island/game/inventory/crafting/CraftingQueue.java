package com.gamefocal.island.game.inventory.crafting;

import java.io.Serializable;
import java.util.LinkedList;

public class CraftingQueue implements Serializable {

    private int size = 0;
    private LinkedList<CraftingJob> jobs = new LinkedList<>();

    public CraftingQueue(int jobSize) {
        this.jobs.clear();
        this.size = jobSize;
    }

    public LinkedList<CraftingJob> getJobs() {
        return jobs;
    }

    public void tick() {
        CraftingJob job = jobs.peek();
        if (job != null) {

            if (!job.isStarted()) {
                job.start();
            }

            job.tick();

            if (job.isComplete()) {
                // Is Complete finish actions here.
                jobs.removeFirst();
            }
        }
    }

    public boolean queueJob(CraftingJob job) {
        if (this.jobs.size() >= this.size) {
            return false;
        }

        this.jobs.add(job);

        return true;
    }
}
