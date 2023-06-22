package ru.vizzi.Utils.shader;

import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;
import ru.vizzi.Utils.resouces.CoreAPI;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL20.*;

public class ShaderLoader {
	public static int loc_in_position = 4;
	public static int loc_in_textureCoords = 5;
	public static int loc_in_normal = 6;
	public static int loc_in_tangent = 11;
	
	static void loadShader(ShaderProgram shader) {
		try {
			loadShaderVF(shader);
		} catch (Throwable throwable) {
			throwable.printStackTrace();
		}
	}

	private static void loadShaderVF(ShaderProgram shader) {
		int[] programs = new int[3];
		int program = glCreateProgram();
		int vertexProgram = glCreateShader(GL_VERTEX_SHADER);
		int fragmentProgram = glCreateShader(GL_FRAGMENT_SHADER);

		StringBuilder vertexShaderSource = loadShaderSrc(shader.getResVs());
		StringBuilder fragmentShaderSource = loadShaderSrc(shader.getResFs());

		compileShader(shader.getResVs().getResourcePath(), vertexProgram, vertexShaderSource);
		compileShader(shader.getResFs().getResourcePath(), fragmentProgram, fragmentShaderSource);

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

		linkShader(shader.getResVs().getResourcePath(), program);
	}

	private static StringBuilder loadShaderSrc(ResourceLocation loc) {
		StringBuilder shaderSource = new StringBuilder();
		BufferedReader reader = null;

		try {
			InputStream is = CoreAPI.getInputStreamFromZip(loc);
			if(is == null) throw new IOException("InputStream is null!");
			reader = new BufferedReader(new InputStreamReader(is));

			String line;

			while ((line = reader.readLine()) != null) {
				shaderSource.append(line).append('\n');
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return shaderSource;
	}

	private static void linkShader(String name, int program) {
		glLinkProgram(program);
		if (glGetProgrami(program, GL_LINK_STATUS) == GL_FALSE) {
			String s = glGetProgramInfoLog(program, 500);
			System.err.println(name + " shader link error: " + s);
		}

		glValidateProgram(program);
		if (glGetProgrami(program, GL_VALIDATE_STATUS) == GL_FALSE) {
			String s = glGetProgramInfoLog(program, 500);
			System.err.println(name + " shader validating error: " + s);
		}
	}

	private static void compileShader(String name, int program, StringBuilder src) {
		glShaderSource(program, src);
		glCompileShader(program);

		if (glGetShaderi(program, GL_COMPILE_STATUS) == GL_FALSE) {
			String s = glGetShaderInfoLog(program, 500);
			System.err.println(name + " shader compile error: " + s);
		}
	}
}
