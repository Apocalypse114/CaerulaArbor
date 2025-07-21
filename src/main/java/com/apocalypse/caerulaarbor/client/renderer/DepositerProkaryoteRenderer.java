//
//package com.apocalypse.caerulaarbor.client.renderer;
//
//import software.bernie.geckolib.renderer.GeoEntityRenderer;
//import software.bernie.geckolib.cache.object.BakedGeoModel;
//
//import net.minecraft.resources.ResourceLocation;
//import net.minecraft.client.renderer.entity.EntityRendererProvider;
//import net.minecraft.client.renderer.RenderType;
//import net.minecraft.client.renderer.MultiBufferSource;
//
//import net.mcreator.caerulaarbor.entity.model.DepositerProkaryoteModel;
//import net.mcreator.caerulaarbor.entity.layer.DepositerProkaryoteLayer;
//import net.mcreator.caerulaarbor.entity.DepositerProkaryoteEntity;
//
//import com.mojang.blaze3d.vertex.VertexConsumer;
//import com.mojang.blaze3d.vertex.PoseStack;
//
//public class DepositerProkaryoteRenderer extends GeoEntityRenderer<DepositerProkaryoteEntity> {
//	public DepositerProkaryoteRenderer(EntityRendererProvider.Context renderManager) {
//		super(renderManager, new DepositerProkaryoteModel());
//		this.shadowRadius = 0.625f;
//		this.addRenderLayer(new DepositerProkaryoteLayer(this));
//	}
//
//	@Override
//	public RenderType getRenderType(DepositerProkaryoteEntity animatable, ResourceLocation texture, MultiBufferSource bufferSource, float partialTick) {
//		return RenderType.entityTranslucent(getTextureLocation(animatable));
//	}
//
//	@Override
//	public void preRender(PoseStack poseStack, DepositerProkaryoteEntity entity, BakedGeoModel model, MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red,
//			float green, float blue, float alpha) {
//		float scale = 1f;
//		this.scaleHeight = scale;
//		this.scaleWidth = scale;
//		super.preRender(poseStack, entity, model, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
//	}
//
//	@Override
//	protected float getDeathMaxRotation(DepositerProkaryoteEntity entityLivingBaseIn) {
//		return 0.0F;
//	}
//}
