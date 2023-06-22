package ru.vizzi.Utils.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.glu.GLU;

public class Vao {

    private static final int BYTES_PER_FLOAT = 4;
    private static final int BYTES_PER_INT = 4;
    public final int id;
    private List<Vbo> dataVbos = new ArrayList<>();
    private Vbo indexVbo;
    private int indexCount;

    public static Vao create() {
        int id = GL30.glGenVertexArrays();
        return new Vao(id);
    }

    private Vao(int id) {
        this.id = id;
    }

    int getIndexCount() {
        return indexCount;
    }

    public void bindAttribs(int... ids) {
        bind();
        for (int id : ids) {
            GL20.glEnableVertexAttribArray(this.dataVbos.get(id).getAttribute());
        }
    }

    public void unbindAttribs(int... ids) {
        for (int id : ids) {
            GL20.glDisableVertexAttribArray(this.dataVbos.get(id).getAttribute());
        }
        unbind();
    }

    void bindAttribs() {
        bind();

        for (Vbo j : this.dataVbos) {
            GL20.glEnableVertexAttribArray(j.getAttribute());
        }
    }

    void unbindAttribs() {
        for (Vbo i : dataVbos) {
            GL20.glDisableVertexAttribArray(i.getAttribute());
        }
        unbind();
    }

    void createIndexBuffer(int[] indices) {
        this.indexVbo = Vbo.create(GL15.GL_ELEMENT_ARRAY_BUFFER);
        indexVbo.bind();
        indexVbo.storeData(indices);
        this.indexCount = indices.length;
    }

    public void createAttribute(int attribute, int[] data, int attrSize) {
        float[] dataConverted = new float[data.length];
        for (int i = 0; i < data.length; i++) {
            dataConverted[i] = data[i];
        }
        Vbo dataVbo = Vbo.create(GL15.GL_ARRAY_BUFFER);
        dataVbo.bind();
        dataVbo.storeData(dataConverted);
        dataVbo.vertexAttribPointer(attribute, attrSize, BYTES_PER_FLOAT);
        dataVbo.unbind();
        this.dataVbos.add(dataVbo);
    }

    void createAttribute(int attribute, float[] data, int attrSize) {
        Vbo dataVbo = Vbo.create(GL15.GL_ARRAY_BUFFER);
        dataVbo.bind();
        dataVbo.storeData(data);
        dataVbo.vertexAttribPointer(attribute, attrSize, BYTES_PER_FLOAT);
        dataVbo.unbind();
        this.dataVbos.add(dataVbo);
    }

    public void createIntAttribute(int attribute, int[] data, int attrSize) {
        Vbo dataVbo = Vbo.create(GL15.GL_ARRAY_BUFFER);
        dataVbo.bind();
        dataVbo.storeData(data);
        dataVbo.vertexAttribPointer(attribute, attrSize, BYTES_PER_INT);
        dataVbo.unbind();
        this.dataVbos.add(dataVbo);
    }

    public void clear() {
        GL30.glDeleteVertexArrays(id);
        for (Vbo vbo : dataVbos) {
            vbo.clear();
        }

        indexVbo.clear();
    }

    public void bind() {
        GL30.glBindVertexArray(id);
    }

    public void unbind() {
        GL30.glBindVertexArray(0);
    }

}
