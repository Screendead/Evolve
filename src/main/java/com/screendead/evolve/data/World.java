package com.screendead.evolve.data;

import org.joml.Vector3f;

import static org.lwjgl.stb.STBPerlin.*;

public class World {
    private Mesh mesh;

    public World(int size, float scale, float height, float detail) {
        float[] v, n, c;
        int[] i;


        v = new float[18 * size * size]; // 3 values * 6 points
        n = new float[18 * size * size]; // 3 values * 6 points
        c = new float[24 * size * size]; // 4 values * 6 points
        i = new   int[ 6 * size * size]; // 6 points

        // Vertices
        for (int z = 0; z < size; z++) {
            for (int x = 0; x < size; x++) {
                int index = (x + size * z) * 18;

                // Triangle 1
                {
                    // Vertex 1
                    v[index    ] = x       * scale;
                    v[index + 1] = noise(x * detail, z * detail) * height * scale;
                    v[index + 2] = z       * scale;

                    // Vertex 2
                    v[index + 3] = x       * scale;
                    v[index + 4] = noise(x * detail, (z + 1) * detail) * height * scale;
                    v[index + 5] = (z + 1) * scale;

                    // Vertex 3
                    v[index + 6] = (x + 1) * scale;
                    v[index + 7] = noise((x + 1) * detail, z * detail) * height * scale;
                    v[index + 8] = z       * scale;
                }

                // Triangle 2
                {
                    // Vertex 3
                    v[index +  9] = (x + 1) * scale;
                    v[index + 10] = noise((x + 1) * detail, z * detail) * height * scale;
                    v[index + 11] = z       * scale;

                    // Vertex 2
                    v[index + 12] = x       * scale;
                    v[index + 13] = noise(x * detail, (z + 1) * detail) * height * scale;
                    v[index + 14] = (z + 1) * scale;

                    // Vertex 4
                    v[index + 15] = (x + 1) * scale;
                    v[index + 16] = noise((x + 1) * detail, (z + 1) * detail) * height * scale;
                    v[index + 17] = (z + 1) * scale;
                }

//                System.out.println("Index: " + index / 18 + " || x, z = " + x + ", " + z);
//                System.out.println("    Triangle 1:");
//                System.out.println("        Vertex 1: [x:" + (v[index     ] - x) + ", z: " + (v[index +  2] - z) + "]");
//                System.out.println("        Vertex 2: [x:" + (v[index +  3] - x) + ", z: " + (v[index +  5] - z) + "]");
//                System.out.println("        Vertex 3: [x:" + (v[index +  6] - x) + ", z: " + (v[index +  8] - z) + "]");
//                System.out.println("    Triangle 2:");
//                System.out.println("        Vertex 1: [x:" + (v[index +  9] - x) + ", z: " + (v[index + 11] - z) + "]");
//                System.out.println("        Vertex 2: [x:" + (v[index + 12] - x) + ", z: " + (v[index + 14] - z) + "]");
//                System.out.println("        Vertex 3: [x:" + (v[index + 15] - x) + ", z: " + (v[index + 17] - z) + "]");
            }
        }

//        for (int a = 0; a < v.length / 3; a++)
//            System.out.println(a + ": " + v[a]);
//            System.out.println((a * 3) + ", [" + ((a / 6) % size) + ", " + ((a / 6) / size) + "]: " + v[a] + " " + v[a + 1] + " " + v[a + 2]);

        // Normals
        for (int z = 0; z < size; z++) {
            for (int x = 0; x < size; x++) {
                int index = (x + size * z) * 18;

                // Triangle 1
                {
                    Vector3f U = new Vector3f(v[index + 3], v[index + 4], v[index + 5])
                            .sub(new Vector3f(v[index    ], v[index + 1], v[index + 2]));
                    Vector3f V = new Vector3f(v[index + 6], v[index + 7], v[index + 8])
                            .sub(new Vector3f(v[index    ], v[index + 1], v[index + 2]));

                    Vector3f normal = new Vector3f(U.y * V.z - U.z * V.y, U.z * V.x - U.x * V.z, U.x * V.y - U.y * V.x).normalize();

//                    n[index    ] = n[index + 3] = n[index + 6] = U.y * V.z - U.z * V.y;
//                    n[index + 1] = n[index + 4] = n[index + 7] = U.z * V.x - U.x * V.z;
//                    n[index + 2] = n[index + 5] = n[index + 8] = U.x * V.y - U.y * V.x;

                    n[index    ] = n[index + 3] = n[index + 6] = normal.x;
                    n[index + 1] = n[index + 4] = n[index + 7] = normal.y;
                    n[index + 2] = n[index + 5] = n[index + 8] = normal.z;
                }

                // Triangle 2
                {
                    Vector3f U = new Vector3f(v[index + 12], v[index + 13], v[index + 14])
                            .sub(new Vector3f(v[index +  9], v[index + 10], v[index + 11]));
                    Vector3f V = new Vector3f(v[index + 15], v[index + 16], v[index + 17])
                            .sub(new Vector3f(v[index +  9], v[index + 10], v[index + 11]));

                    Vector3f normal = new Vector3f(U.y * V.z - U.z * V.y, U.z * V.x - U.x * V.z, U.x * V.y - U.y * V.x).normalize();

//                    n[index +  9] = n[index + 12] = n[index + 15] = U.y * V.z - U.z * V.y;
//                    n[index + 10] = n[index + 13] = n[index + 16] = U.z * V.x - U.x * V.z;
//                    n[index + 11] = n[index + 14] = n[index + 17] = U.x * V.y - U.y * V.x;

                    n[index +  9] = n[index + 12] = n[index + 15] = normal.x;
                    n[index + 10] = n[index + 13] = n[index + 16] = normal.y;
                    n[index + 11] = n[index + 14] = n[index + 17] = normal.z;
                }

//                System.out.println("Index: " + index / 18 + " || x, z = " + x + ", " + z);
//                System.out.println("    Triangle 1:");
//                System.out.println("        Normal 1: [x: " + n[index     ] + ", y: " + n[index +  1] + ", z: " + n[index +  2] + "]");
//                System.out.println("        Normal 2: [x: " + n[index +  3] + ", y: " + n[index +  4] + ", z: " + n[index +  5] + "]");
//                System.out.println("        Normal 3: [x: " + n[index +  6] + ", y: " + n[index +  7] + ", z: " + n[index +  8] + "]");
//                System.out.println("    Triangle 2:");
//                System.out.println("        Normal 1: [x: " + n[index +  9] + ", y: " + n[index + 10] + ", z: " + n[index + 11] + "]");
//                System.out.println("        Normal 2: [x: " + n[index + 12] + ", y: " + n[index + 13] + ", z: " + n[index + 14] + "]");
//                System.out.println("        Normal 3: [x: " + n[index + 15] + ", y: " + n[index + 16] + ", z: " + n[index + 17] + "]");
            }
        }

        // Colors
        for (int z = 0; z < size; z++) {
            for (int x = 0; x < size; x++) {
                int cindex = (x + size * z) * 24;
                int vindex = (x + size * z) * 18;

                c[cindex     ] = color(0, n[vindex +  0]);
                c[cindex +  1] = color(1, n[vindex +  1]);
                c[cindex +  2] = color(2, n[vindex +  2]);
                c[cindex +  3] = 1.0f;

                c[cindex +  4] = color(0, n[vindex +  3]);
                c[cindex +  5] = color(1, n[vindex +  4]);
                c[cindex +  6] = color(2, n[vindex +  5]);
                c[cindex +  7] = 1.0f;

                c[cindex +  8] = color(0, n[vindex +  6]);
                c[cindex +  9] = color(1, n[vindex +  7]);
                c[cindex + 10] = color(2, n[vindex +  8]);
                c[cindex + 11] = 1.0f;

                c[cindex + 12] = color(0, n[vindex +  9]);
                c[cindex + 13] = color(1, n[vindex + 10]);
                c[cindex + 14] = color(2, n[vindex + 11]);
                c[cindex + 15] = 1.0f;

                c[cindex + 16] = color(0, n[vindex + 12]);
                c[cindex + 17] = color(1, n[vindex + 13]);
                c[cindex + 18] = color(2, n[vindex + 14]);
                c[cindex + 19] = 1.0f;

                c[cindex + 20] = color(0, n[vindex + 15]);
                c[cindex + 21] = color(1, n[vindex + 16]);
                c[cindex + 22] = color(2, n[vindex + 17]);
                c[cindex + 23] = 1.0f;
            }
        }

//        // Indices
//        for (int a = 0; a < i.length; a++) {
//            i[a] = a;
//        }

        this.mesh = new Mesh(v, n, c, i);
    }

    private static float noise(float x, float z) {
        return stb_perlin_turbulence_noise3(x, 0.0f, z, 2.0f, 0.5f, 3);
    }

    private static float color(int value, float height) {
        float color = noise(height, value * 24);

//        switch (value) {
//            case 0:
//                color = (float)((byte)height ^ 0b01010101);
//                break;
//            case 1:
//                color = (float)((byte)height & 0b10101010);
//                break;
//            case 2:
//                color = noise(height, value * 24);
//        }

        return color;
    }

    public void render() {
        this.mesh.render();
    }

    public void cleanup() {
        this.mesh.cleanup();
    }
}
