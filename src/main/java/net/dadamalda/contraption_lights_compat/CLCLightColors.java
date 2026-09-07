package net.dadamalda.contraption_lights_compat;

import net.mehvahdjukaar.amendments.common.tile.LiquidCauldronBlockTile;
import net.mehvahdjukaar.supplementaries.common.block.tiles.GobletBlockTile;
import net.mehvahdjukaar.supplementaries.common.block.tiles.JarBlockTile;
import net.mehvahdjukaar.supplementaries.reg.ModRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.Level;
import net.neoforged.fml.ModList;
import org.patryk3211.powergrid.collections.ModdedBlockEntities;
import org.patryk3211.powergrid.collections.ModdedBlocks;
import org.patryk3211.powergrid.electricity.light.bulb.LightBulbState;
import org.patryk3211.powergrid.electricity.light.fixture.LightFixtureBlockEntity;
import xyz.atmerek.contraptionlights.api.ContraptionLightsApi;
import xyz.atmerek.contraptionlights.api.LightColorProvider;

import java.util.HashMap;
import java.util.Optional;

public class CLCLightColors {
    private static final int WHITE = 0xFFFFFF;
    private static final int WARM = 0xFF914D;

    public static HashMap<ResourceLocation, Integer> SOFT_FLUID_COLORS = new HashMap<>();

    public static void register() {
        addSoftFluidColors();
        if(ModList.get().isLoaded("powergrid")) registerPowerGrid();
        if(ModList.get().isLoaded("supplementaries")) registerSupplementaries();
        if(ModList.get().isLoaded("amendments")) registerAmendments();
    }

    private static void registerPowerGrid() {
        ContraptionLightsApi.registerLightColor(ModdedBlocks.LIGHT_FIXTURE.get(), (level, pos, state) -> {
            Optional<LightFixtureBlockEntity> be = level.getBlockEntity(pos, ModdedBlockEntities.LIGHT_FIXTURE.get());
            if(be.isEmpty()) return LightColorProvider.PASS;
            LightBulbState bulbState = be.get().getBulbState();
            if(bulbState == null) return LightColorProvider.PASS;
            DyeColor dyeColor = bulbState.getColor();
            if(dyeColor == null) {
                return WHITE;
            }
            return dyeColor.getFireworkColor();
        });
    }

    private static void registerSupplementaries() {
        ContraptionLightsApi.registerLightColor(ModRegistry.JAR.get(), (level, pos, state) -> {
            Optional<JarBlockTile> be = level.getBlockEntity(pos, ModRegistry.JAR_TILE.get());
            if(be.isEmpty()) return LightColorProvider.PASS;
            ResourceLocation fluid = be.get().getSoftFluidTank().getFluid().fluidKey().location();
            Integer color = SOFT_FLUID_COLORS.get(fluid);
            if(color == null && level instanceof Level level2) {
                color = be.get().getSoftFluidTank().getCachedParticleColor(level2, pos);
            }
            if(color == null) return LightColorProvider.PASS;
            return color;
        });
        ContraptionLightsApi.registerLightColor(ModRegistry.GOBLET.get(), (level, pos, state) -> {
            Optional<GobletBlockTile> be = level.getBlockEntity(pos, ModRegistry.GOBLET_TILE.get());
            if(be.isEmpty()) return LightColorProvider.PASS;
            ResourceLocation fluid = be.get().getSoftFluidTank().getFluid().fluidKey().location();
            Integer color = SOFT_FLUID_COLORS.get(fluid);
            if(color == null && level instanceof Level level2) {
                color = be.get().getSoftFluidTank().getCachedParticleColor(level2, pos);
            }
            if(color == null) return LightColorProvider.PASS;
            return color;
        });
    }

    private static void registerAmendments() {
        ContraptionLightsApi.registerLightColor(net.mehvahdjukaar.amendments.reg.ModRegistry.LIQUID_CAULDRON.get(), (level, pos, state) -> {
            Optional<LiquidCauldronBlockTile> be = level.getBlockEntity(pos, net.mehvahdjukaar.amendments.reg.ModRegistry.LIQUID_CAULDRON_TILE.get());
            if(be.isEmpty()) return LightColorProvider.PASS;
            ResourceLocation fluid = be.get().getSoftFluidTank().getFluid().fluidKey().location();
            Integer color = SOFT_FLUID_COLORS.get(fluid);
            if(color == null && level instanceof Level level2) {
                color = be.get().getSoftFluidTank().getCachedParticleColor(level2, pos);
            }
            if(color == null) return LightColorProvider.PASS;
            return color;
        });
    }

    private static void addSoftFluidColors() {
        SOFT_FLUID_COLORS.put(ResourceLocation.parse("moonlight:lava"), 0xFF661F);
        SOFT_FLUID_COLORS.put(ResourceLocation.parse("moonlight:experience"), 0xBFFF66);
    }

    public static void lightChanged(Level level, BlockPos pos) {
        ContraptionLightsApi.lightColorChanged(level, pos);
    }
}
