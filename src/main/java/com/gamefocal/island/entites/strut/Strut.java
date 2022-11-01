package com.gamefocal.island.entites.strut;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Strut {
    StrutByteOrder byteOrder();

    int size() default -1;

    boolean hasChecksum() default false;

    StrutByteOrder checkSumOrder() default StrutByteOrder.BIG;

    byte fillWith() default 0x00;

}
