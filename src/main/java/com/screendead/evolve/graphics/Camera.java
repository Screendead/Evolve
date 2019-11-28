package com.screendead.evolve.graphics;

import org.joml.Matrix4f;
import org.joml.Vector3f;

class Camera {
    private final Vector3f up = new Vector3f(0, 1, 0);
    private final Vector3f right = new Vector3f();
    private final Vector3f pos;
    private final Vector3f vel;
    private final Vector3f acc;
    private Vector3f look;
    private float horizontal = 0;
    private float vertical = 0;
    private Matrix4f lookMatrix;

    Camera(Vector3f pos, Vector3f look) {
        this.pos = pos;
        this.vel = new Vector3f();
        this.acc = new Vector3f();

        this.look = look.normalize();

        up.cross(look, right);
        right.normalize();

        update(0, 0);
    }

    void update(float dx, float dy) {
        horizontal += -dx / 12.0f;
        vertical += (dy * 2) / 12.0f;

        horizontal = horizontal % 360.0f;
        vertical = constrain(vertical);

        this.look = new Vector3f(0, 0, 1).rotateAxis((float) Math.toRadians(horizontal), up.x, up.y, up.z);

        up.cross(look, right);
        right.normalize();

        this.look.rotateAxis((float) Math.toRadians(vertical), right.x, right.y, right.z);

        vel.add(acc);
        vel.mul(0.9f);
        pos.add(vel);
        acc.zero();

        lookMatrix = new Matrix4f().lookAt(this.pos, this.pos.add(this.look, new Vector3f()), this.up);
    }

    void move(int walk, int fly, int strafe) {
        acc.add(new Vector3f(look.x, 0, look.z).normalize().mul((float) walk / 30.0f));
        acc.add(new Vector3f(right.x, right.y, right.z).normalize().mul((float) strafe / 30.0f));
        acc.add(new Vector3f(up.x, up.y, up.z).normalize().mul((float) fly / 30.0f));
    }

    private float constrain(float f) {
        return Math.min(Math.max(f, -89.99f), 89.99f);
    }

    Matrix4f getMatrix() {
        return lookMatrix;
    }

    Vector3f getPos() {
        return pos;
    }
}
