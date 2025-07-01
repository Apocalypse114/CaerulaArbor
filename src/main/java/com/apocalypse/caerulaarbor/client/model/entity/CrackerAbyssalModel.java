package com.apocalypse.caerulaarbor.client.model.entity;

import com.apocalypse.caerulaarbor.entity.CrackerAbyssalEntity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.model.data.EntityModelData;

public class CrackerAbyssalModel extends GeoModel<CrackerAbyssalEntity> {
	@Override
	public ResourceLocation getAnimationResource(CrackerAbyssalEntity entity) {
		return new ResourceLocation("caerula_arbor", "animations/reefcracker.animation.json");
	}

	@Override
	public ResourceLocation getModelResource(CrackerAbyssalEntity entity) {
		return new ResourceLocation("caerula_arbor", "geo/reefcracker.geo.json");
	}

	@Override
	public ResourceLocation getTextureResource(CrackerAbyssalEntity entity) {
		return new ResourceLocation("caerula_arbor", "textures/entities/" + entity.getTexture() + ".png");
	}

	@Override
	public void setCustomAnimations(CrackerAbyssalEntity animatable, long instanceId, AnimationState animationState) {
		CoreGeoBone head = getAnimationProcessor().getBone("head");
		if (head != null) {
			EntityModelData entityData = (EntityModelData) animationState.getData(DataTickets.ENTITY_MODEL_DATA);
			head.setRotX(entityData.headPitch() * Mth.DEG_TO_RAD);
			head.setRotY(entityData.netHeadYaw() * Mth.DEG_TO_RAD);
		}

	}
}
