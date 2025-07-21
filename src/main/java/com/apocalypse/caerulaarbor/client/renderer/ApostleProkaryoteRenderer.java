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
//import net.mcreator.caerulaarbor.entity.model.ApostleProkaryoteModel;
//import net.mcreator.caerulaarbor.entity.layer.ApostleProkaryoteLayer;
//import net.mcreator.caerulaarbor.entity.ApostleProkaryoteEntity;
//
//import com.mojang.blaze3d.vertex.VertexConsumer;
//import com.mojang.blaze3d.vertex.PoseStack;
//
//public class ApostleProkaryoteRenderer extends GeoEntityRenderer<ApostleProkaryoteEntity> {
//	public ApostleProkaryoteRenderer(EntityRendererProvider.Context renderManager) {
//		super(renderManager, new ApostleProkaryoteModel());
//		this.shadowRadius = 0.5f;
//		this.addRenderLayer(new ApostleProkaryoteLayer(this));
//	}
//
//	@Override
//	public RenderType getRenderType(ApostleProkaryoteEntity animatable, ResourceLocation texture, MultiBufferSource bufferSource, float partialTick) {
//		return RenderType.entityTranslucent(getTextureLocation(animatable));
//	}
//
//	@Override
//	public void preRender(PoseStack poseStack, ApostleProkaryoteEntity entity, BakedGeoModel model, MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red,
//			float green, float blue, float alpha) {
//		float scale = 0.8f;
//		this.scaleHeight = scale;
//		this.scaleWidth = scale;
//		super.preRender(poseStack, entity, model, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
//	}
//
//	@Override
//	protected float getDeathMaxRotation(ApostleProkaryoteEntity entityLivingBaseIn) {
//		return 0.0F;
//	}
//}
