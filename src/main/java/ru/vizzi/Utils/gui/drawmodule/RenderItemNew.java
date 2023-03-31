package ru.vizzi.Utils.gui.drawmodule;

import java.util.concurrent.Callable;

import org.lwjgl.opengl.GL11;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.ReportedException;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.ForgeHooksClient;

public class RenderItemNew extends RenderItem {

	 @SuppressWarnings("unused")
	    public void renderItemAndEffectIntoGUINew(FontRenderer font, TextureManager textureManager, final ItemStack itemStack, float x, float y)
	    {
	        if (itemStack != null)
	        {
	            this.zLevel += 50.0F;

	            try
	            {
	                if (!ForgeHooksClient.renderInventoryItem(this.field_147909_c, textureManager, itemStack, renderWithColor, zLevel, x, y))
	                {
	                    this.renderItemIntoGUINew(font, textureManager, itemStack, x, y, true);
	                }
	            }
	            catch (Throwable throwable)
	            {
	                CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Rendering item");
	                CrashReportCategory crashreportcategory = crashreport.makeCategory("Item being rendered");
	                crashreportcategory.addCrashSectionCallable("Item Type", new Callable()
	                {
	                    private static final String __OBFID = "CL_00001004";
	                    public String call()
	                    {
	                        return String.valueOf(itemStack.getItem());
	                    }
	                });
	                crashreportcategory.addCrashSectionCallable("Item Aux", new Callable()
	                {
	                    private static final String __OBFID = "CL_00001005";
	                    public String call()
	                    {
	                        return String.valueOf(itemStack.getItemDamage());
	                    }
	                });
	                crashreportcategory.addCrashSectionCallable("Item NBT", new Callable()
	                {
	                    private static final String __OBFID = "CL_00001006";
	                    public String call()
	                    {
	                        return String.valueOf(itemStack.getTagCompound());
	                    }
	                });
	                crashreportcategory.addCrashSectionCallable("Item Foil", new Callable()
	                {
	                    private static final String __OBFID = "CL_00001007";
	                    public String call()
	                    {
	                        return String.valueOf(itemStack.hasEffect());
	                    }
	                });
	                throw new ReportedException(crashreport);
	            }

	            // Forge: Bugfix, Move this to a per-render pass, modders must handle themselves
	            if (false && itemStack.hasEffect())
	            {
	                GL11.glDepthFunc(GL11.GL_EQUAL);
	                GL11.glDisable(GL11.GL_LIGHTING);
	                GL11.glDepthMask(false);
	                textureManager.bindTexture(RES_ITEM_GLINT);
	                GL11.glEnable(GL11.GL_ALPHA_TEST);
	                GL11.glEnable(GL11.GL_BLEND);
	                GL11.glColor4f(0.5F, 0.25F, 0.8F, 1.0F);
	                this.renderGlintNew(x * 431278612 + y * 32178161, x - 2, y - 2, 20, 20);
	                OpenGlHelper.glBlendFunc(770, 771, 1, 0);
	                GL11.glDepthMask(true);
	                GL11.glEnable(GL11.GL_LIGHTING);
	                GL11.glDepthFunc(GL11.GL_LEQUAL);
	            }

	            this.zLevel -= 50.0F;
	        }
	    }
	 
	 public void renderItemIntoGUINew(FontRenderer font, TextureManager textureManager, ItemStack itemStack, float x, float y, boolean renderEffectNew)
	    {
	        int k = itemStack.getItemDamage();
	        Object object = itemStack.getIconIndex();
	        int l;
	        float f;
	        float f3;
	        float f4;

	        if (itemStack.getItemSpriteNumber() == 0 && RenderBlocks.renderItemIn3d(Block.getBlockFromItem(itemStack.getItem()).getRenderType()))
	        {
	            textureManager.bindTexture(TextureMap.locationBlocksTexture);
	            Block block = Block.getBlockFromItem(itemStack.getItem());
	            GL11.glEnable(GL11.GL_ALPHA_TEST);

	            if (block.getRenderBlockPass() != 0)
	            {
	                GL11.glAlphaFunc(GL11.GL_GREATER, 0.1F);
	                GL11.glEnable(GL11.GL_BLEND);
	                OpenGlHelper.glBlendFunc(770, 771, 1, 0);
	            }
	            else
	            {
	                GL11.glAlphaFunc(GL11.GL_GREATER, 0.5F);
	                GL11.glDisable(GL11.GL_BLEND);
	            }

	            GL11.glPushMatrix();
	            GL11.glTranslatef((x - 2), (y + 3), -3.0F + this.zLevel);
	            GL11.glScalef(10.0F, 10.0F, 10.0F);
	            GL11.glTranslatef(1.0F, 0.5F, 1.0F);
	            GL11.glScalef(1.0F, 1.0F, -1.0F);
	            GL11.glRotatef(210.0F, 1.0F, 0.0F, 0.0F);
	            GL11.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);
	            l = itemStack.getItem().getColorFromItemStack(itemStack, 0);
	            f3 = (float)(l >> 16 & 255) / 255.0F;
	            f4 = (float)(l >> 8 & 255) / 255.0F;
	            f = (float)(l & 255) / 255.0F;

	            if (this.renderWithColor)
	            {
	                GL11.glColor4f(f3, f4, f, 1.0F);
	            }

	            GL11.glRotatef(-90.0F, 0.0F, 1.0F, 0.0F);
	            this.renderBlocksRi.useInventoryTint = this.renderWithColor;
	            this.renderBlocksRi.renderBlockAsItem(block, k, 1.0F);
	            this.renderBlocksRi.useInventoryTint = true;

	            if (block.getRenderBlockPass() == 0)
	            {
	                GL11.glAlphaFunc(GL11.GL_GREATER, 0.1F);
	            }

	            GL11.glPopMatrix();
	        }
	        else if (itemStack.getItem().requiresMultipleRenderPasses())
	        {
	            GL11.glDisable(GL11.GL_LIGHTING);
	            GL11.glEnable(GL11.GL_ALPHA_TEST);
	            textureManager.bindTexture(TextureMap.locationItemsTexture);
	            GL11.glDisable(GL11.GL_ALPHA_TEST);
	            GL11.glDisable(GL11.GL_TEXTURE_2D);
	            GL11.glEnable(GL11.GL_BLEND);
	            OpenGlHelper.glBlendFunc(0, 0, 0, 0);
	            GL11.glColorMask(false, false, false, true);
	            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
	            Tessellator tessellator = Tessellator.instance;
	            tessellator.startDrawingQuads();
	            tessellator.setColorOpaque_I(-1);
	            tessellator.addVertex((double)(x - 2), (double)(y + 18), (double)this.zLevel);
	            tessellator.addVertex((double)(x + 18), (double)(y + 18), (double)this.zLevel);
	            tessellator.addVertex((double)(x + 18), (double)(y - 2), (double)this.zLevel);
	            tessellator.addVertex((double)(x - 2), (double)(y - 2), (double)this.zLevel);
	            tessellator.draw();
	            GL11.glColorMask(true, true, true, true);
	            GL11.glEnable(GL11.GL_TEXTURE_2D);
	            GL11.glEnable(GL11.GL_ALPHA_TEST);

	            Item item = itemStack.getItem();
	            for (l = 0; l < item.getRenderPasses(k); ++l)
	            {
	                OpenGlHelper.glBlendFunc(770, 771, 1, 0);
	                textureManager.bindTexture(item.getSpriteNumber() == 0 ? TextureMap.locationBlocksTexture : TextureMap.locationItemsTexture);
	                IIcon iicon = item.getIcon(itemStack, l);
	                int i1 = itemStack.getItem().getColorFromItemStack(itemStack, l);
	                f = (float)(i1 >> 16 & 255) / 255.0F;
	                float f1 = (float)(i1 >> 8 & 255) / 255.0F;
	                float f2 = (float)(i1 & 255) / 255.0F;

	                if (this.renderWithColor)
	                {
	                    GL11.glColor4f(f, f1, f2, 1.0F);
	                }

	                GL11.glDisable(GL11.GL_LIGHTING); //Forge: Make sure that render states are reset, ad renderEffectNew can derp them up.
	                GL11.glEnable(GL11.GL_ALPHA_TEST);

	                this.renderIconNew(x, y, iicon, 16, 16);

	                GL11.glDisable(GL11.GL_ALPHA_TEST);
	                GL11.glEnable(GL11.GL_LIGHTING);

	                if (renderEffectNew && itemStack.hasEffect(l))
	                {
	                    renderEffectNew(textureManager, x, y);
	                }
	            }

	            GL11.glEnable(GL11.GL_LIGHTING);
	        }
	        else
	        {
	            GL11.glDisable(GL11.GL_LIGHTING);
	            GL11.glEnable(GL11.GL_BLEND);
	            OpenGlHelper.glBlendFunc(770, 771, 1, 0);
	            ResourceLocation resourcelocation = textureManager.getResourceLocation(itemStack.getItemSpriteNumber());
	            textureManager.bindTexture(resourcelocation);

	            if (object == null)
	            {
	                object = ((TextureMap)Minecraft.getMinecraft().getTextureManager().getTexture(resourcelocation)).getAtlasSprite("missingno");
	            }

	            l = itemStack.getItem().getColorFromItemStack(itemStack, 0);
	            f3 = (float)(l >> 16 & 255) / 255.0F;
	            f4 = (float)(l >> 8 & 255) / 255.0F;
	            f = (float)(l & 255) / 255.0F;

	            if (this.renderWithColor)
	            {
	                GL11.glColor4f(f3, f4, f, 1.0F);
	            }

	            GL11.glDisable(GL11.GL_LIGHTING); //Forge: Make sure that render states are reset, a renderEffectNew can derp them up.
	            GL11.glEnable(GL11.GL_ALPHA_TEST);
	            GL11.glEnable(GL11.GL_BLEND);

	            this.renderIconNew(x, y, (IIcon)object, 16, 16);

	            GL11.glEnable(GL11.GL_LIGHTING);
	            GL11.glDisable(GL11.GL_ALPHA_TEST);
	            GL11.glDisable(GL11.GL_BLEND);

	            if (renderEffectNew && itemStack.hasEffect(0))
	            {
	                renderEffectNew(textureManager, x, y);
	            }
	            GL11.glEnable(GL11.GL_LIGHTING);
	        }

	        GL11.glEnable(GL11.GL_CULL_FACE);
	    }
	 public void renderEffectNew(TextureManager manager, float x, float y)
	    {
	        GL11.glDepthFunc(GL11.GL_EQUAL);
	        GL11.glDisable(GL11.GL_LIGHTING);
	        GL11.glDepthMask(false);
	        manager.bindTexture(RES_ITEM_GLINT);
	        GL11.glEnable(GL11.GL_ALPHA_TEST);
	        GL11.glEnable(GL11.GL_BLEND);
	        GL11.glColor4f(0.5F, 0.25F, 0.8F, 1.0F);
	        this.renderGlintNew(x * 431278612 + y * 32178161, x - 2, y - 2, 20, 20);
	        GL11.glDepthMask(true);
	        GL11.glDisable(GL11.GL_BLEND);
	        GL11.glDisable(GL11.GL_ALPHA_TEST);
	        GL11.glEnable(GL11.GL_LIGHTING);
	        GL11.glDepthFunc(GL11.GL_LEQUAL);
	    }
	 private void renderGlintNew(float p_77018_1_, float p_77018_2_, float p_77018_3_, int p_77018_4_, int p_77018_5_)
	    {
	        for (int j1 = 0; j1 < 2; ++j1)
	        {
	            OpenGlHelper.glBlendFunc(772, 1, 0, 0);
	            float f = 0.00390625F;
	            float f1 = 0.00390625F;
	            float f2 = (float)(Minecraft.getSystemTime() % (long)(3000 + j1 * 1873)) / (3000.0F + (float)(j1 * 1873)) * 256.0F;
	            float f3 = 0.0F;
	            Tessellator tessellator = Tessellator.instance;
	            float f4 = 4.0F;

	            if (j1 == 1)
	            {
	                f4 = -1.0F;
	            }

	            tessellator.startDrawingQuads();
	            tessellator.addVertexWithUV((double)(p_77018_2_ + 0), (double)(p_77018_3_ + p_77018_5_), (double)this.zLevel, (double)((f2 + (float)p_77018_5_ * f4) * f), (double)((f3 + (float)p_77018_5_) * f1));
	            tessellator.addVertexWithUV((double)(p_77018_2_ + p_77018_4_), (double)(p_77018_3_ + p_77018_5_), (double)this.zLevel, (double)((f2 + (float)p_77018_4_ + (float)p_77018_5_ * f4) * f), (double)((f3 + (float)p_77018_5_) * f1));
	            tessellator.addVertexWithUV((double)(p_77018_2_ + p_77018_4_), (double)(p_77018_3_ + 0), (double)this.zLevel, (double)((f2 + (float)p_77018_4_) * f), (double)((f3 + 0.0F) * f1));
	            tessellator.addVertexWithUV((double)(p_77018_2_ + 0), (double)(p_77018_3_ + 0), (double)this.zLevel, (double)((f2 + 0.0F) * f), (double)((f3 + 0.0F) * f1));
	            tessellator.draw();
	        }
	    }
	 
	 public void renderIconNew(float p_94149_1_, float p_94149_2_, IIcon p_94149_3_, float p_94149_4_, float p_94149_5_)
	    {
	        Tessellator tessellator = Tessellator.instance;
	        tessellator.startDrawingQuads();
	        tessellator.addVertexWithUV((double)(p_94149_1_ + 0), (double)(p_94149_2_ + p_94149_5_), (double)this.zLevel, (double)p_94149_3_.getMinU(), (double)p_94149_3_.getMaxV());
	        tessellator.addVertexWithUV((double)(p_94149_1_ + p_94149_4_), (double)(p_94149_2_ + p_94149_5_), (double)this.zLevel, (double)p_94149_3_.getMaxU(), (double)p_94149_3_.getMaxV());
	        tessellator.addVertexWithUV((double)(p_94149_1_ + p_94149_4_), (double)(p_94149_2_ + 0), (double)this.zLevel, (double)p_94149_3_.getMaxU(), (double)p_94149_3_.getMinV());
	        tessellator.addVertexWithUV((double)(p_94149_1_ + 0), (double)(p_94149_2_ + 0), (double)this.zLevel, (double)p_94149_3_.getMinU(), (double)p_94149_3_.getMinV());
	        tessellator.draw();
	    }

}
