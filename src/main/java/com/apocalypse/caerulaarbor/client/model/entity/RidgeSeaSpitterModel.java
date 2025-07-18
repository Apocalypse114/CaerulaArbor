package com.apocalypse.caerulaarbor.client.model.entity;

import com.apocalypse.caerulaarbor.CaerulaArborMod;
import com.apocalypse.caerulaarbor.entity.RidgeSeaSpitterEntity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.model.data.EntityModelData;

public class RidgeSeaSpitterModel extends GeoModel<RidgeSeaSpitterEntity> {

    @Override
    public ResourceLocation getAnimationResource(RidgeSeaSpitterEntity entity) {
        return CaerulaArborMod.loc("animations/ridge_sea_spitter.animation.json");
    }

    @Override
    public ResourceLocation getModelResource(RidgeSeaSpitterEntity entity) {
        return CaerulaArborMod.loc("geo/ridge_sea_spitter.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(RidgeSeaSpitterEntity entity) {
        return CaerulaArborMod.loc("textures/entity/ridge_sea_spitter.png");
    }

    @Override
    public void setCustomAnimations(RidgeSeaSpitterEntity animatable, long instanceId, AnimationState animationState) {
        CoreGeoBone head = getAnimationProcessor().getBone("head");
        if (head != null) {
            EntityModelData entityData = (EntityModelData) animationState.getData(DataTickets.ENTITY_MODEL_DATA);
            head.setRotX(entityData.headPitch() * Mth.DEG_TO_RAD);
            head.setRotY(entityData.netHeadYaw() * Mth.DEG_TO_RAD);
        }
    }
}
