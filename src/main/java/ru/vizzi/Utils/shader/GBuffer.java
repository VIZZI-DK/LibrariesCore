package ru.vizzi.Utils.shader;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.culling.Frustrum;
import net.minecraft.crash.CrashReport;
import net.minecraft.util.ReportedException;
import org.lwjgl.BufferUtils;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.List;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL20.glDrawBuffers;
import static org.lwjgl.opengl.GL30.*;

public class GBuffer {
    private static final Minecraft mc = Minecraft.getMinecraft();
    private int gBuffer, renderBuffer;
    private int gPosition, gNormal, gColor, gLightmap, gEmission;

    public GBuffer() {
        createGBuffer();
    }

    public void bind() {
//        if(mc.gameSettings.lighting.ordinal() > 1) {
//            glBindFramebuffer(GL_FRAMEBUFFER, KrogenitShaders.gBuffer.getGBuffer());
//            glViewport(0, 0, mc.displayWidth, mc.displayHeight);
//            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
//            KrogenitShaders.gBufferShader.enable();
//            KrogenitShaders.gBufferShader.setUseTexture(true);
//            KrogenitShaders.gBufferShader.setLightMapping(true);
//            KrogenitShaders.gBufferShaderOld.enable();
//            KrogenitShaders.gBufferShaderOld.setUseTexture(true);
//            KrogenitShaders.gBufferShaderOld.setLightMapping(true);
//            KrogenitShaders.gBufferPass = true;
//        }
    }

    public int getGBuffer() {
        return gBuffer;
    }

    public int getGColor() {
        return gColor;
    }

    public int getGLightmap() {
        return gLightmap;
    }

    public int getGPosition() {
        return gPosition;
    }

    public int getGNormal() {
        return gNormal;
    }

    public int getGEmission() {
        return gEmission;
    }

    private void unbindGBuffer(Frustrum frustrum) {
        mc.getFramebuffer().bindFramebuffer(false);
        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, KrogenitShaders.gBuffer.getGColor());
        glActiveTexture(GL_TEXTURE2);
        glBindTexture(GL_TEXTURE_2D, KrogenitShaders.gBuffer.getGLightmap());
        glActiveTexture(GL_TEXTURE3);
        glBindTexture(GL_TEXTURE_2D, KrogenitShaders.gBuffer.getGPosition());
        glActiveTexture(GL_TEXTURE4);
        glBindTexture(GL_TEXTURE_2D, KrogenitShaders.gBuffer.getGNormal());
        glActiveTexture(GL_TEXTURE5);
        glBindTexture(GL_TEXTURE_2D, KrogenitShaders.gBuffer.getGEmission());

        KrogenitShaders.gBufferPBRDirectionalShader.enable();
        KrogenitShaders.gBufferPBRDirectionalShader.useDirectionalLight(true);
        KrogenitShaders.gBufferPBRDirectionalShader.setDirectionalValues();
        renderQuad();
        KrogenitShaders.gBufferPBRDirectionalShader.useDirectionalLight(false);
        KrogenitShaders.gBufferPBRDirectionalShader.disable();
        glEnable(GL_BLEND);
        glBlendFunc(GL_ONE, GL_ONE);

        pointLights(frustrum);

        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glDisable(GL_BLEND);


        int nextBuffer = mc.getFramebuffer().framebufferObject;

        glBindFramebuffer(GL_READ_FRAMEBUFFER, KrogenitShaders.gBuffer.getGBuffer());
        glBindFramebuffer(GL_DRAW_FRAMEBUFFER, nextBuffer);

        glBlitFramebuffer(0, 0, mc.displayWidth, mc.displayHeight, 0, 0, mc.displayWidth, mc.displayHeight, GL_DEPTH_BUFFER_BIT, GL_NEAREST);
        glBindFramebuffer(GL_FRAMEBUFFER, nextBuffer);

        //LightManagerClient.getLightsInFrustum(Math.min(mc.gameSettings.renderDistanceChunks * 16.0f, mc.gameSettings.lightingForwardDistance), frustrum);
    }

    private void pointLights(Frustrum frustrum) {
//        float maxDistance = Math.min(mc.gameSettings.renderDistanceChunks * 16, mc.gameSettings.lightingDeferredDistance);
//        List<PointLight> pointLights = LightManagerClient.getLightsInFrustum(maxDistance, frustrum);
//        if(pointLights != null && pointLights.size() > 0) {
//            KrogenitShaders.gBufferPBRPointShader.enable();
//            KrogenitShaders.gBufferPBRPointShader.setSpecularPower(1.0f);
//            KrogenitShaders.gBufferPBRPointShader.setModelView();
//
//            for(PointLight p : pointLights) {
//                KrogenitShaders.gBufferPBRPointShader.setPointLight(p);
//                renderQuad();
//            }
//
//            KrogenitShaders.gBufferPBRPointShader.disable();
//        }
    }

    private void renderQuad() {
        Tessellator t = Tessellator.instance;
        t.startDrawingQuads();
        t.addVertexWithUV(-1, -1, 0, 0.0f, 0.0f);
        t.addVertexWithUV(1f, -1, 0, 1.0f, 0.0f);
        t.addVertexWithUV(1f, 1f, 0, 1.0f, 1.0f);
        t.addVertexWithUV(-1, 1f, 0, 0.0f, 1.0f);
        t.draw();
    }

    public void unbind(Frustrum frustrum) {
        KrogenitShaders.gBufferPass = false;
        if(GameSettings.lighting == EnumShaderLightingType.ALL.ordinal()) {
            unbindGBuffer(frustrum);
        }
    }

    private void createGBuffer() {
        deleteTextures();

        gBuffer = glGenFramebuffers();
        glBindFramebuffer(GL_FRAMEBUFFER, gBuffer);

        gColor = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, gColor);
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA16F, mc.displayWidth, mc.displayHeight, 0, GL_RGBA, GL_FLOAT, (ByteBuffer) null);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
        glFramebufferTexture2D(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, GL_TEXTURE_2D, gColor, 0);

        gNormal = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, gNormal);
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA16F, mc.displayWidth, mc.displayHeight, 0, GL_RGBA, GL_FLOAT, (ByteBuffer) null);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
        glFramebufferTexture2D(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT1, GL_TEXTURE_2D, gNormal, 0);

        gPosition = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, gPosition);
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA16F, mc.displayWidth, mc.displayHeight, 0, GL_RGBA, GL_FLOAT, (ByteBuffer) null);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
        glFramebufferTexture2D(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT2, GL_TEXTURE_2D, gPosition, 0);

        gLightmap = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, gLightmap);
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA16F, mc.displayWidth, mc.displayHeight, 0, GL_RGBA, GL_FLOAT, (ByteBuffer) null);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
        glFramebufferTexture2D(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT3, GL_TEXTURE_2D, gLightmap, 0);

        gEmission = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, gEmission);
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB16F, mc.displayWidth, mc.displayHeight, 0, GL_RGB, GL_FLOAT, (ByteBuffer) null);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
        glFramebufferTexture2D(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT4, GL_TEXTURE_2D, gEmission, 0);

        int[] attachments = new int[] { GL_COLOR_ATTACHMENT0,
                GL_COLOR_ATTACHMENT1, GL_COLOR_ATTACHMENT2,
                GL_COLOR_ATTACHMENT3, GL_COLOR_ATTACHMENT4
        };
        IntBuffer intBuffer = BufferUtils.createIntBuffer(attachments.length);
        intBuffer.put(attachments);
        intBuffer.flip();
        glDrawBuffers(intBuffer);

        if (glCheckFramebufferStatus(GL_FRAMEBUFFER) != GL_FRAMEBUFFER_COMPLETE) {
            Throwable t = new Throwable("MRT couldn't create!");
            CrashReport crashreport = CrashReport.makeCrashReport(t, "Shader compile");
            throw new ReportedException(crashreport);
        }

        createRenderBuffer();
        glBindFramebuffer(GL_FRAMEBUFFER, 0);
    }

    private void createRenderBuffer() {
        glDeleteRenderbuffers(renderBuffer);

        renderBuffer = glGenRenderbuffers();
        glBindRenderbuffer(GL_RENDERBUFFER, renderBuffer);
        glRenderbufferStorage(GL_RENDERBUFFER, GL_DEPTH_COMPONENT, mc.displayWidth, mc.displayHeight);
        glFramebufferRenderbuffer(GL_FRAMEBUFFER, GL_DEPTH_ATTACHMENT, GL_RENDERBUFFER, renderBuffer);

        if (glCheckFramebufferStatus(GL_FRAMEBUFFER) != GL_FRAMEBUFFER_COMPLETE) {
            Throwable t = new Throwable("Render buffer couldn't create!");
            CrashReport crashreport = CrashReport.makeCrashReport(t, "Shader compile");
            throw new ReportedException(crashreport);
        }
    }

    public void resize() {
        createGBuffer();
    }

    private void deleteTextures() {
        glDeleteFramebuffers(gBuffer);
        glDeleteTextures(gPosition);
        glDeleteTextures(gNormal);
        glDeleteTextures(gColor);
        glDeleteTextures(gLightmap);
        glDeleteTextures(gEmission);
    }

    public void clear() {
        deleteTextures();
    }
}
