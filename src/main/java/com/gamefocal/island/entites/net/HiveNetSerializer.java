package com.gamefocal.island.entites.net;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

import java.nio.charset.StandardCharsets;

public class HiveNetSerializer extends Serializer<HiveNetMessage> {
    @Override
    public void write(Kryo kryo, Output output, HiveNetMessage hiveNetMessage) {
        StringBuilder b = new StringBuilder();
        b.append(hiveNetMessage.cmd);
        for (String s : hiveNetMessage.args) {
            b.append(",").append(s);
        }
        output.write(b.toString().getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public HiveNetMessage read(Kryo kryo, Input input, Class<? extends HiveNetMessage> aClass) {
        HiveNetMessage netMessage = new HiveNetMessage();

        byte[] b = input.getBuffer();
        String s = new String(b, StandardCharsets.UTF_8);
        String[] p = s.split("\\|");
        netMessage.cmd = p[0];
        netMessage.args = new String[p.length - 1];
        for (int i = 1; i < (p.length - 1); i++) {
            netMessage.args[i - 1] = p[i];
        }

        return netMessage;
    }
}
