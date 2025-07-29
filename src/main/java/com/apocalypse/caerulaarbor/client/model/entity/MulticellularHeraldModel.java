package com.apocalypse.caerulaarbor.client.model.entity;

import com.apocalypse.caerulaarbor.entity.MulticellularHeraldEntity;
import software.bernie.geckolib.model.data.EntityModelData;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.constant.DataTickets;

import net.minecraft.util.Mth;
import net.minecraft.resources.ResourceLocation;


public class MulticellularHeraldModel extends GeoModel<MulticellularHeraldEntity> {
	@Override
	public ResourceLocation getAnimationResource(MulticellularHeraldEntity entity) {
		return new ResourceLocation("caerula_arbor", "animations/multicellular_herald.animation.json");
	}

	@Override
	public ResourceLocation getModelResource(MulticellularHeraldEntity entity) {
		return new ResourceLocation("caerula_arbor", "geo/multicellular_herald.geo.json");
	}

	@Override
	public ResourceLocation getTextureResource(MulticellularHeraldEntity entity) {
		return new ResourceLocation("caerula_arbor", "textures/entity/" + entity.getTexture() + ".png");
	}

	@Override
	public void setCustomAnimations(MulticellularHeraldEntity animatable, long instanceId, AnimationState animationState) {
		CoreGeoBone head = getAnimationProcessor().getBone("head");
		if (head != null) {
			EntityModelData entityData = (EntityModelData) animationState.getData(DataTickets.ENTITY_MODEL_DATA);
			head.setRotX(entityData.headPitch() * Mth.DEG_TO_RAD);
			head.setRotY(entityData.netHeadYaw() * Mth.DEG_TO_RAD);
		}

	}
}
