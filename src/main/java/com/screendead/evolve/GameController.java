package com.screendead.evolve;

import com.screendead.evolve.graphics.Window;

import static org.lwjgl.glfw.GLFW.glfwPollEvents;
import static org.lwjgl.glfw.GLFW.glfwWindowShouldClose;

public class GameController {
    private Window window;

    public GameController() {
        window = new Window("EVOLVE", 800, 600, true, true);
    }

    public void start() {
        long initialTime = System.nanoTime();
        final float timeU = 1000000000.0f / 60.0f;
        final float timeF = 1000000000.0f / 60.0f;
        float deltaU = 0, deltaF = 0;
        int frames = 0, ticks = 0, totalTicks = 0;
        long timer = System.currentTimeMillis();

        while (!glfwWindowShouldClose(window.getHandle())) {
            long currentTime = System.nanoTime();
            deltaU += (currentTime - initialTime) / timeU;
            deltaF += (currentTime - initialTime) / timeF;
            initialTime = currentTime;

            if (deltaU >= 1) {
                glfwPollEvents();
                window.update();
                ticks++;
                totalTicks++;
                deltaU--;
            }

            if (deltaF >= 1) {
                window.render();
                frames++;
                deltaF--;
            }

            if (System.currentTimeMillis() - timer > 1000) {
                System.out.println(String.format("UPS: %s, FPS: %s", ticks, frames));
                frames = 0;
                ticks = 0;
                timer += 1000;
            }
        }
    }

    public void pause() {

    }

    public void stop() {
        window.destroy();
    }
}
