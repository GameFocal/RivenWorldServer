package com.gamefocal.island.entites.license;

import com.gamefocal.island.DedicatedServer;
import com.gamefocal.island.entites.config.HiveConfigFile;
import com.gamefocal.island.service.PlayerService;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.apache.commons.codec.digest.DigestUtils;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;

public class ServerLicenseManager {

    private static String endpoint = "https://api.hive.rivenworld.net" /*"https://3a5d-47-160-166-152.ngrok.io"*/;

    private String licenseKey;

    private String sessionId;

    private HiveConfigFile configFile;

    private PrivateKey privateKey;

    public ServerLicenseManager(String licenseKey, HiveConfigFile configFile) {
        this.configFile = configFile;
        this.licenseKey = licenseKey;
    }

    public boolean register() {

        JsonObject payload = new JsonObject();
        payload.add("config", configFile.getConfig());
        payload.addProperty("playerCount", DedicatedServer.get(PlayerService.class).players.size());
        payload.addProperty("version", DedicatedServer.serverVersion);

        try {
            HttpResponse<String> r = Unirest.post(endpoint + "/server/session").header("Content-Type", "application/json").body(payload.toString()).asString();

            JsonObject o = JsonParser.parseString(r.getBody()).getAsJsonObject();

            if (o.has("success") && o.get("success").getAsBoolean()) {
                // Was a success

                // TODO: Register the privateKey, publicKey and Session Token

                byte[] keyData = Base64.getDecoder().decode(o.get("data").getAsJsonObject().get("pk").getAsString());
                String rawKeyData = new String(keyData);
                rawKeyData = rawKeyData.replace("-----BEGIN PRIVATE KEY-----", "");
                rawKeyData = rawKeyData.replace("-----END PRIVATE KEY-----", "");
                rawKeyData = rawKeyData.replaceAll("\\s+", "");

                byte[] rawBytes = Base64.getDecoder().decode(rawKeyData);

                KeyFactory factory = KeyFactory.getInstance("RSA");
//                PKCS8EncodedKeySpec keySpecPv = new PKCS8EncodedKeySpec(keyData);
                PKCS8EncodedKeySpec keySpecPv = new PKCS8EncodedKeySpec(rawBytes);
                this.privateKey = factory.generatePrivate(keySpecPv);
                this.sessionId = o.get("data").getAsJsonObject().get("sid").getAsString();

                System.out.println("[Hive]: Registered with Hive, SID " + this.sessionId);
                System.out.println("[Hive]: Session Started with Hash " + DigestUtils.md5Hex(this.privateKey.getEncoded()));

                return true;

            } else {
                System.err.println("[Hive Error]: " + o.get("data").getAsJsonObject().get("error").getAsString());
                System.exit(0);
            }

        } catch (UnirestException | NoSuchAlgorithmException | InvalidKeySpecException e) {
            e.printStackTrace();
//            e.printStackTrace();
            System.err.println("[Hive Error]: " + e.getMessage());
            System.exit(0);
        }

        return false;
    }

    public void hb() {
        JsonObject payload = new JsonObject();
        payload.add("config", configFile.getConfig());
        payload.addProperty("playerCount", DedicatedServer.get(PlayerService.class).players.size());
        payload.addProperty("version", DedicatedServer.serverVersion);
        payload.addProperty("license", this.licenseKey);

        try {
            HttpResponse<String> r = Unirest.patch(endpoint + "/server/session/{session}")
                    .routeParam("session", this.sessionId)
//                    .queryString("license", this.licenseKey)
                    .header("Content-Type", "application/json")
                    .body(payload.toString())
                    .asString();

//            System.out.println(r.getBody());

            JsonObject o = JsonParser.parseString(r.getBody()).getAsJsonObject();

            if (o.has("success") && o.get("success").getAsBoolean()) {
                System.out.println("[Hive]: HB Success (Session: " + o.get("data").getAsJsonObject().get("sid").getAsString());
                return;
            } else {
                System.err.println("[Hive]: HB ERR, " + o.get("data").getAsJsonObject().get("message").getAsString());
            }

        } catch (UnirestException e) {
            System.err.println("[Hive]: HB ERR, " + e.getMessage());
            e.printStackTrace();
        }

        System.err.println("[Hive]: HB FAIL");
    }

    public void close() {
        try {
            HttpResponse<String> r = Unirest.delete(endpoint + "/server/session/{session}")
                    .routeParam("session", this.sessionId)
                    .header("license", this.licenseKey)
                    .header("Content-Type", "application/json")
                    .asString();

            JsonObject o = JsonParser.parseString(r.getBody()).getAsJsonObject();

            if (o.has("success") && o.get("success").getAsBoolean()) {
                System.out.println("[Hive] Session Closed... Stopping Server.");
                System.exit(0);
                return;
            }

        } catch (UnirestException e) {
            System.err.println("[Hive]: Session Stop Error, " + e.getMessage());
            e.printStackTrace();
        }

        System.err.println("[Hive]: Session Stop Failed.");
    }

    public String getSessionId() {
        return sessionId;
    }

    public String getLicenseKey() {
        return licenseKey;
    }

    public HiveConfigFile getConfigFile() {
        return configFile;
    }

    public PrivateKey getPrivateKey() {
        return privateKey;
    }
}
