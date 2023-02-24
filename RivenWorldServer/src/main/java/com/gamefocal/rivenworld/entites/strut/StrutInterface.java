package com.gamefocal.rivenworld.entites.strut;

import org.apache.commons.lang3.ArrayUtils;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class StrutInterface<T> {

    ByteBuffer buffer;

    protected int size = 0;

    private ByteOrder byteOrder;

    private CheckSum checkSum = CheckSum.NONE;

    private Strut strut;

    public int getSize() {
        return size;
    }

    public StrutInterface() {
    }

    public static StrutByteOrder strutByteOrderOverride = StrutByteOrder.INHERIT;

//    private static LogService ls = SpaceSDK.getInstance().getInjector().getInstance(LogService.class);

    private int getSizeOfNativeField(Class type) {
        if (type == short.class) {
            return 2;
        } else if (type == int.class) {
            return 4;
        } else if (type == float.class) {
            return 4;
        } else if (type == byte.class) {
            return 1;
        } else if (type == long.class) {
            return 8;
        }

        return 0;
    }

    private Number addNumbers(Number a, Number b) {
        if (a instanceof Double || b instanceof Double) {
            return a.doubleValue() + b.doubleValue();
        } else if (a instanceof Float || b instanceof Float) {
            return a.floatValue() + b.floatValue();
        } else if (a instanceof Long || b instanceof Long) {
            return a.longValue() + b.longValue();
        } else {
            return a.intValue() + b.intValue();
        }
    }

    private Number multiplyNumbers(Number a, Number b) {
        if (a instanceof Double || b instanceof Double) {
            return a.doubleValue() * b.doubleValue();
        } else if (a instanceof Float || b instanceof Float) {
            return a.floatValue() * b.floatValue();
        } else if (a instanceof Long || b instanceof Long) {
            return a.longValue() * b.longValue();
        } else {
            return a.intValue() * b.intValue();
        }
    }

    public Class getBaseTypeOfArray(Class type) {
        if (type.isArray()) {
            Class subtype = type.getComponentType();
            return this.getBaseTypeOfArray(subtype);
        } else {
            return type;
        }
    }

    public T init() throws Exception {

        this.calcSize();

        this.buildBuffer();

        return (T) this;
    }

    public void calcSize() throws Exception {
        if (this.getClass().getAnnotation(Strut.class) != null) {
            this.strut = this.getClass().getAnnotation(Strut.class);
            this.byteOrder = strut.byteOrder().getBufferOrder();

            if (strut.size() > 0) {
                this.size = strut.size();
            } else {
                for (Field f : this.getClass().getFields()) {
                    if (f.getAnnotation(StrutElement.class) != null) {
                        f.setAccessible(true);
                        StrutElement elm = f.getAnnotation(StrutElement.class);

                        if (List.class.isAssignableFrom(f.getType())) {
                            if (!elm.basedOnField().equalsIgnoreCase("") && elm.listOf() != Void.class) {

                                int sizeOfElm = this.getSizeOfNativeField(elm.listOf());

                                if (StrutInterface.class.isAssignableFrom(elm.listOf())) {
                                    // Is another Strut so we need to calc.
                                    StrutInterface in = (StrutInterface) elm.listOf().newInstance();
                                    in.calcSize();
                                    sizeOfElm = in.getSize();
                                }

                                // Is a valid List, and has a listOf defined.

                                // Get the count set in the ref.
                                Field linkField = this.getClass().getField(elm.basedOnField());

//                                System.out.println("Size of " + f.getName() + " in " + this.getClass().getSimpleName() + ": " + (this.addNumbers((Number) linkField.get(this), sizeOfElm)));

//                                this.size += (this.addNumbers((Number) linkField.get(this), sizeOfElm));

                                this.size = this.addNumbers(this.size, (this.multiplyNumbers((Number) linkField.get(this), sizeOfElm))).intValue();
                            }
                        } else if (f.getType().isArray()) {
                            if (!elm.basedOnField().equalsIgnoreCase("")) {
                                Class typeOfArray = this.getBaseTypeOfArray(f.getType());
                                int sizeOfElm = this.getSizeOfNativeField(typeOfArray);

                                // Get the count set in the ref.
                                Field linkField = this.getClass().getField(elm.basedOnField());

                                this.size = this.addNumbers(this.size, (this.multiplyNumbers((Number) linkField.get(this), sizeOfElm))).intValue();
                            } else {
                                Class typeOfArray = this.getBaseTypeOfArray(f.getType());
                                this.size += (elm.size() * this.getSizeOfNativeField(typeOfArray));
                            }
                        } else {
                            if (f.getType().isPrimitive()) {
                                // Get the size of the field
                                this.size += this.getSizeOfNativeField(f.getType());
                            } else if (StrutInterface.class.isAssignableFrom(f.getType())) {
                                if (f.get(this) == null) {
                                    f.set(this, f.getType().newInstance());
                                }

                                if (f.get(this) != null) {
                                    this.size += ((StrutInterface) f.get(this)).getSize();
                                } else {
                                    throw new Exception("Sub Strut is set to null (" + f.getName() + ")");
                                }
                            } else {
                                throw new Exception("Invalid Type of Field (" + f.getType().getSimpleName() + ")");
                            }
                        }
                    }
                }

                /*
                 * Add a checksum to the end of the message if one is set for the strut
                 * */
                if (strut.hasChecksum()) {
                    // Has a checksum
                    this.size += this.getSizeOfNativeField(this.checksumType());
                }
            }

        } else {
            throw new Exception("A strut interface must be annotated with @Strut");
        }
    }

    private ByteBuffer grow(ByteBuffer buffer, int add) {
        ByteBuffer newBuff = ByteBuffer.allocate(buffer.capacity() + add);
        for (byte b : buffer.array()) {
            newBuff.put(b);
        }

        return newBuff;
    }

    private ByteBuffer addToBuffer(ByteBuffer buffer, Object obj) {
        if (obj == null) {
            // Is obj is null skip and return the buffer
            return buffer;
        }

        // Check to see if buffer is large enough.
        if (buffer.capacity() < this.getSizeOfNativeField(obj.getClass())) {
            int diff = (this.getSizeOfNativeField(obj.getClass()) + buffer.capacity()) - buffer.capacity();
            buffer = this.grow(buffer, Math.abs(diff));
        }

        if (obj instanceof Short) {
            buffer.putShort((Short) obj);
        } else if (obj instanceof Integer) {
            buffer.putInt((Integer) obj);
        } else if (obj instanceof Float) {
            buffer.putFloat((Float) obj);
        } else if (obj instanceof Byte) {
            buffer.put((Byte) obj);
        } else if (obj.getClass().isArray()) {
            int length = Array.getLength(obj);
            for (int i = 0; i < length; i++) {
                Object arrayElement = Array.get(obj, i);
                this.addToBuffer(buffer, arrayElement);
            }
        }

        return buffer;
    }

    private Object[] expandArray(Object[] orgin) {
        Object[] newArray = new Object[orgin.length + 1];
        System.arraycopy(orgin, 0, newArray, 0, orgin.length);
        return newArray;
    }

    public Object getFromBuffer(ByteBuffer buffer, Class type, Object obj, StrutElement element) throws IllegalAccessException {
        if (type == Short.class || type == short.class) {
            return buffer.getShort();
        } else if (type == Integer.class || type == int.class) {
            return buffer.getInt();
        } else if (type == Float.class || type == float.class) {
            return buffer.getFloat();
        } else if (type == Byte.class || type == byte.class) {
            return buffer.get();
        } else if (List.class.isAssignableFrom(type)) {

            /*
             * Is an List.
             * */

            try {
                Class c = element.listOf();
                List<Object> list = new ArrayList<>();

                Field f = this.getClass().getField(element.basedOnField());

//                int size = (int) f.get(this);

                Number size = (Number) f.get(this);

                for (int i = 0; i < size.intValue(); i++) {
                    list.add(this.getFromBuffer(buffer, c, new Object[element.listOfArraySize()], element));
                }

                return list;

            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }

        } else if (type.isArray()) {

            if (!element.basedOnField().equalsIgnoreCase("")) {
                // Is a dynamic array
                try {
                    Class typeOfArray = this.getBaseTypeOfArray(obj.getClass());

                    // Get the count set in the ref.
                    Field linkField = this.getClass().getField(element.basedOnField());

                    int sizeOfArray = (short) linkField.get(this);

                    obj = Array.newInstance(typeOfArray, sizeOfArray);

                    for (int i = 0; i < sizeOfArray; i++) {
                        Array.set(obj, i, this.getFromBuffer(buffer, this.getBaseTypeOfArray(type), Array.get(obj, i), element));
                    }

                    return obj;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                // Is a fixed array
                int sizeOfArray = ArrayUtils.getLength(obj);
                for (int i = 0; i < sizeOfArray; i++) {
                    Array.set(obj, i, this.getFromBuffer(buffer, this.getBaseTypeOfArray(type), Array.get(obj, i), element));
                }
                return obj;
            }
        }

        return null;
    }

    public T resetBuffer() {
        this.buffer = null;
        this.buffer = ByteBuffer.allocate(this.getSize());

        return (T) this;
    }

    public T buildBuffer() throws IllegalAccessException {
        return this.buildBuffer(null);
    }

    public T buildBuffer(ByteBuffer buffer) throws IllegalAccessException {
        boolean orgin = false;
        if (buffer == null) {
            buffer = ByteBuffer.allocate(this.getSize());
            orgin = true;
        }

        if (this.strut != null && this.strut.fillWith() != 0x00) {
            int pos = buffer.position();
            for (int i = 0; i < this.getSize(); i++) {
                buffer.put(this.strut.fillWith());
            }
            buffer.position(pos);
        }

        if (this.strut == null) {
            // Init.
            try {
                this.calcSize();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

//        buffer.order(this.byteOrder); // Possibly don't need this

        // Fill the buffer
        for (Field f : this.getClass().getFields()) {
            if (f.getAnnotation(StrutElement.class) != null) {
                StrutElement element = f.getAnnotation(StrutElement.class);
                f.setAccessible(true);
                if (StrutInterface.class.isAssignableFrom(f.getType())) {
                    if (element.byteOrder() != StrutByteOrder.INHERIT) {
                        // Has a override
                        strutByteOrderOverride = element.byteOrder();
                    }

                    ((StrutInterface) f.get(this)).buildBuffer(buffer);
                } else {

                    if (element.byteOrder() == StrutByteOrder.INHERIT) {
                        if (strutByteOrderOverride != StrutByteOrder.INHERIT) {
                            buffer.order(strutByteOrderOverride.getBufferOrder());
                        } else {
                            buffer.order(this.strut.byteOrder().getBufferOrder());
                        }
                    } else {
                        buffer.order(element.byteOrder().getBufferOrder());
                    }

                    if (List.class.isAssignableFrom(f.getType())) {
                        // Is an List.
                        for (Object o : ((List) f.get(this))) {
                            if (StrutInterface.class.isAssignableFrom(o.getClass())) {
                                if (element.byteOrder() != StrutByteOrder.INHERIT) {
                                    // Has a override
                                    strutByteOrderOverride = element.byteOrder();
                                }

                                ((StrutInterface) o).buildBuffer(buffer);
                            } else {
                                buffer = this.addToBuffer(buffer, o);
                            }
                        }
                    } else {
                        buffer = this.addToBuffer(buffer, f.get(this));
                    }
                }
            }
        }

        this.buffer = buffer;

        if (orgin && this.strut.hasChecksum()) {
            int sizeOfChecksum = this.getSizeOfNativeField(this.checksumType());
            this.buffer.position((buffer.capacity() - (sizeOfChecksum)));
            this.buffer.order(this.strut.checkSumOrder().getBufferOrder());

            Object cc = this.calcChecksum();

            this.buffer = this.addToBuffer(buffer, cc);
        }

        return (T) this;
    }

    public byte[] getArray() {
        return buffer.array();
    }

    public static <T extends StrutInterface> T fromArray(T obj, byte[] bytes) {
        try {

            obj.calcSize();
            obj.readIn(ByteBuffer.wrap(bytes));

            return obj;

        } catch (Exception e) {
//            ls.fatal(ExceptionUtils.getStackTrace(e));
            e.printStackTrace();
        }

        return null;
    }

    public ByteBuffer readIn(ByteBuffer buffer) throws Exception {
        Strut strut = this.getClass().getAnnotation(Strut.class);

        if (this.buffer == null) {
            this.buffer = buffer;
        }

        for (Field f : this.getClass().getFields()) {
            if (f.getAnnotation(StrutElement.class) != null) {
                StrutElement element = f.getAnnotation(StrutElement.class);
                if (element.byteOrder() != StrutByteOrder.INHERIT) {
                    buffer.order(element.byteOrder().getBufferOrder());
                } else {
                    buffer.order(strut.byteOrder().getBufferOrder());
                }

                f.setAccessible(true);
                if (StrutInterface.class.isAssignableFrom(f.getType())) {
                    buffer = ((StrutInterface) f.get(this)).readIn(buffer);
                } else {
                    f.set(this, this.getFromBuffer(buffer, f.getType(), f.get(this), element));
                }
            }
        }

        return buffer;
    }

    public ByteBuffer getBuffer() {
        return buffer;
    }

    public Class checksumType() {
        return short.class;
    }

    public Object calcChecksum() {
        byte[] data = this.getArray();
        byte[] b = Arrays.copyOfRange(data, 8 * 2, (data.length - 2));

        CRC16 crc16 = new CRC16(b);
        return crc16.getValue();
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();

        builder.append("__ [").append(this.getClass().getSimpleName()).append("] __").append("\n");

        try {
            for (Field f : this.getClass().getFields()) {
                if (f.getAnnotation(StrutElement.class) != null) {
                    StrutElement element = f.getAnnotation(StrutElement.class);
                    f.setAccessible(true);
                    if (StrutInterface.class.isAssignableFrom(f.getType())) {
                        builder.append(f.get(this).toString());
                    } else {
                        if (f.get(this) != null) {
                            builder.append(f.getName()).append(": ").append(f.get(this).toString()).append("\n");
                        } else {
                            builder.append(f.getName()).append(": ").append(" NULL FIELD").append("\n");
                        }
                    }
                }
            }
        } catch (Exception e) {
//            ls.fatal(ExceptionUtils.getStackTrace(e));
            e.printStackTrace();
        }
        builder.append("__END__\n");

        return builder.toString();
    }
}
