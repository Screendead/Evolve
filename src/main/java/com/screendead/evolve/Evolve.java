package com.screendead.evolve;

public class Evolve {

    private Evolve() {
        GameController gc = new GameController();
        gc.start();
        gc.stop();
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
