package com.apocalypse.caerulaarbor.client.layer;

import com.apocalypse.caerulaarbor.CaerulaArborMod;
import com.apocalypse.caerulaarbor.entity.UnicellularPredatorEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoRenderer;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;

public class UnicellularPredatorLayer extends GeoRenderLayer<UnicellularPredatorEntity> {

    private static final ResourceLocation LAYER = CaerulaArborMod.loc("textures/entity/unicellular_predator_layer.png");

    public UnicellularPredatorLayer(GeoRenderer<UnicellularPredatorEntity> entityRenderer) {
        super(entityRenderer);
    }

    @Override
    public void render(PoseStack poseStack, UnicellularPredatorEntity animatable, BakedGeoModel bakedModel, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay) {
        RenderType glowRenderType = RenderType.eyes(LAYER);
        getRenderer().reRender(getDefaultBakedModel(animatable), poseStack, bufferSource, animatable, glowRenderType, bufferSource.getBuffer(glowRenderType), partialTick, packedLight, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
    }
}
