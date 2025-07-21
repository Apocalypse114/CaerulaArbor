//package com.apocalypse.caerulaarbor.client.model.entity;
//
//import software.bernie.geckolib.model.data.EntityModelData;
//import software.bernie.geckolib.model.GeoModel;
//import software.bernie.geckolib.core.animation.AnimationState;
//import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
//import software.bernie.geckolib.constant.DataTickets;
//
//import net.minecraft.util.Mth;
//import net.minecraft.resources.ResourceLocation;
//
//import net.mcreator.caerulaarbor.entity.ApostleProkaryoteEntity;
//
//public class ApostleProkaryoteModel extends GeoModel<ApostleProkaryoteEntity> {
//	@Override
//	public ResourceLocation getAnimationResource(ApostleProkaryoteEntity entity) {
//		return new ResourceLocation("caerula_arbor", "animations/apostle.animation.json");
//	}
//
//	@Override
//	public ResourceLocation getModelResource(ApostleProkaryoteEntity entity) {
//		return new ResourceLocation("caerula_arbor", "geo/apostle.geo.json");
//	}
//
//	@Override
//	public ResourceLocation getTextureResource(ApostleProkaryoteEntity entity) {
//		return new ResourceLocation("caerula_arbor", "textures/entities/" + entity.getTexture() + ".png");
//	}
//
//	@Override
//	public void setCustomAnimations(ApostleProkaryoteEntity animatable, long instanceId, AnimationState animationState) {
//		CoreGeoBone head = getAnimationProcessor().getBone("head");
//		if (head != null) {
//			EntityModelData entityData = (EntityModelData) animationState.getData(DataTickets.ENTITY_MODEL_DATA);
//			head.setRotX(entityData.headPitch() * Mth.DEG_TO_RAD);
//			head.setRotY(entityData.netHeadYaw() * Mth.DEG_TO_RAD);
//		}
//
//	}
//}
