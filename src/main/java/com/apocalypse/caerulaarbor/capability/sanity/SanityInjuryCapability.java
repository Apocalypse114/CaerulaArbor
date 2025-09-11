package com.apocalypse.caerulaarbor.capability.sanity;

import com.apocalypse.caerulaarbor.CaerulaArborMod;
import com.apocalypse.caerulaarbor.init.ModAttributes;
import com.apocalypse.caerulaarbor.init.ModDamageTypes;
import com.apocalypse.caerulaarbor.init.ModMobEffects;
import com.apocalypse.caerulaarbor.network.ModNetwork;
import com.apocalypse.caerulaarbor.network.message.receive.SanityInjurySyncMessage;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.PacketDistributor;

import java.util.Optional;

public class SanityInjuryCapability implements ISanityInjuryCapability {
    public static final ResourceLocation ID = CaerulaArborMod.loc("sanity_injury");

    private static final double MAX_VALUE = 1000.0;
    private static final double RECOVER_PER_TICK = 175.0 / 20.0; // 每秒175
    private static final int RECOVERY_TICKS = (int) Math.ceil(MAX_VALUE / RECOVER_PER_TICK);

    private final LivingEntity owner;
    private double value;
    private boolean recovering; // 归零后的自动恢复期间锁定外部修改

    public SanityInjuryCapability(LivingEntity owner) {
        this(owner, MAX_VALUE);
    }

    public SanityInjuryCapability(LivingEntity owner, double value) {
        this.owner = owner;
        this.value = Mth.clamp(value, 0, MAX_VALUE);
        this.recovering = false;
    }

    @Override
    public boolean hurt(double damage) {
        // 创造模式玩家不受神经损伤影响
        if (owner instanceof Player p && p.isCreative()) return false;
        // 自动恢复期间拒绝外部修改
        if (recovering) return false;

        if (owner.hasEffect(ModMobEffects.SANITY_IMMUNE.get())) return false;
        var sanityResistanceAttr = Optional.ofNullable(owner.getAttribute(ModAttributes.SANITY_INJURY_RESISTANCE.get()));
        double sanityResistance = sanityResistanceAttr.map(AttributeInstance::getValue).orElse(0D);
        damage *= 1 - sanityResistance / 100;
        if (damage <= 0) return false;

        // 若将要归零或以下，则爆条并进入锁定恢复：值置0并开始以固定速率恢复
        if (value - damage <= 0) {
            sanityBreak();
            this.value = 0;
            // recovering 状态由 tick() 统一进入
            syncToClient();
            return true;
        }

        this.value -= damage;
        syncToClient();
        return true;
    }

    private void sanityBreak() {
        if (owner.level().isClientSide) {
            owner.level().playLocalSound(owner.getX(), owner.getY(), owner.getZ(), SoundEvents.ELDER_GUARDIAN_CURSE,
                    owner.getSoundSource(), 2.2f, 1, false);
        } else {
            // 创造模式玩家不添加 SANITY_IMMUNE
            boolean creativePlayer = owner instanceof Player && ((Player) owner).isCreative();
            if (!creativePlayer) {
                owner.addEffect(new MobEffectInstance(ModMobEffects.SANITY_IMMUNE.get(), RECOVERY_TICKS + 1, 0, false, false));
            }
            // 创造模式玩家不受影响，可以考虑是否使用配置来开启?
            if (owner instanceof Player player) {
                if (!player.isCreative()) {
                    if (!player.hasEffect(ModMobEffects.DIZZY.get())) {
                        player.addEffect(new MobEffectInstance(ModMobEffects.DIZZY.get(), 200, 0, false, false));
                    }
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
                owner.addEffect(new MobEffectInstance(ModMobEffects.PALSY.get(), -1, 2, false, false, true));
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
        // 仅服务端推进状态机，客户端依赖同步
        if (owner.level().isClientSide) return;
        double prevValue = this.value;
        boolean prevRecovering = this.recovering;
        // 如果已经为0且尚未进入恢复状态，则开始恢复（保障客户端也能正确进入恢复阶段）
        if (!this.recovering && this.value <= 0) {
            this.value = 0;
            this.recovering = true;
        }
        // 自动恢复阶段：以固定速率恢复到满，并锁定外部修改
        if (this.recovering) {
            this.value = Math.min(this.value + RECOVER_PER_TICK, MAX_VALUE);
            if (this.value >= MAX_VALUE) {
                this.value = MAX_VALUE;
                this.recovering = false; // 恢复满后解除锁定
            }
            if (shouldSync(prevValue, prevRecovering)) syncToClient();
            return; // 恢复阶段不叠加其他再生效果
        }

        var attr = this.owner.getAttribute(ModAttributes.SANITY_REGENERATE.get());
        if (attr == null) {
            if (shouldSync(prevValue, prevRecovering)) syncToClient();
            return;
        }
        double regenerateRate = attr.getValue();
        if (regenerateRate > 0) this.heal(regenerateRate);
        else if (shouldSync(prevValue, prevRecovering)) syncToClient();
    }

    private boolean shouldSync(double prevValue, boolean prevRecovering) {
        return !owner.level().isClientSide && (this.value != prevValue || this.recovering != prevRecovering);
    }

    public double getValue() {
        return value;
    }

    @Override
    public void heal(double value) {
        // 自动恢复期间拒绝外部修改（包括治疗类效果）
        if (this.recovering) return;
        double prev = this.value;
        this.value = Math.min(this.value + value, MAX_VALUE);
        if (prev != this.value) syncToClient();
    }

    private void syncToClient() {
        if (owner.level().isClientSide) return;
        if (owner instanceof ServerPlayer serverPlayer) {
            ModNetwork.PACKET_HANDLER.send(PacketDistributor.PLAYER.with(() -> serverPlayer), new SanityInjurySyncMessage(serializeNBT()));
        }
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        tag.putDouble("SanityInjury", this.value);
        tag.putBoolean("SanityRecovering", this.recovering);
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        this.value = nbt.getDouble("SanityInjury");
        this.recovering = nbt.contains("SanityRecovering") && nbt.getBoolean("SanityRecovering");
    }
}
