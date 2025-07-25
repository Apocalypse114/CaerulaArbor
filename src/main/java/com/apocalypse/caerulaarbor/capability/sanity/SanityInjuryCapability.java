package com.apocalypse.caerulaarbor.capability.sanity;

import com.apocalypse.caerulaarbor.CaerulaArborMod;
import com.apocalypse.caerulaarbor.init.ModAttributes;
import com.apocalypse.caerulaarbor.init.ModDamageTypes;
import com.apocalypse.caerulaarbor.init.ModMobEffects;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.player.Player;

import java.util.Optional;

public class SanityInjuryCapability implements ISanityInjuryCapability {
    public static final ResourceLocation ID = CaerulaArborMod.loc("sanity_injury");

    private final LivingEntity owner;
    private double value;

    public SanityInjuryCapability(LivingEntity owner) {
        this(owner, 1000);
    }

    public SanityInjuryCapability(LivingEntity owner, double value) {
        this.owner = owner;
        this.value = Mth.clamp(value, 0, 1000);
    }

    @Override
    public boolean hurt(double damage) {
        if (owner.hasEffect(ModMobEffects.SANITY_IMMUNE.get())) return false;
        var sanityResistanceAttr = Optional.ofNullable(owner.getAttribute(ModAttributes.SANITY_INJURY_RESISTANCE.get()));
        double sanityResistance = sanityResistanceAttr.map(AttributeInstance::getValue).orElse(0D);
        damage *= 1 - (sanityResistance / 100);
        if (damage <= 0) return false;
        if (value < damage) {
            sanityBreak();
            this.value = 1000;
        }
        this.value -= damage;
        return true;
    }

    private void sanityBreak() {
        if (owner.level().isClientSide) {
            owner.level().playLocalSound(owner.getX(), owner.getY(), owner.getZ(), SoundEvents.ELDER_GUARDIAN_CURSE,
                    owner.getSoundSource(), 2.2f, 1, false);
        } else {
            owner.addEffect(new MobEffectInstance(ModMobEffects.SANITY_IMMUNE.get(), 200, 0, false, false));
            // 创造模式玩家不受影响，可以考虑是否使用配置来开启?
            if (owner instanceof Player player) {
                if (!player.isCreative()) {
                    player.addEffect(new MobEffectInstance(ModMobEffects.DIZZY.get(), 200, 0, false, false));
                    player.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 200, 0, false, true));
                    player.hurt(ModDamageTypes.causeNervousImpairmentDamage(player.level().registryAccess(), null), 12);
                }
            } else {
                // TODO 改为麻痹（以下是具体实现）
                /*
                状态效果：麻痹，麻痹震颤
                爆条时给3级无限持续时间麻痹
                生物造成伤害时：
                    如果有麻痹震颤：
                        取消伤害
                    否则如果有麻痹：
                        取消伤害
                        给半秒麻痹震颤
                        减少1级（0级时消除）
                 */
                owner.addEffect(new MobEffectInstance(ModMobEffects.DIZZY.get(), 60, 0, false, false));
                owner.hurt(ModDamageTypes.causeNervousImpairmentDamage(owner.level().registryAccess(), null),
                        Mth.clamp(owner.getMaxHealth() * 0.8f, 12, 72));
            }
            owner.level().playSound(owner instanceof Player player ? player : null,
                    owner.getX(), owner.getY(), owner.getZ(),
                    SoundEvents.ELDER_GUARDIAN_CURSE, owner.getSoundSource(), 2.2f, 1);
        }
    }

    // 不要用requireNonNull，一旦后面是空的直接崩游戏
    @Override
    public void tick() {
        var attr = this.owner.getAttribute(ModAttributes.SANITY_REGENERATE.get());
        if (attr == null) return;
        double regenerateRate = attr.getValue();
        if (regenerateRate > 0) this.heal(regenerateRate);
    }

    public double getValue() {
        return value;
    }

    @Override
    public void heal(double value) {
        this.value = Math.min(this.value + value, 1000);
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        tag.putDouble("SanityInjury", this.value);
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        this.value = nbt.getDouble("SanityInjury");
    }
}
