//package com.apocalypse.caerulaarbor.client.layer;
//
//import software.bernie.geckolib.renderer.layer.GeoRenderLayer;
//import software.bernie.geckolib.renderer.GeoRenderer;
//import software.bernie.geckolib.cache.object.BakedGeoModel;
//
//import net.minecraft.resources.ResourceLocation;
//import net.minecraft.client.renderer.texture.OverlayTexture;
//import net.minecraft.client.renderer.RenderType;
//import net.minecraft.client.renderer.MultiBufferSource;
//
//import net.mcreator.caerulaarbor.entity.FeederProkaryoteEntity;
//
//import com.mojang.blaze3d.vertex.VertexConsumer;
//import com.mojang.blaze3d.vertex.PoseStack;
//
//public class FeederProkaryoteLayer extends GeoRenderLayer<FeederProkaryoteEntity> {
//	private static final ResourceLocation LAYER = new ResourceLocation("caerula_arbor", "textures/entities/feederlit.png");
//
//	public FeederProkaryoteLayer(GeoRenderer<FeederProkaryoteEntity> entityRenderer) {
//		super(entityRenderer);
//	}
//
//	@Override
//	public void render(PoseStack poseStack, FeederProkaryoteEntity animatable, BakedGeoModel bakedModel, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay) {
//		RenderType glowRenderType = RenderType.eyes(LAYER);
//		getRenderer().reRender(getDefaultBakedModel(animatable), poseStack, bufferSource, animatable, glowRenderType, bufferSource.getBuffer(glowRenderType), partialTick, packedLight, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
//	}
//}
