package com.gamefocal.rivenworld.entites.util;

import fr.devnied.bitlib.BitUtils;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class BufferUtil {
    public static void putUnsignedByte(ByteBuffer buf, int value) {
        buf.put((byte) (value & 0xFF));
    }

    public static void putUnsignedShort(ByteBuffer buf, int value) {
        buf.putShort((short) (value & 0xFFFF));
    }


    public static void putUnsignedInt(ByteBuffer buf, long value) {
        buf.putInt((int) (value & 0xFFFFFFFFL));
    }

    public static byte[] buffetToArray(ByteBuffer buffer, int size) {
        byte[] arr = new byte[size];
        buffer.position(0);
        buffer.get(arr, 0, buffer.remaining());
        return arr;
    }

    public static byte[] bufferToArray(ByteBuffer buffer, int start, int len) {
        byte[] arr = new byte[len];
        buffer.position(start);
        buffer.get(arr, start, len);
        return arr;
    }

    public static ByteBuffer push(ByteBuffer byteBuffer, byte[] bytes) {
        // Go the the first open spot of the buffer
        for (byte b : bytes) {
            byteBuffer.put(b);
        }

        return byteBuffer;
    }

    public static ByteBuffer push(ByteBuffer byteBuffer, ByteBuffer add) {
        for (byte b : add.array()) {
            byteBuffer.put(b);
        }

        return byteBuffer;
    }

    public static byte[] getSplice(ByteBuffer buffer, int start, int length) {
        int currentPos = buffer.position();
        buffer.position(start);
        byte[] n = new byte[length];
        int i = 0;
        while (buffer.position() < (start + length)) {
            if (i < n.length) {
                n[i++] = buffer.get();
            }
        }

        buffer.position(currentPos);
        return n;
    }

    public static ByteBuffer getSplice(ByteBuffer buffer, ByteOrder order, int start, int length) {
        int currentPos = buffer.position();
        buffer.position(start);
        ByteBuffer n = ByteBuffer.allocate(length);
        n.order(order);

        int i = 0;
        while (buffer.position() < (start + length)) {
            if (i < n.capacity()) {
                n.put(buffer.get());
            }
            i++;
        }

        buffer.position(currentPos);
        return n;
    }

    public static byte[] getNextPart(ByteBuffer buffer, int length) {
        byte[] n = new byte[length];

        int i = 0;
        while (i < length) {
            n[i++] = buffer.get();
        }

        return n;
    }

    public static ByteBuffer getNextPart(ByteBuffer buffer, ByteOrder order, int length) {
        ByteBuffer n = ByteBuffer.allocate(length);
        n.order(order);

        int i = 0;
        while (i < length) {
            n.put(buffer.get());
            i++;
        }

        return n;
    }

    public static void zeroFillBuffer(ByteBuffer buffer) {
        while (buffer.hasRemaining()) {
            BitUtils b = new BitUtils(8);
            b.setNextInteger(0x00, 8);
            buffer.put(b.getData());
        }
    }

    public static short invert(short value) {
        return (short) (((value & 0x00FF) << 8) | ((value & 0xFF00) >> 8));
    }

    public static int invert(int value) {
        return ((value & 0xFF000000) >> 24) | ((value & 0x00FF0000) >> 8) | ((value & 0x0000FF00) << 8) | ((value & 0x000000FF) << 24);
    }

    public static byte invert(byte value) {
        return (byte) (((value & 0x00FF) << 8) | ((value & 0xFF00) >> 8));
    }

}
