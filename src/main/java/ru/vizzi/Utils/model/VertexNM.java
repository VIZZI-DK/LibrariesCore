package ru.vizzi.Utils.model;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.util.vector.Vector3f;

public class VertexNM {

    private static final int NO_INDEX = -1;

    private Vector3f position;
    private int textureIndex = NO_INDEX;
    private int normalIndex = NO_INDEX;
    private VertexNM duplicateVertex = null;
    private int index;
    private List<Vector3f> tangents = new ArrayList<>();
    private Vector3f averagedTangent = new Vector3f(0, 0, 0);

    VertexNM(int index, Vector3f position) {
        this.index = index;
        this.position = position;
    }

    void addTangent(Vector3f tangent) {
        tangents.add(tangent);
    }

    void averageTangents() {
        if (tangents.isEmpty()) {
            return;
        }
        for (Vector3f tangent : tangents) {
            Vector3f.add(averagedTangent, tangent, averagedTangent);
        }
        if (averagedTangent.length() > 0)
            averagedTangent.normalise();
    }

    Vector3f getAverageTangent() {
        return averagedTangent;
    }

    public int getIndex() {
        return index;
    }

    public boolean isSet() {
        return textureIndex != NO_INDEX && normalIndex != NO_INDEX;
    }

    boolean hasSameTextureAndNormal(int textureIndexOther, int normalIndexOther) {
        return textureIndexOther == textureIndex && normalIndexOther == normalIndex;
    }

    void setTextureIndex(int textureIndex) {
        this.textureIndex = textureIndex;
    }

    void setNormalIndex(int normalIndex) {
        this.normalIndex = normalIndex;
    }

    public Vector3f getPosition() {
        return position;
    }

    int getTextureIndex() {
        return textureIndex;
    }

    int getNormalIndex() {
        return normalIndex;
    }

    VertexNM getDuplicateVertex() {
        return duplicateVertex;
    }

    void setDuplicateVertex(VertexNM duplicateVertex) {
        this.duplicateVertex = duplicateVertex;
    }

}

