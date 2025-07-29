
package com.apocalypse.caerulaarbor.client.renderer;

import com.apocalypse.caerulaarbor.client.layer.MatrocellularNurseLayer;
import com.apocalypse.caerulaarbor.client.model.entity.MatrocellularNurseModel;
import com.apocalypse.caerulaarbor.entity.MatrocellularNurseEntity;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
import software.bernie.geckolib.cache.object.BakedGeoModel;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.MultiBufferSource;

import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.PoseStack;

public class MatrocellularNurseRenderer extends GeoEntityRenderer<MatrocellularNurseEntity> {
	public MatrocellularNurseRenderer(EntityRendererProvider.Context renderManager) {
		super(renderManager, new MatrocellularNurseModel());
		this.shadowRadius = 0.8f;
		this.addRenderLayer(new MatrocellularNurseLayer(this));
	}

	@Override
	public RenderType getRenderType(MatrocellularNurseEntity animatable, ResourceLocation texture, MultiBufferSource bufferSource, float partialTick) {
		return RenderType.entityTranslucent(getTextureLocation(animatable));
	}

	@Override
	public void preRender(PoseStack poseStack, MatrocellularNurseEntity entity, BakedGeoModel model, MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red,
			float green, float blue, float alpha) {
		float scale = 1f;
		this.scaleHeight = scale;
		this.scaleWidth = scale;
		super.preRender(poseStack, entity, model, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
	}

	@Override
	protected float getDeathMaxRotation(MatrocellularNurseEntity entityLivingBaseIn) {
		return 0.0F;
	}
}
