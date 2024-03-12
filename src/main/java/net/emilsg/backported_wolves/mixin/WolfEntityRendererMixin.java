package net.emilsg.backported_wolves.mixin;

import net.emilsg.backported_wolves.BackportedWolves;

import net.emilsg.backported_wolves.variant.WolfEntityVariant;
import net.minecraft.client.renderer.entity.WolfRenderer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.Wolf;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;


@Mixin(WolfRenderer.class)
public class WolfEntityRendererMixin {
    private static final String BASE_PATH = "textures/entity/wolf/wolf";


    @Inject(method = "getTextureLocation(Lnet/minecraft/world/entity/animal/Wolf;)Lnet/minecraft/resources/ResourceLocation;", at = @At("HEAD"), cancellable = true)
    public void getWolfTexture (Wolf wolfEntity, CallbackInfoReturnable<ResourceLocation> cir) {
        CompoundTag compound = new CompoundTag();
        wolfEntity.addAdditionalSaveData(compound);

        if (compound.contains("Variant")) {
            int wolfVariant = compound.getInt("Variant");
            if (wolfVariant == 0 || wolfVariant >= 9){
                return;
            }
            ResourceLocation customTexture = backportedWolvesForge$getCustomTextureForVariant(wolfVariant, wolfEntity);
            cir.setReturnValue(customTexture);
        }
    }

    @Unique
    private ResourceLocation backportedWolvesForge$getCustomTextureForVariant(int variant, Wolf wolfEntity) {
        String texture = BASE_PATH + "_" + WolfEntityVariant.byId(variant).getSerializedName();

        if (wolfEntity.isTame()){
            texture += "_tame";
        } else if (wolfEntity.getRemainingPersistentAngerTime() > 0){
            texture += "_angry";
        }

        return new ResourceLocation(BackportedWolves.MOD_ID, texture + ".png");
    }
}
