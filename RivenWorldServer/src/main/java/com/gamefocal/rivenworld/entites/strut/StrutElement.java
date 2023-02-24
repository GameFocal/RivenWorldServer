package com.gamefocal.rivenworld.entites.strut;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface StrutElement {
    int size() default -1;

    StrutByteOrder byteOrder() default StrutByteOrder.INHERIT;

    boolean dynamic() default false;

    String basedOnField() default "";

    Class listOf() default Void.class;

    int listOfArraySize() default 0;
}
