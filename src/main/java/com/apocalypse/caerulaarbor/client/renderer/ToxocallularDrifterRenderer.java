
package com.apocalypse.caerulaarbor.client.renderer;

import com.apocalypse.caerulaarbor.client.layer.ToxocellularDrifterLayer;
import com.apocalypse.caerulaarbor.client.model.entity.ToxocallularDrifterModel;
import com.apocalypse.caerulaarbor.entity.ToxocellularDrifterEntity;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
import software.bernie.geckolib.cache.object.BakedGeoModel;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.MultiBufferSource;

import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.PoseStack;

public class ToxocallularDrifterRenderer extends GeoEntityRenderer<ToxocellularDrifterEntity> {
	public ToxocallularDrifterRenderer(EntityRendererProvider.Context renderManager) {
		super(renderManager, new ToxocallularDrifterModel());
		this.shadowRadius = 0.6f;
		this.addRenderLayer(new ToxocellularDrifterLayer(this));
	}

	@Override
	public RenderType getRenderType(ToxocellularDrifterEntity animatable, ResourceLocation texture, MultiBufferSource bufferSource, float partialTick) {
		return RenderType.entityTranslucent(getTextureLocation(animatable));
	}

	@Override
	public void preRender(PoseStack poseStack, ToxocellularDrifterEntity entity, BakedGeoModel model, MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red,
			float green, float blue, float alpha) {
		float scale = 0.75f;
		this.scaleHeight = scale;
		this.scaleWidth = scale;
		super.preRender(poseStack, entity, model, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
	}

	@Override
	protected float getDeathMaxRotation(ToxocellularDrifterEntity entityLivingBaseIn) {
		return 0.0F;
	}
}
