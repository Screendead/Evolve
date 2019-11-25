package com.screendead.evolve.data;

import org.lwjgl.system.MemoryUtil;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

public class Mesh {
    private static ArrayList<Integer> vboList = new ArrayList<>();
    private final int vao, vertexCount;

    public Mesh(float[] vertices, float[] normals, float[] colors, int[] indices) {
        FloatBuffer vertBuffer = null, normsBuffer = null, colorBuffer = null;
//        IntBuffer indicesBuffer = null;
        try {
            vertexCount = indices.length;
            vboList = new ArrayList<>();

            vao = glGenVertexArrays();
            glBindVertexArray(vao);

            // Position VBO
            int vbo = glGenBuffers();
            vboList.add(vbo);
            vertBuffer = MemoryUtil.memAllocFloat(vertices.length);
            vertBuffer.put(vertices);
            vertBuffer.flip();
            glBindBuffer(GL_ARRAY_BUFFER, vbo);
            glBufferData(GL_ARRAY_BUFFER, vertBuffer, GL_STATIC_DRAW);
            glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);

            // Normals VBO
            vbo = glGenBuffers();
            vboList.add(vbo);
            normsBuffer = MemoryUtil.memAllocFloat(normals.length);
            normsBuffer.put(normals);
            normsBuffer.flip();
            glBindBuffer(GL_ARRAY_BUFFER, vbo);
            glBufferData(GL_ARRAY_BUFFER, normsBuffer, GL_STATIC_DRAW);
            glVertexAttribPointer(1, 3, GL_FLOAT, true, 0, 0);

            // Colors VBO
            vbo = glGenBuffers();
            vboList.add(vbo);
            colorBuffer = MemoryUtil.memAllocFloat(colors.length);
            colorBuffer.put(colors);
            colorBuffer.flip();
            glBindBuffer(GL_ARRAY_BUFFER, vbo);
            glBufferData(GL_ARRAY_BUFFER, colorBuffer, GL_STATIC_DRAW);
            glVertexAttribPointer(2, 4, GL_FLOAT, true, 0, 0);
            glBindBuffer(GL_ARRAY_BUFFER, 0);

            // Index VBO
//            vbo = glGenBuffers();
//            vboList.add(vbo);
//            indicesBuffer = MemoryUtil.memAllocInt(indices.length);
//            indicesBuffer.put(indices);
//            indicesBuffer.flip();
//            glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, vbo);
//            glBufferData(GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL_STATIC_DRAW);
//            glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
            glBindVertexArray(0);
        } finally {
            if (vertBuffer != null) MemoryUtil.memFree(vertBuffer);
            if (normsBuffer != null) MemoryUtil.memFree(normsBuffer);
            if (colorBuffer != null) MemoryUtil.memFree(colorBuffer);
//            if (indicesBuffer != null) MemoryUtil.memFree(indicesBuffer);
        }
    }

    /**
     * Render this mesh to the framebuffer
     */
    public void render() {
        // Draw the mesh
        glBindVertexArray(vao);
//        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, vboList.get(3));
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);
        glEnableVertexAttribArray(2);

//        glDrawElements(GL_TRIANGLES, vertexCount, GL_UNSIGNED_INT, 0);
        glDrawArrays(GL_TRIANGLES, 0, vertexCount);

        // Restore state
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glDisableVertexAttribArray(2);
//        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
        glBindVertexArray(0);
    }

    /**
     * Clean the memory after removal
     */
    public void cleanup() {
        glDisableVertexAttribArray(0);

        // Delete the VBOs
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        for (int vboId : vboList) {
            glDeleteBuffers(vboId);
        }

        // Delete the VAO
        glBindVertexArray(0);
        glDeleteVertexArrays(vao);
    }

    /**
     * @return vao The vertex array object
     */
    public int getVAO() {
        return vao;
    }

    /**
     * @return vertexCount The number of vertices in the mesh
     */
    public int getVertexCount() {
        return vertexCount;
    }
}
