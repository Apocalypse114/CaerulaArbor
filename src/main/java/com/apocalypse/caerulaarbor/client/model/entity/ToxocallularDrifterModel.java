package com.apocalypse.caerulaarbor.client.model.entity;

import com.apocalypse.caerulaarbor.entity.ToxocellularDrifterEntity;
import software.bernie.geckolib.model.GeoModel;

import net.minecraft.resources.ResourceLocation;

public class ToxocallularDrifterModel extends GeoModel<ToxocellularDrifterEntity> {
	@Override
	public ResourceLocation getAnimationResource(ToxocellularDrifterEntity entity) {
		return new ResourceLocation("caerula_arbor", "animations/toxocellular_drifter.animation.json");
	}

	@Override
	public ResourceLocation getModelResource(ToxocellularDrifterEntity entity) {
		return new ResourceLocation("caerula_arbor", "geo/toxocellular_drifter.geo.json");
	}

	@Override
	public ResourceLocation getTextureResource(ToxocellularDrifterEntity entity) {
		return new ResourceLocation("caerula_arbor", "textures/entity/" + entity.getTexture() + ".png");
	}

}
