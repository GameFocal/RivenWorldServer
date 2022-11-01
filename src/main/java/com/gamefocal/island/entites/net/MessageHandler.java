package com.gamefocal.island.entites.net;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;

import java.util.UUID;

public class MessageHandler {

    private String name;

    public MessageHandler(String name) {
        this.name = name;
    }

    public void routeOut(HiveMessage message, String channel) {
        message.from = this.name;
        message.type = message.getClass().getName();
        if (message.id == null) {
            message.id = UUID.randomUUID().toString();
        }

        String json = new Gson().toJson(message);

//        this.redisService.getPubConnection().async().publish(channel, json);
    }

    public Object handleMessage(String raw) throws ClassNotFoundException {
        try {
            JsonObject o = JsonParser.parseString(raw).getAsJsonObject();

            if (o.get("from").getAsString().equalsIgnoreCase(this.name)) {
                return null;
            }

            if (o.has("type")) {
                // Type of
                Class c = Class.forName(o.get("type").getAsString());
                if (HiveMessage.class.isAssignableFrom(c)) {
                    // Is a hive message class.

//                Class<HiveMessage> hmc = c;

                    return new Gson().fromJson(o, c);
                } else {
                    System.err.println("Message Class " + c.getName() + " not HiveMessage");
                }
            } else {
                System.err.println("Object missing type");
            }

            return null;
        } catch (JsonParseException e) {
            return null;
        }
    }

    public <T> T getObjFromJson(Class<T> t, JsonObject o) {
        return new Gson().fromJson(o, t);
    }

}
