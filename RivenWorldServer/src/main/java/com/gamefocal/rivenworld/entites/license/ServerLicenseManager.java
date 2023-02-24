package com.gamefocal.rivenworld.entites.license;

import com.gamefocal.rivenworld.DedicatedServer;
import com.gamefocal.rivenworld.entites.config.HiveConfigFile;
import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.service.PlayerService;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import lowentry.ue4.classes.RsaPrivateKey;
import lowentry.ue4.library.LowEntry;

import java.util.Base64;

public class ServerLicenseManager {

    private static String endpoint = "https://api.hive.rivenworld.net" /*"https://3a5d-47-160-166-152.ngrok.io"*/;

    private String licenseKey;

    private String sessionId;

    private HiveConfigFile configFile;

    private RsaPrivateKey privateKey;

    public ServerLicenseManager(String licenseKey, HiveConfigFile configFile) {
        this.configFile = configFile;
        this.licenseKey = licenseKey;
    }

    public boolean getPlayerData(String playerSession, HiveNetConnection sender) {
        try {
            HttpResponse<String> r = Unirest.get(endpoint + "/server/session/{ss}/player/{ps}")
                    .header("Content-Type", "application/json")
                    .routeParam("ss", this.sessionId)
                    .routeParam("ps", playerSession)
                    .queryString("license", this.licenseKey).asString();

            JsonObject o = JsonParser.parseString(r.getBody()).getAsJsonObject();

            System.out.println("[Hive]: Requesting Player Data (S:" + this.sessionId + "/P:" + playerSession + ")");

            if (o.has("success") && o.get("success").getAsBoolean()) {

                JsonObject data = o.get("data").getAsJsonObject();

                sender.setHiveId(data.get("pid").getAsString());
                sender.setHiveDisplayName(data.get("display").getAsString());
                sender.setNetAppearance(data.get("appearance").getAsJsonObject());

                byte[] keyData = Base64.getDecoder().decode(data.get("player_key").getAsString());

                try {
                    // Process Key
                    sender.setPublicKey(LowEntry.bytesToRsaPublicKey(keyData));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return true;
            } else {
                System.err.println(o.toString());
            }


        } catch (UnirestException e) {
            e.printStackTrace();
        }

        return false;
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

                byte[] rawBytes = Base64.getDecoder().decode(o.get("data").getAsJsonObject().get("pk").getAsString());
//                String rawKeyData = new String(keyData);
//                rawKeyData = rawKeyData.replace("-----BEGIN PRIVATE KEY-----", "");
//                rawKeyData = rawKeyData.replace("-----END PRIVATE KEY-----", "");
//                rawKeyData = rawKeyData.replaceAll("\\s+", "");

//                byte[] rawBytes = Base64.getDecoder().decode(rawKeyData);

                try {
//                    KeyFactory factory = KeyFactory.getInstance("RSA");
////                PKCS8EncodedKeySpec keySpecPv = new PKCS8EncodedKeySpec(keyData);
//                    PKCS8EncodedKeySpec keySpecPv = new PKCS8EncodedKeySpec(rawBytes);
                    this.privateKey = LowEntry.bytesToRsaPrivateKey(rawBytes);

                    this.sessionId = o.get("data").getAsJsonObject().get("sid").getAsString();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                System.out.println("[Hive]: Registered with Hive, SID " + this.sessionId);
//                System.out.println("[Hive]: Session Started with Hash " + DigestUtils.md5Hex(LowEntry.rsa));

                return true;

            } else {
                System.err.println("[Hive Error]: " + o.get("data").getAsJsonObject().get("error").getAsString());
                System.exit(0);
            }

        } catch (UnirestException e) {
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
                System.err.println("[HB-JSON]: " + o.toString());
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

    public RsaPrivateKey getPrivateKey() {
        return privateKey;
    }
}
