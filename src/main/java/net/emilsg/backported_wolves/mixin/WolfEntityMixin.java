package net.emilsg.backported_wolves.mixin;


import net.emilsg.backported_wolves.variant.WolfEntityVariant;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;


@Mixin(Wolf.class)
public abstract class WolfEntityMixin extends MobEntityMixin {

    @Unique
    private static final EntityDataAccessor<Integer> backportedWolvesForge$VARIANT = SynchedEntityData.defineId(Wolf.class, EntityDataSerializers.INT);

    @Inject(method = "defineSynchedData", at = @At("HEAD"))
    public void initTracker (CallbackInfo ci) {
        Wolf wolfEntity = (Wolf) (Object) this;
        wolfEntity.getEntityData().define(backportedWolvesForge$VARIANT, 0);
    }

    @Inject(method = "addAdditionalSaveData", at = @At("HEAD"))
    public void writeNBTData (CompoundTag pCompound, CallbackInfo ci) {
        pCompound.putInt("Variant", getTypeVariant());
    }

    @Inject(method = "readAdditionalSaveData", at = @At("HEAD"))
    public void readNBTData (CompoundTag pCompound, CallbackInfo ci) {
        Wolf wolfEntity = (Wolf) (Object) this;
        wolfEntity.getEntityData().set(backportedWolvesForge$VARIANT, pCompound.getInt("Variant"));
    }

    @Override
    protected void onInitialize(ServerLevelAccessor pLevel, DifficultyInstance pDifficulty, MobSpawnType pReason, SpawnGroupData pSpawnData, CompoundTag pDataTag, CallbackInfoReturnable<SpawnGroupData> cir) {
        Wolf wolf = (Wolf) (Object) this;
        BlockPos blockPos = new BlockPos(wolf.getBlockX(), wolf.getBlockY(), wolf.getBlockZ());
        Holder<Biome> biome = pLevel.getBiome(blockPos);
        WolfEntityVariant variant = WolfEntityVariant.PALE_WOLF;

        if(biome.is(Biomes.FOREST)) {
            variant = WolfEntityVariant.WOODS_WOLF;
        } else if(biome.is(Biomes.SNOWY_TAIGA)) {
            variant = WolfEntityVariant.ASHEN_WOLF;
        } else if(biome.is(Biomes.OLD_GROWTH_PINE_TAIGA)) {
            variant = WolfEntityVariant.BLACK_WOLF;
        } else if(biome.is(Biomes.OLD_GROWTH_SPRUCE_TAIGA)) {
            variant = WolfEntityVariant.CHESTNUT_WOLF;
        } else if(biome.is(Biomes.SPARSE_JUNGLE)) {
            variant = WolfEntityVariant.RUSTY_WOLF;
        } else if(biome.is(Biomes.SAVANNA_PLATEAU)) {
            variant = WolfEntityVariant.SPOTTED_WOLF;
        } else if(biome.is(Biomes.WOODED_BADLANDS)) {
            variant = WolfEntityVariant.STRIPED_WOLF;
        } else if(biome.is(Biomes.GROVE)) {
            variant = WolfEntityVariant.SNOWY_WOLF;
        }

        this.setVariant(variant);
    }

    @Inject(
            method = "getBreedOffspring(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/entity/AgeableMob;)Lnet/minecraft/world/entity/animal/Wolf;",
            at = @At("RETURN")
    )
    private void onCreateChild(ServerLevel pLevel, AgeableMob pOtherParent, CallbackInfoReturnable<Wolf> cir) {
        Wolf child = cir.getReturnValue();
        Wolf wolfEntity = (Wolf) (Object) this;

        CompoundTag childNbt = new CompoundTag();
        child.addAdditionalSaveData(childNbt);

        CompoundTag nbtParent = new CompoundTag();
        (wolfEntity).addAdditionalSaveData(nbtParent);
        CompoundTag nbtOtherParent = new CompoundTag();
        pOtherParent.addAdditionalSaveData(nbtOtherParent);

        int variant = wolfEntity.getRandom().nextBoolean() ? nbtParent.getInt("Variant") : nbtOtherParent.getInt("Variant");

        child.getEntityData().set(backportedWolvesForge$VARIANT, variant & 255);

        childNbt.putInt("Variant", variant);

        child.readAdditionalSaveData(childNbt);
    }

    public WolfEntityVariant getVariant() {
        return WolfEntityVariant.byId(getTypeVariant() & 255);
    }

    public int getTypeVariant() {
        Wolf wolfEntity = (Wolf) (Object) this;
        return wolfEntity.getEntityData().get(backportedWolvesForge$VARIANT);
    }

    public void setVariant(WolfEntityVariant variant) {
        Wolf wolfEntity = (Wolf) (Object) this;
        wolfEntity.getEntityData().set(backportedWolvesForge$VARIANT, variant.getId() & 255);
    }

}
