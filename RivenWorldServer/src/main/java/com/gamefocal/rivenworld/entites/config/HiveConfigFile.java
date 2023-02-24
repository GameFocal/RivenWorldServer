package com.gamefocal.rivenworld.entites.config;

import com.gamefocal.rivenworld.entites.util.GsonUtil;
import com.google.gson.*;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

public class HiveConfigFile {

    private File file;

    private JsonObject config;

    public HiveConfigFile(File file) {
        this.file = file;
        this.config = new JsonObject();
    }

    public JsonObject getConfig() {
        return config;
    }

    public void load() throws IOException {
        if (Files.exists(this.file.toPath())) {
            byte[] data = Files.readAllBytes(this.file.toPath());
            this.config = JsonParser.parseString(new String(data, StandardCharsets.UTF_8)).getAsJsonObject();
        } else {
            this.generate();
        }
    }

    public void save() throws IOException {
        if (!Files.exists(this.file.toPath())) {
            this.generate();
        }

        Gson g = new GsonBuilder().setPrettyPrinting().create();

        Files.write(this.file.toPath(), g.toJson(this.config).getBytes(StandardCharsets.UTF_8));
    }

    public void generate() throws IOException {
        if (!Files.exists(this.file.toPath())) {
            Files.createFile(this.file.toPath());
        }
    }

    public void merge(JsonElement e) {
        this.config = GsonUtil.merge(this.config, e).getAsJsonObject();
    }

}
