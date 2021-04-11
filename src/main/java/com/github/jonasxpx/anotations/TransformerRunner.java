package com.github.jonasxpx.anotations;

public interface TransformerRunner<T> {

    T run(T entity);
}
