package com.gamefocal.island.game.inventory.crafting;

import com.gamefocal.island.entites.net.HiveNetConnection;
import com.gamefocal.island.game.inventory.Inventory;
import com.gamefocal.island.game.util.InventoryUtil;

import java.io.Serializable;
import java.util.LinkedList;

public class CraftingQueue implements Serializable {

    private int size = 0;
    private LinkedList<CraftingJob> jobs = new LinkedList<>();
    private boolean process = true;

    private boolean requireOpen = true;

    public CraftingQueue(int jobSize) {
        this.size = jobSize;
    }

    public CraftingQueue(int jobSize, boolean requireOpen) {
        this.size = jobSize;
        this.requireOpen = requireOpen;
    }

    public LinkedList<CraftingJob> getJobs() {
        return jobs;
    }

    public boolean tick(HiveNetConnection connection) {
        CraftingJob job = jobs.peek();
        if (job != null) {


            if (!job.isStarted()) {
                job.start();
            } else {
                job.tick(connection);

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

    public void cancelJobByIndex(int index) {
        CraftingJob job = this.jobs.get(index);
        if (job != null) {
            job.cancel();
            this.jobs.remove(index);
        }
    }

    public void clearAndReturnToSource() {
        this.process = false;
        for (CraftingJob job : this.jobs) {
            job.cancel();
        }
        this.jobs.clear();
        this.process = true;
    }

    public boolean isRequireOpen() {
        return requireOpen;
    }

    public void setRequireOpen(boolean requireOpen) {
        this.requireOpen = requireOpen;
    }
}
