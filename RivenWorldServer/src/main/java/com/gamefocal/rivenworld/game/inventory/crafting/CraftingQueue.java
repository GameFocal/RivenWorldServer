package com.gamefocal.rivenworld.game.inventory.crafting;

import com.gamefocal.rivenworld.DedicatedServer;
import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.events.crafting.CraftItemEvent;
import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.inventory.Inventory;
import com.gamefocal.rivenworld.game.inventory.InventoryStack;
import com.gamefocal.rivenworld.game.sounds.GameSounds;
import com.gamefocal.rivenworld.game.ui.CraftingUI;
import com.gamefocal.rivenworld.service.PlayerService;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.Serializable;
import java.util.LinkedList;

public class CraftingQueue implements Serializable {

    private transient LinkedList<CraftingRecipe> allowedRecipes = new LinkedList<>();
    private int size = 0;
    private LinkedList<CraftingJob> jobs = new LinkedList<>();
    private boolean process = true;

    private boolean requireOpen = true;

    public CraftingQueue(int jobSize) {
        this.size = jobSize;
        this.allowedRecipes = new LinkedList<>();
    }

    public CraftingQueue(int jobSize, boolean requireOpen) {
        this.size = jobSize;
        this.requireOpen = requireOpen;
        this.allowedRecipes = new LinkedList<>();
    }

    public CraftingQueue() {
        this.allowedRecipes = new LinkedList<>();
    }

    public void setAllDestInventories(Inventory inventories) {
        for (CraftingJob job : this.jobs) {
            job.setDestinationInventory(inventories);
        }
    }

    public void setAllSourceInventories(Inventory inventories) {
        for (CraftingJob job : this.jobs) {
            job.setSourceInventory(inventories);
        }
    }

    public boolean isProcess() {
        return process;
    }

    public void setProcess(boolean process) {
        this.process = process;
    }

    public LinkedList<CraftingJob> getJobs() {
        return jobs;
    }

    public boolean tick(HiveNetConnection connection) {
        if (this.process) {
            CraftingJob job = jobs.peek();
            if (job != null) {
                if (!job.isStarted()) {
                    job.start();

                    if (connection != null && connection.getOpenUI() != null && CraftingUI.class.isAssignableFrom(connection.getOpenUI().getClass())) {
                        connection.getOpenUI().update(connection);
                    }

                    DedicatedServer.instance.getWorld().playSoundAtLocation(GameSounds.CRAFTING, job.getLocation(), 200f, 5f, 1f);
                } else {

                    /*
                     * Check for the storage to see if it can take the finished item
                     * */

                    if (job.getDestinationInventory() == null) {
                        return false;
                    }

                    if (!job.getDestinationInventory().canAdd(new InventoryStack(job.getRecipe().getProduces(), job.getRecipe().getProducesAmt()))) {
                        return false;
                    }

                    job.tick(connection);

                    if (job.isComplete()) {
                        // Is Complete finish actions here.
                        jobs.poll();

                        if (job.getOwnedBy() != null) {
                            if (DedicatedServer.get(PlayerService.class).players.containsKey(job.getOwnedBy())) {
                                new CraftItemEvent(DedicatedServer.get(PlayerService.class).players.get(job.getOwnedBy()), job.getRecipe(), job, job.getFromUi()).call();
                            }
                        }

//                        new CraftItemEvent(connection,)

                        return true;
                    }
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

    public boolean hasEmptySlot() {
        return (this.jobs.size() < this.size);
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

    public LinkedList<CraftingRecipe> getAllowedRecipes() {
        return allowedRecipes;
    }

    public void setAllowedRecipes(LinkedList<CraftingRecipe> allowedRecipes) {
        this.allowedRecipes = allowedRecipes;
    }

    public void addAllowedRecipes(CraftingRecipe... craftingRecipes) {
        if (this.allowedRecipes == null) {
            this.allowedRecipes = new LinkedList<>();
        }

        for (CraftingRecipe recipe : craftingRecipes) {
            if (!this.allowedRecipes.contains(recipe)) {
                this.allowedRecipes.add(recipe);
            }
        }
//        this.allowedRecipes.addAll(Arrays.asList(craftingRecipes));
    }

    public boolean isRequireOpen() {
        return requireOpen;
    }

    public void setRequireOpen(boolean requireOpen) {
        this.requireOpen = requireOpen;
    }

    public void cancelBySlotNumber(int slot) {
        this.jobs.get(slot).cancel();
        this.jobs.remove(slot);
    }

    public JsonObject toJson(Inventory fromInventory) {
        JsonObject o = new JsonObject();

        JsonArray rec = new JsonArray();
        for (CraftingRecipe r : this.allowedRecipes) {
            rec.add(r.toJson(fromInventory));
        }
        o.add("rec", rec);

        CraftingJob current = null;
        if (this.jobs.size() > 0) {
            CraftingJob j = this.jobs.peek();
            if (j != null && j.isStarted()) {
                current = j;
            }
        }

        if (current != null) {
            o.add("current", current.toJson());
            o.addProperty("started", current.getStartedAt());
        } else {
            o.add("current", new JsonObject());
            o.addProperty("started", 0);
        }

        JsonArray q = new JsonArray();
//        int i = 0;
        for (int i = 0; i < this.size; i++) {
            if (i < this.jobs.size()) {
                q.add(this.jobs.get(i).toJson());
            } else {
                // Not a job
                q.add(new JsonObject());
            }
        }
//        for (CraftingJob j : this.jobs) {
//            q.add(j.toJson());
//        }

        o.add("q", q);
        return o;
    }

}
