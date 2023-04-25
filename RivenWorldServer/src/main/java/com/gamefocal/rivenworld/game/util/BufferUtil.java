package com.gamefocal.rivenworld.game.util;

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

    /**
     * Returns the position in a ByteBuffer based on x, y coordinates, considering each element as an int.
     *
     * @param buffer the ByteBuffer containing the data
     * @param width the width of the 2D array
     * @param x the x-coordinate
     * @param y the y-coordinate
     * @return the position in the ByteBuffer
     */
    public static int getPositionInByteBuffer(ByteBuffer buffer, int width, int x, int y) {
        if (buffer == null) {
            throw new IllegalArgumentException("ByteBuffer cannot be null.");
        }

        int position = (y * width + x) * Float.BYTES;

        if (position < 0 || position >= buffer.capacity()) {
            throw new IllegalArgumentException("Coordinates are out of bounds.");
        }

        return position;
    }

}
