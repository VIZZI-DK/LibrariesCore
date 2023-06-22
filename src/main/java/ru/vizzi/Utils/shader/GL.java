package ru.vizzi.Utils.shader;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.util.vector.Vector4f;
import ru.vizzi.Utils.shader.pbr.IPBR;

import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL11.GL_MODELVIEW;
import static org.lwjgl.opengl.GL11.glColor4f;

public class GL {

    private static final Vector4f lastColor = new Vector4f(1f, 1f, 1f, 1f);
    private static final FloatBuffer lightMapTextureMatrix = BufferUtils.createFloatBuffer(16);

    public static void color(float r, float g, float b, float a) {
        glColor4f(r, g, b, a);
        ShaderProgram currentShader = KrogenitShaders.getCurrentShader();
        if(currentShader instanceof IPBR) {
            ((IPBR) currentShader).setColor(r, g, b, a);
        }
//        else {
//            if(lastColor.x == r && lastColor.y == g && lastColor.z == b && lastColor.w == a) return;

//        }

        lastColor.x = r; lastColor.y = g; lastColor.z = b; lastColor.w = a;
    }

    public static void color(float r, float g, float b) {
        color(r, g, b, lastColor.w);
    }

    public static FloatBuffer getLightMapTextureMatrix() {
        GL13.glActiveTexture(GL13.GL_TEXTURE1);
        GL11.glMatrixMode(GL11.GL_TEXTURE);
        GL11.glLoadIdentity();
        float f = 0.00390625F;
        GL11.glScalef(f, f, f);
        GL11.glTranslatef(8.0F, 8.0F, 8.0F);
        GL11.glGetFloat(GL11.GL_TEXTURE_MATRIX, (FloatBuffer) lightMapTextureMatrix.position(0));
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glMatrixMode(GL_MODELVIEW);
        return lightMapTextureMatrix;
    }
}
