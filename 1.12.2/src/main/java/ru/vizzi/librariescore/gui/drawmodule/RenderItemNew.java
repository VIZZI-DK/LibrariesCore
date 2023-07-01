package ru.vizzi.librariescore.gui.drawmodule;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.crash.ICrashReportDetail;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ReportedException;
import net.minecraft.world.World;
import net.minecraftforge.client.ForgeHooksClient;

import javax.annotation.Nullable;

public class RenderItemNew {

    private RenderItem renderItem;

    public RenderItemNew(RenderItem renderItem){
        this.renderItem = renderItem;
    }


    public void renderItemAndEffectIntoGUI(ItemStack stack, float xPosition, float yPosition) {
        this.renderItemAndEffectIntoGUI(Minecraft.getMinecraft().player, stack, xPosition, yPosition);
    }

    public void renderItemAndEffectIntoGUI(@Nullable EntityLivingBase p_184391_1_, final ItemStack p_184391_2_, float p_184391_3_, float p_184391_4_) {
        if (!p_184391_2_.isEmpty()) {
            renderItem.zLevel += 50.0F;

            try {
                this.renderItemModelIntoGUI(p_184391_2_, p_184391_3_, p_184391_4_, renderItem.getItemModelWithOverrides(p_184391_2_, (World)null, p_184391_1_));
            } catch (Throwable var8) {
                CrashReport crashreport = CrashReport.makeCrashReport(var8, "Rendering item");
                CrashReportCategory crashreportcategory = crashreport.makeCategory("Item being rendered");
                crashreportcategory.addDetail("Item Type", new ICrashReportDetail<String>() {
                    public String call() throws Exception {
                        return String.valueOf(p_184391_2_.getItem());
                    }
                });
                crashreportcategory.addDetail("Registry Name", () -> {
                    return String.valueOf(p_184391_2_.getItem().getRegistryName());
                });
                crashreportcategory.addDetail("Item Aux", new ICrashReportDetail<String>() {
                    public String call() throws Exception {
                        return String.valueOf(p_184391_2_.getMetadata());
                    }
                });
                crashreportcategory.addDetail("Item NBT", new ICrashReportDetail<String>() {
                    public String call() throws Exception {
                        return String.valueOf(p_184391_2_.getTagCompound());
                    }
                });
                crashreportcategory.addDetail("Item Foil", new ICrashReportDetail<String>() {
                    public String call() throws Exception {
                        return String.valueOf(p_184391_2_.hasEffect());
                    }
                });
                throw new ReportedException(crashreport);
            }

            renderItem.zLevel -= 50.0F;
        }

    }

    public void setupGuiTransform(float xPosition, float yPosition, boolean isGui3d) {
        GlStateManager.translate((float)xPosition, (float)yPosition, 100.0F + renderItem.zLevel);
        GlStateManager.translate(8.0F, 8.0F, 0.0F);
        GlStateManager.scale(1.0F, -1.0F, 1.0F);
        GlStateManager.scale(16.0F, 16.0F, 16.0F);
        if (isGui3d) {
            GlStateManager.enableLighting();
        } else {
            GlStateManager.disableLighting();
        }

    }

    public void renderItemModelIntoGUI(ItemStack stack, float x, float y, IBakedModel bakedmodel) {
        GlStateManager.pushMatrix();
        renderItem.textureManager.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
        renderItem.textureManager.getTexture(TextureMap.LOCATION_BLOCKS_TEXTURE).setBlurMipmap(false, false);
        GlStateManager.enableRescaleNormal();
        GlStateManager.enableAlpha();
        GlStateManager.alphaFunc(516, 0.1F);
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        this.setupGuiTransform(x, y, bakedmodel.isGui3d());
        bakedmodel = ForgeHooksClient.handleCameraTransforms(bakedmodel, ItemCameraTransforms.TransformType.GUI, false);
        renderItem.renderItem(stack, bakedmodel);
        GlStateManager.disableAlpha();
        GlStateManager.disableRescaleNormal();
        GlStateManager.disableLighting();
        GlStateManager.popMatrix();
        renderItem.textureManager.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
        renderItem.textureManager.getTexture(TextureMap.LOCATION_BLOCKS_TEXTURE).restoreLastBlurMipmap();
    }


}
