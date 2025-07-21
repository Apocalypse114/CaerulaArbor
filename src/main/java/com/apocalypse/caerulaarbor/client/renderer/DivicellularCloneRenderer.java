
package com.apocalypse.caerulaarbor.client.renderer;

import com.apocalypse.caerulaarbor.client.layer.DivicellularCloneLayer;
import com.apocalypse.caerulaarbor.client.model.entity.DivicellularCloneModel;
import com.apocalypse.caerulaarbor.entity.DivicellularCloneEntity;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
import software.bernie.geckolib.cache.object.BakedGeoModel;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.MultiBufferSource;

import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.PoseStack;

public class DivicellularCloneRenderer extends GeoEntityRenderer<DivicellularCloneEntity> {
	public DivicellularCloneRenderer(EntityRendererProvider.Context renderManager) {
		super(renderManager, new DivicellularCloneModel());
		this.shadowRadius = 0.5f;
		this.addRenderLayer(new DivicellularCloneLayer(this));
	}

	@Override
	public RenderType getRenderType(DivicellularCloneEntity animatable, ResourceLocation texture, MultiBufferSource bufferSource, float partialTick) {
		return RenderType.entityTranslucent(getTextureLocation(animatable));
	}

	@Override
	public void preRender(PoseStack poseStack, DivicellularCloneEntity entity, BakedGeoModel model, MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red,
			float green, float blue, float alpha) {
		float scale = 1f;
		this.scaleHeight = scale;
		this.scaleWidth = scale;
		super.preRender(poseStack, entity, model, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
	}
}
