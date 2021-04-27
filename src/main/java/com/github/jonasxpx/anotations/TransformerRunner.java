package com.github.jonasxpx.anotations;

public interface TransformerRunner<T> {

    T run(Object entity);
}
