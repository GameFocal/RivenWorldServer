package com.gamefocal.island.entites.net;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Serialization;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class HiveNetSerializer extends Serialization<HiveNetMessage> {
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

    @Override
    public void write(Connection connection, ByteBuffer byteBuffer, Object o) {
        if(HiveNetMessage.class.isAssignableFrom(o.getClass())) {

            HiveNetMessage hm = (HiveNetMessage) o;

            StringBuilder b = new StringBuilder();
            b.append(hm.cmd);
            for (String s : hm.args) {
                b.append(",").append(s);
            }
//            output.write(b.toString().getBytes(StandardCharsets.UTF_8));


        }
    }

    @Override
    public Object read(Connection connection, ByteBuffer byteBuffer) {
        return null;
    }

    @Override
    public int getLengthLength() {
        return 0;
    }

    @Override
    public void writeLength(ByteBuffer byteBuffer, int i) {

    }

    @Override
    public int readLength(ByteBuffer byteBuffer) {
        return 0;
    }
}
