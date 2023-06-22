package ru.vizzi.Utils.shader;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import javax.vecmath.Vector4f;
import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL20.*;

public abstract class ShaderProgram {
    protected static final Minecraft mc = Minecraft.getMinecraft();
    protected static final FloatBuffer matrix = BufferUtils.createFloatBuffer(16);

    private int[] programs;
    protected int shaderProgram;

    private ResourceLocation resVs, resFs;

    public ShaderProgram(ResourceLocation resVs, ResourceLocation resFs) {
        this.resVs = resVs;
        this.resFs = resFs;
        ShaderLoader.loadShader(this);
        getAllUniformLocations();
    }

    public ShaderProgram(String vs, String fs) {
        ShaderTextLoader.loadShader(this, vs, fs);
        getAllUniformLocations();
    }

    public void preInit() {
        enable();
        init();
        int i = GL11.glGetError();

        if (i != 0) {
            String s1 = GLU.gluErrorString(i);
            System.err.println("########## GL ERROR ##########");
            System.err.println("@ Init shader " + resVs.getResourcePath());
            System.err.println(i + ": " + s1);
        }

        disable();
    }


    void setShaderPrograms(int[] programs) {
        this.shaderProgram = programs[0];
        this.programs = programs;
    }

    public abstract void bindAttribLocations();

    protected abstract void getAllUniformLocations();

    protected abstract void init();

    protected void bindAttribute(int attribute, String variableName) {
        glBindAttribLocation(shaderProgram, attribute, variableName);
    }

    protected int getUniformLocation(String uniformName) {
        return glGetUniformLocation(shaderProgram, uniformName);
    }

    protected void setBoolean(int location, boolean value) {
        glUniform1f(location, value ? 1 : 0);
    }

    protected void setInt(int location, int value) {
        glUniform1i(location, value);
    }

    protected void setFloat(int location, float value) {
        glUniform1f(location, value);
    }

    protected void setVector(int location, Vector2f vector) {
        glUniform2f(location, vector.x, vector.y);
    }

    protected void setVector(int location, Vector3f vector) {
        glUniform3f(location, vector.x, vector.y, vector.z);
    }

    protected void setVector(int location, Vector4f vector) {
        glUniform4f(location, vector.x, vector.y, vector.z, vector.w);
    }

    protected void setVector(int location, float x, float y, float z, float w) {
        glUniform4f(location, x, y, z, w);
    }

    protected void setVector(int location, float x, float y, float z) {
        glUniform3f(location, x, y, z);
    }

    protected void setVector(int location, float x, float y) {
        glUniform2f(location, x, y);
    }

    protected void setMatrix(int location, FloatBuffer matrix) {
        glUniformMatrix4(location, false, matrix);
    }

    public int[] getPrograms() {
        return programs;
    }

    ResourceLocation getResFs() {
        return resFs;
    }

    ResourceLocation getResVs() {
        return resVs;
    }

    public void enable() {
        if(KrogenitShaders.getCurrentShader() != this) {
            glUseProgram(shaderProgram);
            KrogenitShaders.setCurrentShader(this);
        }
    }

    public void disable() {
        if(KrogenitShaders.getCurrentShader() != null) {
            glUseProgram(0);
            KrogenitShaders.setCurrentShader(null);
        }
    }

    public boolean isActive() {
        return KrogenitShaders.getCurrentShader() == this;
    }

    public void clear() {
        disable();

        for(int i=0;i<programs.length-1;i++) {
            glDetachShader(shaderProgram, programs[i + 1]);
            glDeleteShader(programs[i + 1]);
        }

        glDeleteProgram(shaderProgram);
    }
}
