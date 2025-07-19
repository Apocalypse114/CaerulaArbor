
package com.apocalypse.caerulaarbor.client.renderer;

import com.apocalypse.caerulaarbor.entity.NetherseaBrandguiderEntity;
import com.apocalypse.caerulaarbor.client.model.entity.GuideAbyssalModel;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class GuideAbyssalRenderer extends GeoEntityRenderer<NetherseaBrandguiderEntity> {
	public GuideAbyssalRenderer(EntityRendererProvider.Context renderManager) {
		super(renderManager, new GuideAbyssalModel());
		this.shadowRadius = 0.6f;
	}

	@Override
	public RenderType getRenderType(NetherseaBrandguiderEntity animatable, ResourceLocation texture, MultiBufferSource bufferSource, float partialTick) {
		return RenderType.entityTranslucent(getTextureLocation(animatable));
	}

	@Override
	public void preRender(PoseStack poseStack, NetherseaBrandguiderEntity entity, BakedGeoModel model, MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green,
                          float blue, float alpha) {
		float scale = 1.5f;
		this.scaleHeight = scale;
		this.scaleWidth = scale;
		super.preRender(poseStack, entity, model, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
	}

	@Override
	protected float getDeathMaxRotation(NetherseaBrandguiderEntity entityLivingBaseIn) {
		return 0.0F;
	}
}
