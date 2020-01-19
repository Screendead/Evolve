package com.screendead.evolve.data;

import java.util.ArrayList;

// Without  instancing 2.0432107 seconds
// With     instancing 0.5937610 seconds
// Speed improvement: 3.4x

public class World {
    private ArrayList<Chunk> chunks = new ArrayList<>();

    public World(int size, float scale, float height, float detail) {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                int x = (int) (j - size / 2.0f);
                int z = (int) (i - size / 2.0f);

                chunks.add(new Chunk(x, z, scale, height, detail));
            }
        }
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
