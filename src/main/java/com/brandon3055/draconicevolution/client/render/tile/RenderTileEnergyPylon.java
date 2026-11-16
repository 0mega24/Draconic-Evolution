package com.brandon3055.draconicevolution.client.render.tile;

import codechicken.lib.math.MathHelper;
import codechicken.lib.render.CCModel;
import codechicken.lib.render.CCRenderState;
import codechicken.lib.render.OBJParser;
import codechicken.lib.texture.TextureUtils;
import codechicken.lib.vec.Rotation;
import codechicken.lib.vec.Scale;
import codechicken.lib.vec.Vector3;
import codechicken.lib.vec.uv.IconTransformation;
import com.brandon3055.brandonscore.client.render.TESRBase;
import com.brandon3055.draconicevolution.blocks.tileentity.TileEnergyPylon;
import com.brandon3055.draconicevolution.client.DETextureCache;
import com.brandon3055.draconicevolution.client.handler.ClientEventHandler;
import com.brandon3055.draconicevolution.config.visuals.EnergyCoreVisuals;
import com.brandon3055.draconicevolution.helpers.ResourceHelperDE;
import java.util.Map;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraftforge.client.MinecraftForgeClient;
import org.lwjgl.opengl.GL11;

/** Created by brandon3055 on 20/05/2016. */
public class RenderTileEnergyPylon extends TESRBase<TileEnergyPylon> {

    private static CCModel model;
    private static TextureAtlasSprite lastSprite;
    private static IconTransformation lastIconTransform;

    public RenderTileEnergyPylon() {
        Map<String, CCModel> map =
                OBJParser.parseModels(
                        ResourceHelperDE.getResource(
                                "models/pylon_sphere.obj")); // Note dont generate the model evey
        // render frame move this to
        // constructor
        model = CCModel.combine(map.values());
        model.apply(new Scale(0.35, 0.35, 0.35));
        model.computeNormals();
    }

    @Override
    public void render(
            TileEnergyPylon te,
            double x,
            double y,
            double z,
            float partialTicks,
            int destroyStage,
            float a) {
        if (!te.structureValid.value) {
            return;
        }
        CCRenderState ccrs = CCRenderState.instance();
        TextureUtils.bindBlockTexture();
        GlStateManager.pushMatrix();
        Vector3 translateVector =
                new Vector3(x + 0.5, y + (te.sphereOnTop.value ? 1.5 : -0.5), z + 0.5);
        translateVector.translation().glApply();
        int rgb = EnergyCoreVisuals.pylonRGB;
        TextureAtlasSprite sprite =
                DETextureCache.getDETexture(
                        rgb < 0 ? "models/pylon_sphere_texture" : "models/pylon_sphere_tint");
        if (sprite != lastSprite) {
            lastSprite = sprite;
            lastIconTransform = new IconTransformation(sprite);
        }
        IconTransformation iconTransform = lastIconTransform;
        if (rgb < 0) {
            rgb = 0xFFFFFF;
        }
        setLighting(200F);
        float red = (rgb >> 16 & 0xFF) / 255F;
        float green = (rgb >> 8 & 0xFF) / 255F;
        float blue = (rgb & 0xFF) / 255F;

        if (MinecraftForgeClient.getRenderPass() == 0) {
            GlStateManager.disableCull();

            GlStateManager.color(red, green, blue, 1F);
            ccrs.startDrawing(GL11.GL_TRIANGLES, DefaultVertexFormats.POSITION_TEX_NORMAL);
            Rotation rotY =
                    new Rotation(
                            ((ClientEventHandler.elapsedTicks + partialTicks)
                                            * 2F
                                            * (float) EnergyCoreVisuals.pylonRotationSpeed)
                                    * MathHelper.torad,
                            new Vector3(0, 1, 0.5).normalize());
            model.render(ccrs, iconTransform, rotY);
            ccrs.draw();

            GlStateManager.enableCull();

        } else {
            float f =
                    (((ClientEventHandler.elapsedTicks + partialTicks)
                                            * (float) EnergyCoreVisuals.pylonPulseSpeed)
                                    % 30F)
                            / 30F;

            if (te.isOutputMode.value) {
                f = 1F - f;
            }

            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(
                    GlStateManager.SourceFactor.SRC_ALPHA,
                    GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA,
                    GlStateManager.SourceFactor.ONE,
                    GlStateManager.DestFactor.ZERO);
            GlStateManager.alphaFunc(GL11.GL_GREATER, 0F);
            GlStateManager.color(red, green, blue, 1F - f);
            GlStateManager.enableBlend();

            ccrs.startDrawing(GL11.GL_TRIANGLES, DefaultVertexFormats.POSITION_TEX_NORMAL);
            model.render(ccrs, iconTransform, new Scale(1 + f, 1 + f, 1 + f));
            ccrs.draw();

            GlStateManager.alphaFunc(GL11.GL_GREATER, 0.1F);
            GlStateManager.disableBlend();
        }

        GlStateManager.color(1F, 1F, 1F, 1F);
        GlStateManager.popMatrix();
    }
}
