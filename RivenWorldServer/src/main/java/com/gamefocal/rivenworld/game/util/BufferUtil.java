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
     * @param width the width of the 2D array
     * @param x     the x-coordinate
     * @param y     the y-coordinate
     * @return the position in the ByteBuffer
     */
    public static int getPositionInByteBuffer(int width, int height, int x, int y) {
        if (x < 0 || x >= width || y < 0 || y >= height) {
            throw new IllegalArgumentException("Coordinates (" + x + ", " + y + ") out of bounds");
        }

        return y * width + x;
    }

}
