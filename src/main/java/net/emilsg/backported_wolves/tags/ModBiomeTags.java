package net.emilsg.backported_wolves.tags;

import net.emilsg.backported_wolves.BackportedWolves;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;

public class ModBiomeTags {
    public static final TagKey<Biome> SPAWNS_WOODS_WOLF = create(BackportedWolves.MOD_ID, "spawns_woods_wolf");
    public static final TagKey<Biome> SPAWNS_ASHEN_WOLF = create(BackportedWolves.MOD_ID, "spawns_ashen_wolf");
    public static final TagKey<Biome> SPAWNS_BLACK_WOLF = create(BackportedWolves.MOD_ID, "spawns_black_wolf");
    public static final TagKey<Biome> SPAWNS_CHESTNUT_WOLF = create(BackportedWolves.MOD_ID, "spawns_chestnut_wolf");
    public static final TagKey<Biome> SPAWNS_RUSTY_WOLF = create(BackportedWolves.MOD_ID, "spawns_rusty_wolf");
    public static final TagKey<Biome> SPAWNS_SPOTTED_WOLF = create(BackportedWolves.MOD_ID, "spawns_spotted_wolf");
    public static final TagKey<Biome> SPAWNS_STRIPED_WOLF = create(BackportedWolves.MOD_ID, "spawns_striped_wolf");
    public static final TagKey<Biome> SPAWNS_SNOWY_WOLF = create(BackportedWolves.MOD_ID, "spawns_snowy_wolf");

    private static TagKey<Biome> create(String namespace, String path) {
        return TagKey.create(Registries.BIOME, new ResourceLocation(namespace, path));
    }
}
