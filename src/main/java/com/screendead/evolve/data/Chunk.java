package com.screendead.evolve.data;

import org.joml.Vector3f;

import static org.lwjgl.stb.STBPerlin.stb_perlin_turbulence_noise3;

public class Chunk {
    private static final int SIZE = 16;
    private final int x, z;
    private final float height, worldHeight, scale, detail;
    private float[][] noise;
    private Mesh mesh;

    public Chunk(int x, int z, float scale, float height, float detail) {
        this.x = x;
        this.z = z;

        this.height = SIZE * height;
        this.worldHeight = height * scale;
        this.scale = scale;
        this.detail = detail;

        initNoise(SIZE, scale, height, detail);

        generate();
    }

    private static float terrainHeight(float x, float z) {
        return Math.max(noise(x, 0.0f, z), 0.65f);
    }

    private static float noise(float x, float y, float z) {
        return stb_perlin_turbulence_noise3(x, y, z, 3.5f, 0.5f, 5) / 2.0f + 0.5f;
    }

    private void generate() {
        float[] v, n, c;
        int[] i;

        v = new float[18 * SIZE * SIZE]; // 3 values * 6 points
        n = new float[18 * SIZE * SIZE]; // 3 values * 6 points
        c = new float[24 * SIZE * SIZE]; // 4 values * 6 points
        i = new int[6 * SIZE * SIZE]; // 6 points

        // Vertices
        for (int z = 0; z < SIZE - 1; z++) {
            for (int x = 0; x < SIZE - 1; x++) {
                int index = (x + SIZE * z) * 18;

                // Triangle 1
                {
                    // Vertex 1
                    v[index] = x * scale;
                    v[index + 1] = noise[z][x];
                    v[index + 2] = z * scale;
                    // Vertex 2
                    v[index + 3] = x * scale;
                    v[index + 4] = noise[z + 1][x];
                    v[index + 5] = (z + 1) * scale;

                    // Vertex 3
                    v[index + 6] = (x + 1) * scale;
                    v[index + 7] = noise[z][x + 1];
                    v[index + 8] = z * scale;
                }

                // Triangle 2
                {
                    // Vertex 3
                    v[index + 9] = (x + 1) * scale;
                    v[index + 10] = noise[z][x + 1];
                    v[index + 11] = z * scale;

                    // Vertex 2
                    v[index + 12] = x * scale;
                    v[index + 13] = noise[z + 1][x];
                    v[index + 14] = (z + 1) * scale;

                    // Vertex 4
                    v[index + 15] = (x + 1) * scale;
                    v[index + 16] = noise[z + 1][x + 1];
                    v[index + 17] = (z + 1) * scale;
                }
            }
        }

        // Normals
        for (int z = 0; z < SIZE; z++) {
            for (int x = 0; x < SIZE; x++) {
                int index = (x + SIZE * z) * 18;

                // Triangle 1
                {
                    Vector3f U = new Vector3f(v[index + 3], v[index + 4], v[index + 5])
                            .sub(new Vector3f(v[index], v[index + 1], v[index + 2]));
                    Vector3f V = new Vector3f(v[index + 6], v[index + 7], v[index + 8])
                            .sub(new Vector3f(v[index], v[index + 1], v[index + 2]));

                    Vector3f normal = new Vector3f(U.y * V.z - U.z * V.y, U.z * V.x - U.x * V.z, U.x * V.y - U.y * V.x).normalize();

                    n[index] = n[index + 3] = n[index + 6] = normal.x;
                    n[index + 1] = n[index + 4] = n[index + 7] = normal.y;
                    n[index + 2] = n[index + 5] = n[index + 8] = normal.z;
                }

                // Triangle 2
                {
                    Vector3f U = new Vector3f(v[index + 12], v[index + 13], v[index + 14])
                            .sub(new Vector3f(v[index + 9], v[index + 10], v[index + 11]));
                    Vector3f V = new Vector3f(v[index + 15], v[index + 16], v[index + 17])
                            .sub(new Vector3f(v[index + 9], v[index + 10], v[index + 11]));

                    Vector3f normal = new Vector3f(U.y * V.z - U.z * V.y, U.z * V.x - U.x * V.z, U.x * V.y - U.y * V.x).normalize();

                    n[index + 9] = n[index + 12] = n[index + 15] = normal.x;
                    n[index + 10] = n[index + 13] = n[index + 16] = normal.y;
                    n[index + 11] = n[index + 14] = n[index + 17] = normal.z;
                }
            }
        }

        // Colors
        for (int z = 0; z < SIZE; z++) {
            for (int x = 0; x < SIZE; x++) {
                int cindex = (x + SIZE * z) * 24;
                int vindex = (x + SIZE * z) * 18;

                Vector3f color = color(new Vector3f(v[vindex], v[vindex + 1], v[vindex + 2]),
                        new Vector3f(v[vindex + 3], v[vindex + 4], v[vindex + 5]),
                        new Vector3f(v[vindex + 6], v[vindex + 7], v[vindex + 8]));

                c[cindex] = color.x;
                c[cindex + 1] = color.y;
                c[cindex + 2] = color.z;
                c[cindex + 3] = 1.0f;

                c[cindex + 4] = color.x;
                c[cindex + 5] = color.y;
                c[cindex + 6] = color.z;
                c[cindex + 7] = 1.0f;

                c[cindex + 8] = color.x;
                c[cindex + 9] = color.y;
                c[cindex + 10] = color.z;
                c[cindex + 11] = 1.0f;

                color = color(new Vector3f(v[vindex + 9], v[vindex + 10], v[vindex + 11]),
                        new Vector3f(v[vindex + 12], v[vindex + 13], v[vindex + 14]),
                        new Vector3f(v[vindex + 15], v[vindex + 16], v[vindex + 17]));

                c[cindex + 12] = color.x;
                c[cindex + 13] = color.y;
                c[cindex + 14] = color.z;
                c[cindex + 15] = 1.0f;

                c[cindex + 16] = color.x;
                c[cindex + 17] = color.y;
                c[cindex + 18] = color.z;
                c[cindex + 19] = 1.0f;

                c[cindex + 20] = color.x;
                c[cindex + 21] = color.y;
                c[cindex + 22] = color.z;
                c[cindex + 23] = 1.0f;
            }
        }

        // Indices
        for (int a = 0; a < i.length; a++) {
            i[a] = a;
        }

        this.mesh = new Mesh(v, n, c, i);
    }

    private void initNoise(int SIZE, float scale, float height, float detail) {
        noise = new float[SIZE][SIZE];

        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                noise[j][i] = terrainHeight((this.x * SIZE + j) * detail, (this.z * SIZE + i) * detail) * height * scale;
            }
        }
    }

    private Vector3f color(Vector3f p1, Vector3f p2, Vector3f p3) {
        Vector3f average = p1.add(p2).add(p3).div(3);
        Vector3f noiseInputs = new Vector3f(average).div(worldHeight);
        int ratiox = 1, ratioy = 2;
        float height = (ratiox * noise(noiseInputs.x, noiseInputs.y, noiseInputs.z) + ratioy * (average.y / worldHeight)) / (ratiox + ratioy);

        if (noiseInputs.y <= 0.65f) {
            return new Vector3f(0.1f, 0.4f, 1.0f);
        } else if (height < 0.675f) {
            return new Vector3f(0.75f, 0.75f, 0.0f);
        } else if (height < 0.75f) {
//            return new Vector3f(0.5f, 0.25f, 0.0f);
            return new Vector3f(0.0f, 0.5f, 0.0f);
        } else if (height < 0.85f) {
            return new Vector3f(0.7f, 0.65f, 0.65f);
        } else {
            return new Vector3f(0.9f, 0.95f, 1.0f);
        }

//        return new Vector3f(0.2f, 0.4f, 1.0f);
    }

    public void render() {
        this.mesh.render();
    }

    public void cleanup() {
        this.mesh.cleanup();
    }
}
