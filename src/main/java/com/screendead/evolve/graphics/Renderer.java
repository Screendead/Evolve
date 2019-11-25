package com.screendead.evolve.graphics;

import com.screendead.evolve.data.World;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.GL_MULTISAMPLE;
import static org.lwjgl.opengl.GL43.GL_DEBUG_OUTPUT;

class Renderer {
    private Shader shader;
    private World world;

    /**
     * Render to the framebuffer
     */
    void render(Camera camera) {
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
    void init(int size, float scale, float height, float detail) {
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

        world = new World(size, scale, height, detail);

//        {
//            float[] v, n, c;
//            int  [] i;
//
////            v = new float[3 * size * size];
//            v = new float[3 * size * size];
//            n = new float[3 * size * size];
//            c = new float[4 * size * size];
//            i = new int  [6 * size * size];
//
//            // Vertices
//            for (int z = 0; z < size; z++) {
//                for (int x = 0; x < size; x++) {
//                    v[(z * size + x) * 3 + 0] = x * scale;
//                    v[(z * size + x) * 3 + 1] = STBPerlin.stb_perlin_noise3(x * detail, 0.0f, z * detail, 0, 0, 0) * height * scale;
//                    v[(z * size + x) * 3 + 2] = z * scale;
//
//                    n[(z * size + x) * 3 + 0] = 0.0f;
//                    n[(z * size + x) * 3 + 1] = 1.0f;
//                    n[(z * size + x) * 3 + 2] = 0.0f;
//                }
//            }
//
//            // Colors
//            for (int z = 0; z < size; z++) {
//                for (int x = 0; x < size; x++) {
//                    c[(z * size + x) * 4 + 0] = 1.0f;
//                    c[(z * size + x) * 4 + 1] = 1.0f;
//                    c[(z * size + x) * 4 + 2] = 1.0f;
//                    c[(z * size + x) * 4 + 3] = 1.0f;
//                }
//            }
//
//            // Indices
//            for (int z = 0; z < size - 1; z++) {
//                for (int x = 0; x < size - 1; x++) {
//                    int index = (z * size + x) * 6;
//
//                    i[index + 0] = z * size + x;
//                    i[index + 1] = (z + 1) * size + x;
//                    i[index + 2] = z * size + x + 1;
//
//                    i[index + 3] = z * size + x + 1;
//                    i[index + 4] = (z + 1) * size + x;
//                    i[index + 5] = (z + 1) * size + x + 1;
//
//                }
//            }
//
//            world = new Mesh(v, n, c, i);
//        }

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
    void setViewport(float width, float height) {
        // Set the viewport
        glViewport(0, 0, (int) width, (int) height);

        // Set the viewMatrix
        Matrix4f view = new Matrix4f();
        float fov = 90.0f;
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
    void setTransform(float dx, float dy, float dz, float rx, float ry, float rz, float sx, float sy, float sz) {
        shader.bind();
        shader.setUniform("transform", new Matrix4f().translation(dx, dy, dz)
                    .rotateYXZ((float) Math.toRadians(ry), (float) Math.toRadians(rx), (float) Math.toRadians(rz))
                    .scale(sx, sy, sz));
        Shader.unbind();
    }

    void cleanup() {
        world.cleanup();
    }
}
