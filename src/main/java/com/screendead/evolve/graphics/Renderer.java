package com.screendead.evolve.graphics;

import com.screendead.evolve.data.World;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.GL_MULTISAMPLE;
import static org.lwjgl.opengl.GL43.GL_DEBUG_OUTPUT;

class Renderer {
    private Shader shader;
    private World world;
    private final Vector3f sun = new Vector3f(0.0f, -1.0f, 0.0f);
    private Vector3f lamp;

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
        shader.setUniform("viewPos", camera.getPos());
        shader.setUniform("sunPos", sun);
        shader.setUniform("lampPos", lamp);
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

        lamp = new Vector3f(size * scale / 2.0f,
                size * scale / 2.0f,
                size * scale / 2.0f);

        world = new World(size, scale, height, detail);

        // Create shader
        shader = new Shader("phong");
        shader.addUniform("view");
        shader.addUniform("transform");
        shader.addUniform("camera");
        shader.addUniform("viewPos");
        shader.addUniform("sunPos");
        shader.addUniform("lampPos");

        // Set the clear color
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
    @SuppressWarnings("SameParameterValue")
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
