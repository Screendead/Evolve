package com.screendead.evolve.data;

import java.util.ArrayList;

// Without  instancing 2.0432107 seconds
// With     instancing 0.5937610 seconds
// Speed improvement: 3.4x

public class World {
    private ArrayList<Chunk> chunks = new ArrayList<>();

    public World(int size, float scale, float height, float detail) {

    }

    public void render() {
        for (Chunk c : chunks) {
            c.render();
        }
    }

    public void cleanup() {
        for (Chunk c : chunks) {
            c.cleanup();
        }
    }
}
