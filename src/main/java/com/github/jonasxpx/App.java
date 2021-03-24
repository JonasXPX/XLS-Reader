package com.github.jonasxpx;

import com.github.jonasxpx.facade.FaturaReader;

public class App {

    public static void main(String[] args) throws Exception {
        new FaturaReader()
                .read();
    }
}
