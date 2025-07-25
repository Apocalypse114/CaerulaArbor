package com.apocalypse.caerulaarbor.client.model.entity;

import com.apocalypse.caerulaarbor.entity.ExocellularDepositerEntity;
import software.bernie.geckolib.model.GeoModel;

import net.minecraft.resources.ResourceLocation;


public class ExocellularDepositerModel extends GeoModel<ExocellularDepositerEntity> {
	@Override
	public ResourceLocation getAnimationResource(ExocellularDepositerEntity entity) {
		return new ResourceLocation("caerula_arbor", "animations/exocellular_depsoiter.animation.json");
	}

	@Override
	public ResourceLocation getModelResource(ExocellularDepositerEntity entity) {
		return new ResourceLocation("caerula_arbor", "geo/exocellular_depsoiter.geo.json");
	}

	@Override
	public ResourceLocation getTextureResource(ExocellularDepositerEntity entity) {
		return new ResourceLocation("caerula_arbor", "textures/entity/" + entity.getTexture() + ".png");
	}

}
