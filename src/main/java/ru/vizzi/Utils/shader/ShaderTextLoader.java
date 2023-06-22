package ru.vizzi.Utils.shader;

import cpw.mods.fml.common.FMLCommonHandler;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL20.*;

public class ShaderTextLoader {
    static void loadShader(ShaderProgram shader, String vs, String fs) {
        loadShaderVF(shader, vs, fs);
    }

    private static void loadShaderVF(ShaderProgram shader, String vs, String fs) {
        int[] programs = new int[3];
        int program = glCreateProgram();
        int vertexProgram = glCreateShader(GL_VERTEX_SHADER);
        int fragmentProgram = glCreateShader(GL_FRAGMENT_SHADER);

        StringBuilder vertexShaderSource = loadShaderSrc(vs);
        StringBuilder fragmentShaderSource = loadShaderSrc(fs);

        compileShader(vs.substring(0, 20), vertexProgram, vertexShaderSource);
        compileShader(fs.substring(0, 20), fragmentProgram, fragmentShaderSource);

        programs[0] = program;
        programs[1] = vertexProgram;
        programs[2] = fragmentProgram;

        glAttachShader(program, vertexProgram);
        glAttachShader(program, fragmentProgram);

        shader.setShaderPrograms(programs);
        shader.bindAttribLocations();

        int i = GL11.glGetError();

        if (i != 0) {
            String s1 = GLU.gluErrorString(i);
            System.err.println("########## GL ERROR ##########");
            System.err.println("@ Attrib locations " + shader.getResVs().getResourcePath());
            System.err.println(i + ": " + s1);
        }

        linkShader(vs.substring(0, 20), program);
    }

    private static StringBuilder loadShaderSrc(String src) {
        return new StringBuilder(src);
    }

    private static void linkShader(String name, int program) {
        glLinkProgram(program);
        if (glGetProgrami(program, GL_LINK_STATUS) == GL_FALSE) {
            String s = glGetProgramInfoLog(program, 500);
            System.err.println(name + " shader link error: " + s);
            FMLCommonHandler.instance().exitJava(0, false);
        }

        glValidateProgram(program);
        if (glGetProgrami(program, GL_VALIDATE_STATUS) == GL_FALSE) {
            String s = glGetProgramInfoLog(program, 500);
            System.err.println(name + " shader validating error: " + s);
            FMLCommonHandler.instance().exitJava(0, false);
        }
    }

    private static void compileShader(String name, int program, StringBuilder src) {
        glShaderSource(program, src);
        glCompileShader(program);

        if (glGetShaderi(program, GL_COMPILE_STATUS) == GL_FALSE) {
            String s = glGetShaderInfoLog(program, 500);
            System.err.println(name + " shader compile error: " + s);
            FMLCommonHandler.instance().exitJava(0, false);
        }
    }
}
