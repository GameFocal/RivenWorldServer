package com.gamefocal.island.game.util;

import java.nio.ByteBuffer;

public class BufferUtil {

    /**
     * Read an unsigned short from a buffer
     *
     * @param buffer Buffer containing the short
     * @return The unsigned short as an int
     */
    public static int getUnsignedShort(ByteBuffer buffer) {
        int pos = buffer.position();
        int rtn = getUnsignedShort(buffer, pos);
        buffer.position(pos + 2);
        return rtn;
    }

    /**
     * Read an unsigned short from a buffer
     *
     * @param buffer Buffer containing the short
     * @param offset Offset at which to read the short
     * @return The unsigned short as an int
     */
    public static int getUnsignedShort(ByteBuffer buffer, int offset) {
        return asUnsignedShort(buffer.getShort(offset));
    }

    /**
     * @return the short value converted to an unsigned int value
     */
    public static int asUnsignedShort(short s) {
        return s & 0xFFFF;
    }

}
