package com.gamefocal.island.threads;

import com.gamefocal.island.DedicatedServer;
import com.gamefocal.island.entites.config.HiveConfigFile;
import com.gamefocal.island.entites.thread.AsyncThread;
import com.gamefocal.island.entites.thread.HiveAsyncThread;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

@AsyncThread(name = "hive-hb")
public class HiveHbThread implements HiveAsyncThread {
    @Override
    public void run() {

        while (true) {

            HiveConfigFile configFile = DedicatedServer.instance.getConfigFile();

            JsonArray players = new JsonArray();

            JsonObject obj = configFile.getConfig().deepCopy();
            obj.add("players", players);

            try {
                HttpResponse<String> s = Unirest.post("https://api.gamefocal.com/riven/servers").body(obj.toString()).asString();
                JsonObject re = JsonParser.parseString(s.getBody()).getAsJsonObject();
                if (!re.has("success") || !re.get("success").getAsBoolean()) {
                    System.err.println("Failed to register server with hive... will try again later...");
                }
            } catch (UnirestException e) {
                e.printStackTrace();
            }

            try {
                Thread.sleep(60 * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
}
