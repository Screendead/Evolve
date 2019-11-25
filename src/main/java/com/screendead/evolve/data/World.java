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
                int index = (x + size * z) * 24;

                c[index     ] = 1.0f;
                c[index +  1] = 1.0f;
                c[index +  2] = 1.0f;
                c[index +  3] = 1.0f;

                c[index +  4] = 1.0f;
                c[index +  5] = 1.0f;
                c[index +  6] = 1.0f;
                c[index +  7] = 1.0f;

                c[index +  8] = 1.0f;
                c[index +  9] = 1.0f;
                c[index + 10] = 1.0f;
                c[index + 11] = 1.0f;

                c[index + 12] = 1.0f;
                c[index + 13] = 1.0f;
                c[index + 14] = 1.0f;
                c[index + 15] = 1.0f;

                c[index + 16] = 1.0f;
                c[index + 17] = 1.0f;
                c[index + 18] = 1.0f;
                c[index + 19] = 1.0f;

                c[index + 20] = 1.0f;
                c[index + 21] = 1.0f;
                c[index + 22] = 1.0f;
                c[index + 23] = 1.0f;
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

    public void render() {
        this.mesh.render();
    }

    public void cleanup() {
        this.mesh.cleanup();
    }
}
