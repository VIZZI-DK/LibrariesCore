package ru.vizzi.Utils.shader;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.RenderList;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.culling.Frustrum;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.AxisAlignedBB;
import org.lwjgl.util.vector.Vector3f;
import ru.vizzi.Utils.shader.pbr.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.lwjgl.opengl.GL20.glUseProgram;

public class KrogenitShaders {

    private static final Minecraft mc = Minecraft.getMinecraft();

    public static boolean gBufferPass;
    public static boolean lightPass;
    public static boolean entitiesPass;
    public static boolean handPass;
    public static boolean interfacePass;

    public static GBuffer gBuffer = new GBuffer();

    public static ForwardPBRDirectionalShaderOld forwardPBRDirectionalShaderOld = new ForwardPBRDirectionalShaderOld();
    public static ForwardPBRDirectionalShader forwardPBRDirectionalShader = new ForwardPBRDirectionalShader();
    public static ForwardPBRPointShaderOld forwardPBRPointShaderOld = new ForwardPBRPointShaderOld();
    public static ForwardPBRPointShader forwardPBRPointShader = new ForwardPBRPointShader();

    public static GBufferShaderOld gBufferShaderOld = new GBufferShaderOld();
    public static GBufferShader gBufferShader = new GBufferShader();

    public static GBufferPBRDirectionalShader gBufferPBRDirectionalShader = new GBufferPBRDirectionalShader();
    public static GBufferPBRPointShader gBufferPBRPointShader = new GBufferPBRPointShader();

    private static IPBR currentPBRShader;
    private static ShaderProgram currentShader;

    static {
        forwardPBRDirectionalShaderOld.preInit();
        forwardPBRDirectionalShader.preInit();
        forwardPBRPointShaderOld.preInit();
        forwardPBRPointShader.preInit();
        gBufferShaderOld.preInit();
        gBufferShader.preInit();
        gBufferPBRDirectionalShader.preInit();
        gBufferPBRPointShader.preInit();
    }

    public static void reload() {
        gBuffer.clear();
        gBuffer = new GBuffer();

        forwardPBRDirectionalShaderOld.clear();
        forwardPBRDirectionalShaderOld = new ForwardPBRDirectionalShaderOld();
        forwardPBRPointShaderOld.clear();
        forwardPBRPointShaderOld = new ForwardPBRPointShaderOld();
        forwardPBRDirectionalShader.clear();
        forwardPBRDirectionalShader = new ForwardPBRDirectionalShader();
        forwardPBRPointShader.clear();
        forwardPBRPointShader = new ForwardPBRPointShader();

        gBufferShader.clear();
        gBufferShader = new GBufferShader();
        gBufferShaderOld.clear();
        gBufferShaderOld = new GBufferShaderOld();
        gBufferPBRDirectionalShader.clear();
        gBufferPBRDirectionalShader = new GBufferPBRDirectionalShader();
        gBufferPBRPointShader.clear();
        gBufferPBRPointShader = new GBufferPBRPointShader();

        forwardPBRDirectionalShaderOld.preInit();
        forwardPBRDirectionalShader.preInit();
        forwardPBRPointShaderOld.preInit();
        forwardPBRPointShader.preInit();
        gBufferShaderOld.preInit();
        gBufferShader.preInit();
        gBufferPBRDirectionalShader.preInit();
        gBufferPBRPointShader.preInit();

        PostProcessing.reload();
    }

    public static void setCurrentShader(ShaderProgram shaderProgram) {
        if(shaderProgram == null) {
            currentPBRShader = null;
            currentShader = null;
        } else {
            currentShader = shaderProgram;
            if(shaderProgram instanceof IPBR) {
                currentPBRShader = (IPBR) shaderProgram;
            }
        }
    }

    public static void resize(int width, int height) {
        gBuffer.resize();
        PostProcessing.resize(width, height);
    }

    public static Map<String, WorldRenderer> worldRenderers = Collections.synchronizedMap(new HashMap<>());
    static RenderList[] allRenderLists = new RenderList[] {new RenderList(), new RenderList(), new RenderList(), new RenderList()};

    public static void calculateForwardPointLightsForChunks(RenderGlobal renderGlobal, EntityLivingBase entitylivingbase, double time, Frustrum frustrum) {
        if(GameSettings.lighting != EnumShaderLightingType.ALL.ordinal()) return;
        int renderPass = 1;
        lightPass = true;
//		mc.mcProfiler.startSection("[K]pointLightsChunks");
//        List<PointLight> pointLights = LightManagerClient.getNearestLightsInFrustum();
//        if(pointLights != null && pointLights.size() > 0) {
//            glBlendFunc(GL_SRC_ALPHA, GL_ONE);
//            KrogenitShaders.forwardPBRPointShader.enable();
//            KrogenitShaders.forwardPBRPointShader.setModelView();
//            KrogenitShaders.forwardPBRPointShaderOld.enable();
//            KrogenitShaders.forwardPBRPointShaderOld.setUseTexture(true);
//            KrogenitShaders.forwardPBRPointShaderOld.setModelView();
//
//            double d3 = entitylivingbase.lastTickPosX + (entitylivingbase.posX - entitylivingbase.lastTickPosX) * time;
//            double d1 = entitylivingbase.lastTickPosY + (entitylivingbase.posY - entitylivingbase.lastTickPosY) * time;
//            double d2 = entitylivingbase.lastTickPosZ + (entitylivingbase.posZ - entitylivingbase.lastTickPosZ) * time;
//
//            for(PointLight p : pointLights) {
//                for (RenderList allRenderList : allRenderLists) {
//                    allRenderList.resetList();
//                }
//
//                KrogenitShaders.forwardPBRPointShaderOld.setPointLight(p);
//                AxisAlignedBB aabb = p.getAabb();
//                int minChunkX =  (int)(aabb.minX) / 16 * 16 - 16;
//                int minChunkY =  (int)(aabb.minY) / 16 * 16 - 16;
//                int minChunkZ =  (int)(aabb.minZ) / 16 * 16 - 16;
//                int maxChunkX =  (int)(aabb.maxX) / 16 * 16 + 16;
//                int maxChunkY =  (int)(aabb.maxY) / 16 * 16 + 16;
//                int maxChunkZ =  (int)(aabb.maxZ) / 16 * 16 + 16;
//
//                int k2;
//                int l2;
//                int i2 = 0;
//                for(int x = minChunkX; x < maxChunkX;x+=16) {
//                    for(int y = minChunkY; y < maxChunkY;y+=16) {
//                        for(int z = minChunkZ; z < maxChunkZ;z+=16) {
//                            WorldRenderer worldrenderer = worldRenderers.get(x + ":" + y + ":" + z);
//                            if(worldrenderer != null) {
//                                if(!worldrenderer.skipRenderPass[renderPass] && worldrenderer.isInFrustum && (!renderGlobal.occlusionEnabled || worldrenderer.isVisible) && shouldRenderChunk(renderPass, worldrenderer)) {
//                                    k2 = -1;
//
//                                    for (l2 = 0; l2 < i2; ++l2) {
//                                        if (allRenderLists[l2].rendersChunk(worldrenderer.posXMinus, worldrenderer.posYMinus, worldrenderer.posZMinus)) {
//                                            k2 = l2;
//                                        }
//                                    }
//
//                                    if (k2 < 0) {
//                                        k2 = i2++;
//                                        allRenderLists[k2].setupRenderList(worldrenderer.posXMinus, worldrenderer.posYMinus, worldrenderer.posZMinus, d3, d1, d2);
//                                    }
//
//                                    allRenderLists[k2].addGLRenderList(worldrenderer.getGLCallListForPass(renderPass));
//                                }
//                            }
//                        }
//                    }
//                }
//
//                for (RenderList allRenderList : allRenderLists) {
//                    allRenderList.callLists();
//                }
//            }
//
//            if(currentShader != null) currentShader.disable();
//            glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
//        }
//		mc.mcProfiler.endSection();
        lightPass = false;
    }

    private static final AxisAlignedBB helpAABB = AxisAlignedBB.getBoundingBox(0, 0, 0, 0, 0, 0);

    public static boolean shouldRenderChunk(int renderPass, WorldRenderer worldRenderer) {
        if(renderPass > 0 && forwardPBRPointShaderOld.isActive()) {
//            PointLight l = forwardPBRPointShaderOld.getCurrentLight();
//            if(l != null) {
//                AxisAlignedBB aabb = l.getAabb();
//                helpAABB.minX = worldRenderer.posX;
//                helpAABB.minY = worldRenderer.posY;
//                helpAABB.minZ = worldRenderer.posZ;
//                helpAABB.maxX = worldRenderer.posX + 16;
//                helpAABB.maxY = worldRenderer.posY + 16;
//                helpAABB.maxZ = worldRenderer.posZ + 16;
//                return aabb.intersectsWith(helpAABB);
//            }
        }
        return true;
    }

//    public static void calculateForwardPointLightsForEntities(RenderGlobal renderGlobal, EntityLivingBase entitylivingbase, Frustrum frustrum, float time) {
//        if(mc.gameSettings.lighting != EnumShaderLightingType.ALL) return;
////		mc.mcProfiler.startSection("[K]pointLightsEntities");
//        lightPass = true;
//        List<PointLight> pointLights = LightManagerClient.getNearestLightsInFrustum();
//        if(pointLights != null && pointLights.size() > 0) {
//            glBlendFunc(GL_SRC_ALPHA, GL_ONE);
//            KrogenitShaders.forwardPBRPointShaderOld.enable();
//            KrogenitShaders.forwardPBRPointShaderOld.setUseTexture(true);
//            KrogenitShaders.forwardPBRPointShader.enable();
//            KrogenitShaders.forwardPBRPointShader.setUseTexture(true);
//
//            for(PointLight p : pointLights) {
//                KrogenitShaders.forwardPBRPointShader.enable();
//                KrogenitShaders.forwardPBRPointShader.setPointLight(p);
//                KrogenitShaders.forwardPBRPointShaderOld.enable();
//                KrogenitShaders.forwardPBRPointShaderOld.setPointLight(p);
//                renderGlobal.renderEntities(entitylivingbase, frustrum, time);
//            }
//
//            if(currentShader != null) currentShader.disable();
//            glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
//        }
////		mc.mcProfiler.endSection();
//        lightPass = false;
//    }

//    public static boolean shouldRenderTileEntity(TileEntity tile, int renderPass) {
//        if(renderPass > 0 && forwardPBRPointShaderOld.isActive()) {
//            PointLight l = forwardPBRPointShaderOld.getCurrentLight();
//            if(l != null) {
//                return Math.sqrt(tile.getDistanceFrom(l.pos.x, l.pos.y, l.pos.z)) <= l.power;
//            }
//        }
//
//        return true;
//    }

//    public static boolean shouldRenderEntity(Entity entity, int renderPass) {
//        if(renderPass > 0 && forwardPBRPointShaderOld.isActive()) {
//            PointLight l = forwardPBRPointShaderOld.getCurrentLight();
//            if(l != null) {
//                return entity.getDistance(l.pos.x, l.pos.y, l.pos.z) <= l.power;
//            }
//        }
//
//        return true;
//    }

    public static void calculateForwardPointLightsForHand(EntityRenderer entityRenderer, float time, int j, Frustrum frustrum) {
//        if(mc.gameSettings.lighting != EnumShaderLightingType.ALL) return;
////		mc.mcProfiler.startSection("[K]pointLightsHand");
//        lightPass = true;
//        List<PointLight> pointLights = LightManagerClient.getNearestLightsInFrustum();
//        if(pointLights != null && pointLights.size() > 0) {
//            glEnable(GL_BLEND);
//            glBlendFunc(GL_ONE, GL_ONE);
//            KrogenitShaders.forwardPBRPointShaderOld.enable();
//            KrogenitShaders.forwardPBRPointShaderOld.setUseTexture(true);
//            KrogenitShaders.forwardPBRPointShader.enable();
//            KrogenitShaders.forwardPBRPointShader.setUseTexture(true);
//
//            for(PointLight p : pointLights) {
//                if(mc.thePlayer.getDistance(p.pos.x, p.pos.y, p.pos.z) <= p.power) {
//                    KrogenitShaders.forwardPBRPointShader.enable();
//                    KrogenitShaders.forwardPBRPointShader.setPointLight(p);
//                    KrogenitShaders.forwardPBRPointShaderOld.enable();
//                    KrogenitShaders.forwardPBRPointShaderOld.setPointLight(p);
//                    entityRenderer.renderHand(time, j);
//                }
//            }
//
//            if(currentShader != null) currentShader.disable();
//            glDisable(GL_BLEND);
//            glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
//        }
//		mc.mcProfiler.endSection();
        lightPass = false;
    }

    public static void begin(float tickTime) {
        KrogenitShaders.forwardPBRDirectionalShaderOld.enable();
        KrogenitShaders.forwardPBRDirectionalShaderOld.updateDirectionalLight(tickTime);
        KrogenitShaders.forwardPBRDirectionalShaderOld.setDirectionLight(true);
        KrogenitShaders.forwardPBRDirectionalShaderOld.setNormalMapping(false);
        KrogenitShaders.forwardPBRDirectionalShaderOld.setSpecularMapping(false);
        KrogenitShaders.forwardPBRDirectionalShaderOld.setEmissionMapping(false);
        KrogenitShaders.forwardPBRDirectionalShaderOld.setLightMapping(true);
        KrogenitShaders.forwardPBRDirectionalShaderOld.setModelView();
        KrogenitShaders.forwardPBRDirectionalShader.enable();
        KrogenitShaders.forwardPBRDirectionalShader.setDirectionLight(true);
        Vector3f directColor = KrogenitShaders.forwardPBRDirectionalShaderOld.getDirectColor();
        KrogenitShaders.forwardPBRDirectionalShader.setDirectionLightColor(directColor.x, directColor.y, directColor.z);
        Vector3f directDirection = KrogenitShaders.forwardPBRDirectionalShaderOld.getDirectDirection();
        KrogenitShaders.forwardPBRDirectionalShader.setDirectionLightDirection(directDirection.x, directDirection.y, directDirection.z);
        KrogenitShaders.forwardPBRDirectionalShader.setNormalMapping(false);
        KrogenitShaders.forwardPBRDirectionalShader.setSpecularMapping(false);
        KrogenitShaders.forwardPBRDirectionalShader.setEmissionMapping(false);
        KrogenitShaders.forwardPBRDirectionalShader.setLightMapping(true);
        KrogenitShaders.forwardPBRDirectionalShader.setModelView();
        if(GameSettings.lighting > 1) KrogenitShaders.gBuffer.bind();
        else KrogenitShaders.forwardPBRDirectionalShaderOld.enable();
    }

    public static void beginHand() {
        handPass = true;
        KrogenitShaders.forwardPBRDirectionalShader.enable();
        KrogenitShaders.forwardPBRDirectionalShader.setLightMapping(true);
        KrogenitShaders.forwardPBRDirectionalShader.setDirectionLight(true);
        KrogenitShaders.forwardPBRDirectionalShaderOld.enable();
        KrogenitShaders.forwardPBRDirectionalShaderOld.setLightMapping(true);
        KrogenitShaders.forwardPBRDirectionalShaderOld.setDirectionLight(true);
    }

    public static void endHand(EntityRenderer entityRenderer, float tickTime, int pass, Frustrum frustrum) {
        KrogenitShaders.finishCurrentShader();
        calculateForwardPointLightsForHand(entityRenderer, tickTime, pass, frustrum);
        handPass = false;
    }

    public static void beginInterface() {
        interfacePass = true;
        KrogenitShaders.forwardPBRDirectionalShaderOld.enable();
        KrogenitShaders.forwardPBRDirectionalShaderOld.setLightMapping(false);
        KrogenitShaders.forwardPBRDirectionalShaderOld.setDirectionLight(false);
        KrogenitShaders.forwardPBRDirectionalShaderOld.disable();
        KrogenitShaders.forwardPBRDirectionalShader.enable();
        KrogenitShaders.forwardPBRDirectionalShader.setLightMapping(false);
        KrogenitShaders.forwardPBRDirectionalShader.setDirectionLight(false);
        KrogenitShaders.forwardPBRDirectionalShader.disable();
    }

    public static void endInterface() {
        interfacePass = false;
    }

    public static void setProjectionMatrix() {
        glUseProgram(KrogenitShaders.forwardPBRDirectionalShader.shaderProgram);
        KrogenitShaders.forwardPBRDirectionalShader.setProjectionMatrix();

        if(GameSettings.lighting > 1) {
            if(lightPass) {
                glUseProgram(KrogenitShaders.forwardPBRPointShader.shaderProgram);
                KrogenitShaders.forwardPBRPointShader.setProjectionMatrix();
                glUseProgram(KrogenitShaders.forwardPBRPointShaderOld.shaderProgram);
            } else {
                glUseProgram(KrogenitShaders.gBufferShader.shaderProgram);
                KrogenitShaders.gBufferShader.setProjectionMatrix();
            }
        }

        glUseProgram(currentShader != null ? currentShader.shaderProgram : 0);
    }

    public static IPBR getCurrentPBRShader(boolean oldRender) {
        if(currentPBRShader == null) {
            if(oldRender) KrogenitShaders.forwardPBRDirectionalShaderOld.enable();
            else KrogenitShaders.forwardPBRDirectionalShader.enable();
        } else {
            if(oldRender) {
                if(currentPBRShader == gBufferShader) gBufferShaderOld.enable();
                else if(currentPBRShader == forwardPBRPointShader) forwardPBRPointShaderOld.enable();
                else if(currentPBRShader == forwardPBRDirectionalShader) forwardPBRDirectionalShaderOld.enable();
            } else {
                if(currentPBRShader == gBufferShaderOld) gBufferShader.enable();
                else if(currentPBRShader == forwardPBRPointShaderOld) forwardPBRPointShader.enable();
                else if(currentPBRShader == forwardPBRDirectionalShaderOld) forwardPBRDirectionalShader.enable();
            }
        }
        return currentPBRShader;
    }

    public static ShaderProgram getCurrentShader() {
        return currentShader;
    }

    public static void finishCurrentShader() {
        if(gBufferPass) {
            if(currentPBRShader == gBufferShader) gBufferShaderOld.enable();
        } else if(lightPass) {

        } else if(interfacePass) {
            if(currentShader != null) currentShader.disable();
        } else if(entitiesPass || handPass) {
            if(currentPBRShader == forwardPBRDirectionalShader) forwardPBRDirectionalShaderOld.enable();
        }
    }
}
