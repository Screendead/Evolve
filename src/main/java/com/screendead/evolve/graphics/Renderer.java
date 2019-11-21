package com.screendead.evolve.graphics;

import com.screendead.evolve.data.Mesh;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL;
import org.lwjgl.stb.STBPerlin;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.GL_MULTISAMPLE;
import static org.lwjgl.opengl.GL43.GL_DEBUG_OUTPUT;

public class Renderer {
    private Shader shader;
    private Mesh world;
    private Matrix4f view = new Matrix4f().identity(), transform = new Matrix4f().identity();
    private float fov = 90.0f;

    /**
     * Render to the framebuffer
     */
    public void render(Camera camera) {
        // Clear the framebuffer
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
//        glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);

//        glColor4f(255.0f, 255.0f, 255.0f, 0.5f);
//        glBegin(GL_TRIANGLE_STRIP);
//            glVertex3f(-0.5f, -0.5f, 0.0f);
//            glVertex3f(0.5f, -0.5f, 0.0f);
//            glVertex3f(-0.5f, 0.5f, 0.0f);
//            glVertex3f(-0.5f, 0.5f, 0.0f);
//            glVertex3f(0.5f, -0.5f, 0.0f);
//            glVertex3f(0.5f, 0.5f, 0.0f);
//        glEnd();

        shader.bind();
            shader.setUniform("camera", camera.getMatrix());
            world.render();
        Shader.unbind();
    }

    /**
     * Initialise OpenGL context for use with this window
     */
    public void init() {
        // This line is critical for LWJGL's interoperation with GLFW's
        // OpenGL context, or any context that is managed externally.
        // LWJGL detects the context that is current in the current thread,
        // creates the GLCapabilities instance and makes the OpenGL
        // bindings available for use.
        GL.createCapabilities();

        glEnable(GL_DEPTH_TEST);
        glEnable(GL_DEBUG_OUTPUT);
//        glEnable(GL_CULL_FACE);
        glEnable(GL_BLEND);
        glEnable(GL_MULTISAMPLE);

        // OpenGL settings
//        glCullFace(GL_BACK);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        {
            int size = 1024;
            float scale = 0.1f;
            float height = 0.01f;
            float detail = 0.005f;

            float[] v, n, c;
            int[] i;

            v = new float[3 * size * size];
            n = new float[3 * size * size];
            c = new float[4 * size * size];
            i = new int[6 * size * size];

            // Vertices
            for (int z = 0; z < size; z++) {
                for (int x = 0; x < size; x++) {
                    v[(z * size + x) * 3]     = x * scale;
                    v[(z * size + x) * 3 + 1] = STBPerlin.stb_perlin_noise3(x * detail, 0.0f, z * detail, 0, 0, 0) * size * height;
                    v[(z * size + x) * 3 + 2] = z * scale;
                }
            }

            // Normals
            for (int a = 0; a < v.length; a++) {
                n[a] = 0.0f;
            }

            // Colors
            for (int z = 0; z < size; z++) {
                for (int x = 0; x < size; x++) {
                    c[(z * size + x) * 4] = 0.0f;
                    c[(z * size + x) * 4 + 1] = 0.0f;
                    c[(z * size + x) * 4 + 2] = 255.0f;
                    c[(z * size + x) * 4 + 3] = 1.0f;
                }
            }

            // Indices
            for (int z = 0; z < size - 1; z++) {
                for (int x = 0; x < size - 1; x++) {
                    i[(z * size + x) * 6] = z * size + x;
                    i[(z * size + x) * 6 + 1] = (z + 1) * size + x;
                    i[(z * size + x) * 6 + 2] = z * size + x + 1;

                    i[(z * size + x) * 6 + 3] = z * size + x + 1;
                    i[(z * size + x) * 6 + 4] = (z + 1) * size + x;
                    i[(z * size + x) * 6 + 5] = (z + 1) * size + x + 1;
                }
            }

            // Normals
//            for (int a = 0; a < size * size; a++) {
//                Vector3f P1 = new Vector3f(v[i[a * 6]], v[i[a * 6] + 1], v[i[a * 6] + 2]);
//                Vector3f P2 = new Vector3f(v[i[a * 6]], v[i[a * 6]], v[i[a * 6]]);
//                Vector3f P3 = new Vector3f();
//
//                Vector3f V = P1.sub(P2);
//                Vector3f W = P1.sub(P3);
//
//                n[a * 3]     = normal.x;
//                n[a * 3 + 1] = normal.y;
//                n[a * 3 + 2] = normal.z;
//            }

            world = new Mesh(v, n, c, i);
        }

//        world = new Mesh(new float[] {
//                0.0f, 0.0f, 0.0f,
//                1.0f, 0.0f, 0.0f,
//                0.0f, 0.0f, 1.0f,
//                1.0f, 0.0f, 1.0f,
//        }, new float[] {
//                0.0f, 1.0f, 0.0f,
//                0.0f, 1.0f, 0.0f,
//                0.0f, 1.0f, 0.0f,
//                0.0f, 1.0f, 0.0f,
//        }, new float[] {
//                0.0f, 0.0f, 0.0f, 1.0f,
//                0.0f, 0.0f, 0.0f, 1.0f,
//                0.0f, 0.0f, 0.0f, 1.0f,
//                0.0f, 0.0f, 0.0f, 1.0f,
//        }, new int[] {
//                0, 1, 2, 2, 1, 3,
//        });

        // Create texture and shader
        shader = new Shader("basic");
        shader.addUniform("view");
        shader.addUniform("transform");
        shader.addUniform("camera");

        // Set the clear color
//        glClearColor(0.5f, 0.5f, 1.0f, 1.0f);
        glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
    }

    /**
     * Set the OpenGL viewport transformation and update the viewMatrix
     * @param width The window width
     * @param height The window height
     */
    public void setViewport(float width, float height) {
        // Set the viewport
        glViewport(0, 0, (int) width, (int) height);

        // Set the viewMatrix
        view = new Matrix4f();
        view.perspective((float) Math.toRadians(fov),
                width / height, 0.01f, 65536.0f);

        // Update the viewMatrix in the shader
        shader.bind();
            shader.setUniform("view", view);
        Shader.unbind();
    }

    /**
     * Set the transformation matrix for the shader
     * Rotation order is YXZ
     * @param dx X component of the translation
     * @param dy Y component of the translation
     * @param dz Z component of the translation
     * @param rx Degrees of rotation about the X axis
     * @param ry Degrees of rotation about the Y axis
     * @param rz Degrees of rotation about the Z axis
     * @param sx X component of the scale
     * @param sy Y component of the scale
     * @param sz Z component of the scale
     */
    public void setTransform(float dx, float dy, float dz, float rx, float ry, float rz, float sx, float sy, float sz) {
        shader.bind();
            shader.setUniform("transform", transform = new Matrix4f().translation(dx, dy, dz)
                    .rotateYXZ((float) Math.toRadians(ry), (float) Math.toRadians(rx), (float) Math.toRadians(rz))
                    .scale(sx, sy, sz));
        Shader.unbind();
    }

    public void cleanup() {
        world.cleanup();
    }
}
