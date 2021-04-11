package com.github.jonasxpx.facade;

import com.github.jonasxpx.anotations.TransformerRunner;

public class CustomTransformer implements TransformerRunner<String> {
    @Override
    public String run(String entity) {
        return entity.toUpperCase();
    }
}
