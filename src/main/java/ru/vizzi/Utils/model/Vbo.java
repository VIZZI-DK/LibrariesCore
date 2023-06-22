package ru.vizzi.Utils.model;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;

public class Vbo {

    private final int vboId;
    private final int type;
    private int attribute;

    private Vbo(int vboId, int type) {
        this.vboId = vboId;
        this.type = type;
    }

    public static Vbo create(int type) {
        int id = GL15.glGenBuffers();
        return new Vbo(id, type);
    }

    public void bind() {
        GL15.glBindBuffer(type, vboId);
    }

    public void unbind() {
        GL15.glBindBuffer(type, 0);
    }

    void storeData(float[] data) {
        FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
        buffer.put(data);
        buffer.flip();
        storeData(buffer);
    }

    void storeData(int[] data) {
        IntBuffer buffer = BufferUtils.createIntBuffer(data.length);
        buffer.put(data);
        buffer.flip();
        storeData(buffer);
    }

    private void storeData(IntBuffer data) {
        GL15.glBufferData(type, data, GL15.GL_STATIC_DRAW);
    }

    private void storeData(FloatBuffer data) {
        GL15.glBufferData(type, data, GL15.GL_STATIC_DRAW);
    }

    public void clear() {
        GL15.glDeleteBuffers(vboId);
    }

    void vertexAttribPointer(int attribute, int attrSize, int BYTES_PER_FLOAT) {
        this.attribute = attribute;
        GL20.glVertexAttribPointer(attribute, attrSize, GL11.GL_FLOAT, false, attrSize * BYTES_PER_FLOAT, 0);
    }

    public int getAttribute() {
        return this.attribute;
    }

}
