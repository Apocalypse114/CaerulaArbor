package com.apocalypse.caerulaarbor.client.renderer;

import com.apocalypse.caerulaarbor.entity.PocketSeaCrawlerEntity;
import com.apocalypse.caerulaarbor.client.layer.PocketSeaCrawlerLayer;
import com.apocalypse.caerulaarbor.client.model.entity.PocketSeaCrawlerModel;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class PocketSeaCrawlerRenderer extends GeoEntityRenderer<PocketSeaCrawlerEntity> {

    public PocketSeaCrawlerRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new PocketSeaCrawlerModel());
        this.shadowRadius = 0.5f;
        this.addRenderLayer(new PocketSeaCrawlerLayer(this));
    }

    @Override
    public RenderType getRenderType(PocketSeaCrawlerEntity animatable, ResourceLocation texture, MultiBufferSource bufferSource, float partialTick) {
        return RenderType.entityTranslucent(getTextureLocation(animatable));
    }

    @Override
    public void preRender(PoseStack poseStack, PocketSeaCrawlerEntity entity, BakedGeoModel model, MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green,
                          float blue, float alpha) {
        float scale = 1.75f;
        this.scaleHeight = scale;
        this.scaleWidth = scale;
        super.preRender(poseStack, entity, model, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
    }

    @Override
    protected float getDeathMaxRotation(PocketSeaCrawlerEntity entityLivingBaseIn) {
        return 0.0F;
    }
}
