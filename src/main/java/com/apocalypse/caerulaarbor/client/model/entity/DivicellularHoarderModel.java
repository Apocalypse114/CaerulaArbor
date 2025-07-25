package com.apocalypse.caerulaarbor.client.model.entity;

import com.apocalypse.caerulaarbor.entity.DivicellularHoarderEntity;
import software.bernie.geckolib.model.GeoModel;

import net.minecraft.resources.ResourceLocation;

public class DivicellularHoarderModel extends GeoModel<DivicellularHoarderEntity> {
	@Override
	public ResourceLocation getAnimationResource(DivicellularHoarderEntity entity) {
		return new ResourceLocation("caerula_arbor", "animations/divicellular_hoarder.animation.json");
	}

	@Override
	public ResourceLocation getModelResource(DivicellularHoarderEntity entity) {
		return new ResourceLocation("caerula_arbor", "geo/divicellular_hoarder.geo.json");
	}

	@Override
	public ResourceLocation getTextureResource(DivicellularHoarderEntity entity) {
		return new ResourceLocation("caerula_arbor", "textures/entity/" + entity.getTexture() + ".png");
	}

}
