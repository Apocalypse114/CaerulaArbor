package com.apocalypse.caerulaarbor.client.model.entity;

import com.apocalypse.caerulaarbor.entity.MatrocellularNurseEntity;
import software.bernie.geckolib.model.GeoModel;

import net.minecraft.resources.ResourceLocation;

public class MatrocellularNurseModel extends GeoModel<MatrocellularNurseEntity> {
	@Override
	public ResourceLocation getAnimationResource(MatrocellularNurseEntity entity) {
		return new ResourceLocation("caerula_arbor", "animations/matrocellular_nurse.animation.json");
	}

	@Override
	public ResourceLocation getModelResource(MatrocellularNurseEntity entity) {
		return new ResourceLocation("caerula_arbor", "geo/matrocellular_nurse.geo.json");
	}

	@Override
	public ResourceLocation getTextureResource(MatrocellularNurseEntity entity) {
		return new ResourceLocation("caerula_arbor", "textures/entity/" + entity.getTexture() + ".png");
	}

}
