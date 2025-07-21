package com.apocalypse.caerulaarbor.client.model.entity;

import com.apocalypse.caerulaarbor.entity.DivicellularCloneEntity;
import software.bernie.geckolib.model.GeoModel;

import net.minecraft.resources.ResourceLocation;

public class DivicellularCloneModel extends GeoModel<DivicellularCloneEntity> {
	@Override
	public ResourceLocation getAnimationResource(DivicellularCloneEntity entity) {
		return new ResourceLocation("caerula_arbor", "animations/divicellular_hoarder.animation.json");
	}

	@Override
	public ResourceLocation getModelResource(DivicellularCloneEntity entity) {
		return new ResourceLocation("caerula_arbor", "geo/divicellular_hoarder.geo.json");
	}

	@Override
	public ResourceLocation getTextureResource(DivicellularCloneEntity entity) {
		return new ResourceLocation("caerula_arbor", "textures/entities/" + entity.getTexture() + ".png");
	}

}
