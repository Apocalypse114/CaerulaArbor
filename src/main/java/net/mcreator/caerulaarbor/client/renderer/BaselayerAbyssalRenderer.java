
package net.mcreator.caerulaarbor.client.renderer;

import software.bernie.geckolib.renderer.GeoEntityRenderer;
import software.bernie.geckolib.cache.object.BakedGeoModel;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.MultiBufferSource;

import net.mcreator.caerulaarbor.entity.model.BaselayerAbyssalModel;
import net.mcreator.caerulaarbor.entity.BaselayerAbyssalEntity;

import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.PoseStack;

public class BaselayerAbyssalRenderer extends GeoEntityRenderer<BaselayerAbyssalEntity> {
	public BaselayerAbyssalRenderer(EntityRendererProvider.Context renderManager) {
		super(renderManager, new BaselayerAbyssalModel());
		this.shadowRadius = 0.5f;
	}

	@Override
	public RenderType getRenderType(BaselayerAbyssalEntity animatable, ResourceLocation texture, MultiBufferSource bufferSource, float partialTick) {
		return RenderType.entityTranslucent(getTextureLocation(animatable));
	}

	@Override
	public void preRender(PoseStack poseStack, BaselayerAbyssalEntity entity, BakedGeoModel model, MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red,
			float green, float blue, float alpha) {
		float scale = 1.4f;
		this.scaleHeight = scale;
		this.scaleWidth = scale;
		super.preRender(poseStack, entity, model, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
	}

	@Override
	protected float getDeathMaxRotation(BaselayerAbyssalEntity entityLivingBaseIn) {
		return 0.0F;
	}
}
