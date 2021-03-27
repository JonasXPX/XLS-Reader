package com.github.jonasxpx.anotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface SheetObject {

    int startAtRow()  default 0;

    int startAtColumn() default 0;

    int endAtRow() default Integer.MAX_VALUE;

    int endAtColumn();



}
