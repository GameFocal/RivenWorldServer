package com.gamefocal.island.entites.util.cli;

import com.gamefocal.island.DedicatedServer;
import com.gamefocal.island.service.ThreadService;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.UUID;

public class CliTask {

    public UUID uuid;

    public String toRun;

    public StringBuilder reply;

    public Exception exception;

    public boolean isRunning = false;

    public CliPromise onComplete;

    public CliPromise onLine;

    private String lastLine = "";

    public CliTask(String toRun) {
        this.uuid = UUID.randomUUID();
        this.toRun = toRun;
        this.reply = new StringBuilder();
    }

    public UUID getUuid() {
        return uuid;
    }

    public String getToRun() {
        return toRun;
    }

    public StringBuilder getReply() {
        return reply;
    }

    public CliTask onComplete(CliPromise promise) {
        this.onComplete = promise;
        return this;
    }

    public CliTask onLineRead(CliPromise promise) {
        this.onLine = promise;
        return this;
    }

    public String getLastLine() {
        return lastLine;
    }

    public boolean run() throws IOException {
        System.out.println("CLI Task Queued: " + getUuid().toString());
        DedicatedServer.get(ThreadService.class).queueToPool(() -> {
            this.isRunning = true;
            System.out.println("Runnng CLI Task: " + getUuid().toString());
            try {
                ProcessBuilder pb = new ProcessBuilder().command("sh", "-c", this.toRun);
                pb.directory(new File("/usr/local/nebula"));
                Process p = pb.start();
                p.waitFor();

                int exitValue = p.exitValue();
                System.out.println("CLI EXIT VALUE = " + exitValue);

                BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));

                String line;
                while ((line = in.readLine()) != null) {
                    this.reply.append(line);
                }

                System.out.println("--- CLI TASK " + getUuid().toString() + " RETURN ---");
                System.out.println(this.reply.toString());
                System.out.println("--- [END RETURN] ---");

                // Is completed.
                if (this.onComplete != null) {
                    this.onComplete.setTask(this);
                    this.onComplete.run();
                }

            } catch (Exception e) {
                this.exception = e;
            }

            this.isRunning = false;
            System.out.println("Completed CLI Task: " + getUuid().toString());
        });

        return true;
    }
}
