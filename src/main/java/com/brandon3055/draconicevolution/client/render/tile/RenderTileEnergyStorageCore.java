package com.brandon3055.draconicevolution.client.render.tile;

import com.brandon3055.brandonscore.client.render.TESRBase;
import com.brandon3055.brandonscore.lib.Vec3I;
import com.brandon3055.brandonscore.lib.datamanager.ManagedVec3I;
import com.brandon3055.brandonscore.utils.ModelUtils;
import com.brandon3055.draconicevolution.DEConfig;
import com.brandon3055.draconicevolution.DEFeatures;
import com.brandon3055.draconicevolution.blocks.machines.EnergyStorageCore;
import com.brandon3055.draconicevolution.blocks.tileentity.TileEnergyStorageCore;
import com.brandon3055.draconicevolution.client.handler.ClientEventHandler;
import com.brandon3055.draconicevolution.helpers.ResourceHelperDE;
import com.brandon3055.draconicevolution.utils.ConfigColours;
import com.brandon3055.draconicevolution.utils.DETextures;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.client.MinecraftForgeClient;
import org.lwjgl.opengl.GL11;

/** Created by brandon3055 on 2/4/2016. */
public class RenderTileEnergyStorageCore extends TESRBase<TileEnergyStorageCore> {
    private static final double[] SCALES = {1.1, 1.7, 2.3, 3.6, 5.5, 7.1, 8.6, 10.2};

    public RenderTileEnergyStorageCore() {}

    @Override
    public void render(
            TileEnergyStorageCore te,
            double x,
            double y,
            double z,
            float partialTicks,
            int destroyStage,
            float alpha) {
        // region Build Guide

        if (te.buildGuide.value && MinecraftForgeClient.getRenderPass() == 0) {
            GlStateManager.bindTexture(
                    Minecraft.getMinecraft().getTextureMapBlocks().getGlTextureId());
            GlStateManager.pushMatrix();
            GlStateManager.translate(x, y, z);
            te.coreStructure.renderTier(te.tier.value);
            GlStateManager.popMatrix();
        }
        if (!te.active.value) return;

        // endregion

        // region Do Calculations
        float rotation = (ClientEventHandler.elapsedTicks + partialTicks) / 2F;
        float brightness =
                (float) Math.abs(Math.sin((float) ClientEventHandler.elapsedTicks / 100f) * 100f);
        double scale = SCALES[te.tier.value - 1];

        boolean t8 = te.tier.value == 8;
        double fill = (double) te.getExtendedStorage() / (double) te.getExtendedCapacity();
        int coreRGB =
                t8
                        ? ConfigColours.blend(
                                DEConfig.energyCoreRGBT8, DEConfig.energyCoreRatioT8, fill)
                        : ConfigColours.blend(
                                DEConfig.energyCoreRGB, DEConfig.energyCoreRatio, fill);
        int shellRGB = t8 ? DEConfig.energyCoreShellRGBT8 : DEConfig.energyCoreShellRGB;
        // endregion

        // region Render Core

        GlStateManager.bindTexture(Minecraft.getMinecraft().getTextureMapBlocks().getGlTextureId());
        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y, z);
        GlStateManager.color(1F, 1F, 1F, 1F);

        // GlStateManager.disableCull();

        if (MinecraftForgeClient.getRenderPass() == 0) {
            GlStateManager.enableCull();
            GlStateManager.disableBlend();
            GlStateManager.depthMask(true);

            GlStateManager.pushMatrix();
            translateScaleTranslate(0.5D, scale, scale, scale);
            setLighting(80f + brightness);
            translateRotateTranslate(0.5, rotation, 0F, 1F, 0.5F);
            List<BakedQuad> innerQuads =
                    ModelUtils.getModelQuads(
                            DEFeatures.energyStorageCore
                                    .getDefaultState()
                                    .withProperty(EnergyStorageCore.RENDER_TYPE, 1));
            renderQuadsRGB(innerQuads, coreRGB);
            GlStateManager.popMatrix();
            setLighting(200F);
            renderStabilizers(te, false, partialTicks, coreRGB, shellRGB);
        } else {
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(
                    GlStateManager.SourceFactor.SRC_ALPHA,
                    GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA,
                    GlStateManager.SourceFactor.ONE,
                    GlStateManager.DestFactor.ZERO);

            setLighting(200F);
            renderStabilizers(te, true, partialTicks, coreRGB, shellRGB);
            translateScaleTranslate(0.5D, scale, scale, scale);
            translateRotateTranslate(0.5, rotation * 0.5F, 0F, -1F, -0.5F);
            List<BakedQuad> outerQuads =
                    ModelUtils.getModelQuads(
                            DEFeatures.energyStorageCore
                                    .getDefaultState()
                                    .withProperty(EnergyStorageCore.RENDER_TYPE, 2));
            renderQuadsRGB(outerQuads, shellRGB);
        }

        GlStateManager.enableTexture2D();
        GlStateManager.depthMask(true);
        GlStateManager.disableBlend();

        resetLighting();
        GlStateManager.popMatrix();

        // endregion
    }

    private static void renderQuadsRGB(List<BakedQuad> quads, int rgb) {
        ModelUtils.renderQuadsRGB(
                quads, (rgb >> 16 & 0xFF) / 255F, (rgb >> 8 & 0xFF) / 255F, (rgb & 0xFF) / 255F);
    }

    private void renderStabilizers(
            TileEnergyStorageCore te,
            boolean renderStage,
            float partialTick,
            int coreRGB,
            int shellRGB) {
        if (!te.stabilizersOK.value) {
            return;
        }

        for (ManagedVec3I vec3I : te.stabOffsets) {
            GlStateManager.pushMatrix();
            GlStateManager.translate(-vec3I.vec.x + 0.5, -vec3I.vec.y + 0.5, -vec3I.vec.z + 0.5);

            EnumFacing facing =
                    EnumFacing.getFacingFromVector(
                            vec3I.vec.x,
                            vec3I.vec.y,
                            vec3I.vec
                                    .z); // EnumFacing.getFacingFromAxis(EnumFacing.AxisDirection.POSITIVE, te.multiBlockAxis);
            if (facing.getAxis() == EnumFacing.Axis.X || facing.getAxis() == EnumFacing.Axis.Y) {
                GlStateManager.rotate(-90F, -facing.getFrontOffsetY(), facing.getFrontOffsetX(), 0);
            } else if (facing == EnumFacing.SOUTH) {
                GlStateManager.rotate(180F, 0, 1, 0);
            }

            GlStateManager.rotate(90, 1, 0, 0);
            if (DEConfig.energyCoreTintBeams) {
                // Two greyscale layers, tinted separately: beam centre and beam edges.
                renderStabilizerBeam(
                        te,
                        vec3I.vec,
                        renderStage,
                        partialTick,
                        DETextures.STABILIZER_BEAM_CENTRE_TINT,
                        coreRGB);
                renderStabilizerBeam(
                        te,
                        vec3I.vec,
                        renderStage,
                        partialTick,
                        DETextures.STABILIZER_BEAM_EDGE_TINT,
                        shellRGB);
            } else {
                renderStabilizerBeam(
                        te,
                        vec3I.vec,
                        renderStage,
                        partialTick,
                        DETextures.STABILIZER_BEAM,
                        0xFFFFFF);
            }
            if (te.tier.value >= 5) {
                GlStateManager.scale(1.2F, 0.5F, 1.2F);
            } else {
                GlStateManager.scale(0.45, 0.45, 0.45);
            }
            //            LogHelper.dev(vec3I);
            renderStabilizer(
                    renderStage,
                    partialTick,
                    DEConfig.energyCoreTintStabilizers ? shellRGB : 0x00FFFF);
            GlStateManager.popMatrix();
        }
    }

    private void renderStabilizer(boolean renderStage, float partialTick, int rgb) {
        IBakedModel bakedModel =
                ModelUtils.loadBakedModel(
                        ResourceHelperDE.getResource("block/obj_models/stabilizer_sphere.obj"));
        List<BakedQuad> listQuads =
                bakedModel.getQuads(DEFeatures.energyStorageCore.getDefaultState(), null, 0);
        GlStateManager.bindTexture(Minecraft.getMinecraft().getTextureMapBlocks().getGlTextureId());
        if (!renderStage) {
            GlStateManager.scale(0.9F, 0.9F, 0.9F);
            GlStateManager.rotate(ClientEventHandler.elapsedTicks + partialTick, 0, -1, 0);
            ModelUtils.renderQuadsARGB(listQuads, 0xFF000000 | rgb);
        } else {
            GlStateManager.rotate((ClientEventHandler.elapsedTicks + partialTick) * 0.5F, 0, 1, 0);
            GlStateManager.scale(1.1F, 1.1F, 1.1F);
            ModelUtils.renderQuadsARGB(listQuads, 0x50000000 | rgb);
        }
    }

    /** Draws one beam layer. rgb tints the layer; 0xFFFFFF draws the texture as is. */
    private void renderStabilizerBeam(
            TileEnergyStorageCore te,
            Vec3I vec,
            boolean renderStage,
            float partialTick,
            String texture,
            int rgb) {
        ResourceHelperDE.bindTexture(texture);
        int r = rgb >> 16 & 0xFF, g = rgb >> 8 & 0xFF, b = rgb & 0xFF;
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder vertexBuffer = tessellator.getBuffer();
        GL11.glPushMatrix();
        GlStateManager.rotate(180, 0, 0, 1);

        double beamLength = Math.abs(vec.x + vec.y + vec.z) - 0.5;
        float time = ClientEventHandler.elapsedTicks + partialTick;
        double rotation = (double) time * 0.025D * -1.5D;
        float beamMotion = -time * 0.2F - (float) MathHelper.floor(-time * 0.1F);

        if (!renderStage) {
            // region Render Inner Beam
            double scale = 0.2;
            double d7 = 0.5D + Math.cos(rotation + 2.356194490192345D) * scale; // x point 1
            double d9 = 0.5D + Math.sin(rotation + 2.356194490192345D) * scale; // z point 1
            double d11 = 0.5D + Math.cos(rotation + (Math.PI / 4D)) * scale; // x point 2
            double d13 = 0.5D + Math.sin(rotation + (Math.PI / 4D)) * scale; // z point 2
            double d15 = 0.5D + Math.cos(rotation + 3.9269908169872414D) * scale; // Dist from x-3
            double d17 = 0.5D + Math.sin(rotation + 3.9269908169872414D) * scale;
            double d19 = 0.5D + Math.cos(rotation + 5.497787143782138D) * scale;
            double d21 = 0.5D + Math.sin(rotation + 5.497787143782138D) * scale;
            double texXMin = 0.0D;
            double texXMax = 1.0D;
            double d28 = (double) (-1.0F + beamMotion);
            double texHeight = beamLength * (0.5D / scale) + d28;

            if (te.tier.value >= 5) {
                GlStateManager.scale(3.5, 1, 3.5);
            }
            GlStateManager.translate(-0.5, 0, -0.5);

            GlStateManager.color(r / 255F, g / 255F, b / 255F, 1F);
            vertexBuffer.begin(7, DefaultVertexFormats.POSITION_TEX);
            vertexBuffer.pos(d7, beamLength, d9).tex(texXMax, texHeight).endVertex();
            vertexBuffer.pos(d7, 0, d9).tex(texXMax, d28).endVertex();
            vertexBuffer.pos(d11, 0, d13).tex(texXMin, d28).endVertex();
            vertexBuffer.pos(d11, beamLength, d13).tex(texXMin, texHeight).endVertex();

            vertexBuffer.pos(d19, beamLength, d21).tex(texXMax, texHeight).endVertex();
            vertexBuffer.pos(d19, 0, d21).tex(texXMax, d28).endVertex();
            vertexBuffer.pos(d15, 0, d17).tex(texXMin, d28).endVertex();
            vertexBuffer.pos(d15, beamLength, d17).tex(texXMin, texHeight).endVertex();

            vertexBuffer.pos(d11, beamLength, d13).tex(texXMax, texHeight).endVertex();
            vertexBuffer.pos(d11, 0, d13).tex(texXMax, d28).endVertex();
            vertexBuffer.pos(d19, 0, d21).tex(texXMin, d28).endVertex();
            vertexBuffer.pos(d19, beamLength, d21).tex(texXMin, texHeight).endVertex();

            vertexBuffer.pos(d15, beamLength, d17).tex(texXMax, texHeight).endVertex();
            vertexBuffer.pos(d15, 0, d17).tex(texXMax, d28).endVertex();
            vertexBuffer.pos(d7, 0, d9).tex(texXMin, d28).endVertex();
            vertexBuffer.pos(d7, beamLength, d9).tex(texXMin, texHeight).endVertex();

            rotation += 0.77f;
            d7 = 0.5D + Math.cos(rotation + 2.356194490192345D) * scale;
            d9 = 0.5D + Math.sin(rotation + 2.356194490192345D) * scale;
            d11 = 0.5D + Math.cos(rotation + (Math.PI / 4D)) * scale;
            d13 = 0.5D + Math.sin(rotation + (Math.PI / 4D)) * scale;
            d15 = 0.5D + Math.cos(rotation + 3.9269908169872414D) * scale;
            d17 = 0.5D + Math.sin(rotation + 3.9269908169872414D) * scale;
            d19 = 0.5D + Math.cos(rotation + 5.497787143782138D) * scale;
            d21 = 0.5D + Math.sin(rotation + 5.497787143782138D) * scale;

            d28 = (-1F + (beamMotion * 1));
            texHeight = beamLength * (0.5D / scale) + d28;

            vertexBuffer.pos(d7, beamLength, d9).tex(texXMax, texHeight).endVertex();
            vertexBuffer.pos(d7, 0, d9).tex(texXMax, d28).endVertex();
            vertexBuffer.pos(d11, 0, d13).tex(texXMin, d28).endVertex();
            vertexBuffer.pos(d11, beamLength, d13).tex(texXMin, texHeight).endVertex();

            vertexBuffer.pos(d19, beamLength, d21).tex(texXMax, texHeight).endVertex();
            vertexBuffer.pos(d19, 0, d21).tex(texXMax, d28).endVertex();
            vertexBuffer.pos(d15, 0, d17).tex(texXMin, d28).endVertex();
            vertexBuffer.pos(d15, beamLength, d17).tex(texXMin, texHeight).endVertex();

            vertexBuffer.pos(d11, beamLength, d13).tex(texXMax, texHeight).endVertex();
            vertexBuffer.pos(d11, 0, d13).tex(texXMax, d28).endVertex();
            vertexBuffer.pos(d19, 0, d21).tex(texXMin, d28).endVertex();
            vertexBuffer.pos(d19, beamLength, d21).tex(texXMin, texHeight).endVertex();

            vertexBuffer.pos(d15, beamLength, d17).tex(texXMax, texHeight).endVertex();
            vertexBuffer.pos(d15, 0, d17).tex(texXMax, d28).endVertex();
            vertexBuffer.pos(d7, 0, d9).tex(texXMin, d28).endVertex();
            vertexBuffer.pos(d7, beamLength, d9).tex(texXMin, texHeight).endVertex();
            // endregion
        } else {
            // region Render Outer Beam
            GlStateManager.rotate(90, -1, 0, 0);
            GlStateManager.rotate(45, 0, 0, 1);
            GlStateManager.translate(0, 0, 0.4);
            GlStateManager.depthMask(true);

            int sides = 4;
            float enlarge = 0.35F;
            if (te.tier.value >= 5) {
                sides = 12;
                enlarge = 0.5F + ((te.tier.value - 5) * 0.1F);
                GlStateManager.rotate(
                        (ClientEventHandler.elapsedTicks + partialTick) * 0.6F, 0, 0, -1);
                GlStateManager.scale(3.5, 3.5, 1);
            }

            vertexBuffer.begin(5, DefaultVertexFormats.POSITION_TEX_COLOR);
            //            for (int i = 0; i < 4; ++i) {
            //                vertexBuffer.putColorRGBA(i + 1, 255, 255, 255, 32);
            //            }

            for (int i = 0; i <= sides; i++) {

                float verX =
                        MathHelper.sin((float) (i % sides) * (float) Math.PI * 2F / (float) sides)
                                * 1;
                float verY =
                        MathHelper.cos((float) (i % sides) * (float) Math.PI * 2F / (float) sides)
                                * 1;
                vertexBuffer
                        .pos((double) (verX * 0.35F), (double) (verY * 0.35F), 0.0D)
                        .tex((double) i, (beamMotion * 2))
                        .color(r, g, b, 32)
                        .endVertex();
                vertexBuffer
                        .pos((double) verX * enlarge, (double) verY * enlarge, beamLength)
                        .tex((double) i, beamLength + (beamMotion * 2))
                        .color(r, g, b, 32)
                        .endVertex();
            }

            GlStateManager.depthMask(false);
            // endregion
        }

        tessellator.draw();
        GlStateManager.color(1F, 1F, 1F, 1F);
        GlStateManager.popMatrix();
    }

    @Override
    public boolean isGlobalRenderer(TileEnergyStorageCore p_188185_1_) {
        return true;
    }
}
