package com.screendead.evolve;

import com.screendead.evolve.graphics.Window;

public class Evolve {
    private GameController gc;
    private Window window;

    private Evolve() {
        gc = new GameController();
        gc.start();
        gc.stop();
    }

    private void loop() {

    }

    public static String getResource(String name) {
        String out = "src/main/resources/" + name;
        System.out.println("Loading " + out + " ...");
        return out;
    }

    public static void main(String[] args) {
        new Evolve();
    }
}
