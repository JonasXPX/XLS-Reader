package com.github.jonasxpx.anotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ColumnIdentify {

    String value() default "";

    Direction readDirection() default Direction.TO_BOTTOM;

    public enum Direction {
        TO_BOTTOM,
        TO_LEFT,
        TO_RIGHT,
        TO_TOP
    }
}
