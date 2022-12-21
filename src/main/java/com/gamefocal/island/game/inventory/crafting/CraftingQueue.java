package com.gamefocal.island.game.inventory.crafting;

import com.gamefocal.island.entites.net.HiveNetConnection;
import com.gamefocal.island.game.inventory.Inventory;
import com.gamefocal.island.game.util.InventoryUtil;

import java.io.Serializable;
import java.util.LinkedList;

public class CraftingQueue implements Serializable {

    private int size = 0;
    private LinkedList<CraftingJob> jobs = new LinkedList<>();

    public CraftingQueue(int jobSize) {
//        this.jobs.clear();
        this.size = jobSize;
    }

    public LinkedList<CraftingJob> getJobs() {
        return jobs;
    }

    public boolean tick() {
        CraftingJob job = jobs.peek();
        if (job != null) {

            if (!job.isStarted()) {
                job.start();
            } else {
                job.tick();

                if (job.isComplete()) {
                    // Is Complete finish actions here.
                    jobs.poll();
                    return true;
                }
            }
        }

        return false;
    }

    public int getSize() {
        return size;
    }

    public CraftingJob[] getJobArray() {
        CraftingJob[] arr = new CraftingJob[this.size];
        LinkedList<CraftingJob> j = new LinkedList<>();
        j.addAll(this.jobs);
        for (int i = 0; i < this.size; i++) {
            arr[i] = j.poll();
        }

        return arr;
    }

    public boolean queueJob(CraftingJob job) {
        if (this.jobs.size() >= this.size) {
            return false;
        }

        this.jobs.add(job);

        return true;
    }
}
