package com.apocalypse.caerulaarbor.client.renderer;

import com.apocalypse.caerulaarbor.client.model.entity.NetherseaSpewerModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
import software.bernie.geckolib.cache.object.BakedGeoModel;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.MultiBufferSource;

import com.apocalypse.caerulaarbor.entity.NetherseaSpewerEntity;

import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.PoseStack;

public class NetherseaSpewerRenderer extends GeoEntityRenderer<NetherseaSpewerEntity> {

    public NetherseaSpewerRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new NetherseaSpewerModel());
        this.shadowRadius = 0.5f;
    }

    @Override
    public RenderType getRenderType(NetherseaSpewerEntity animatable, ResourceLocation texture, MultiBufferSource bufferSource, float partialTick) {
        return RenderType.entityTranslucent(getTextureLocation(animatable));
    }

    @Override
    public void preRender(PoseStack poseStack, NetherseaSpewerEntity entity, BakedGeoModel model, MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red,
                          float green, float blue, float alpha) {
        float scale = 1f;
        this.scaleHeight = scale;
        this.scaleWidth = scale;
        super.preRender(poseStack, entity, model, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
    }
}
