package net.dadamalda.contraption_lights_compat;

import com.kipti.bnb.content.trinkets.light.headlamp.HeadlampBlockEntity;
import com.kipti.bnb.content.trinkets.light.headlamp.rendering.HeadlampConstants;
import com.kipti.bnb.registry.content.BnbBlockEntities;
import com.kipti.bnb.registry.content.blocks.BnbTrinketBlocks;
import com.simibubi.create.AllBlockEntityTypes;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.content.fluids.tank.FluidTankBlockEntity;
import dev.propulsionteam.propulsionsimulated.registries.PropulsionBlockEntities;
import dev.propulsionteam.propulsionsimulated.registries.PropulsionBlocks;
import dev.simulated_team.simulated.content.blocks.portable_engine.PortableEngineBlockEntity;
import dev.simulated_team.simulated.index.SimBlockEntityTypes;
import dev.simulated_team.simulated.index.SimBlocks;
import net.dadamalda.contraption_lights_compat.data_loading.ColorMaps;
import net.mehvahdjukaar.amendments.common.tile.CandleSkullBlockTile;
import net.mehvahdjukaar.amendments.common.tile.LiquidCauldronBlockTile;
import net.mehvahdjukaar.supplementaries.common.block.tiles.GobletBlockTile;
import net.mehvahdjukaar.supplementaries.common.block.tiles.JarBlockTile;
import net.mehvahdjukaar.supplementaries.reg.ModRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.fml.ModList;
import org.patryk3211.powergrid.collections.ModdedBlockEntities;
import org.patryk3211.powergrid.collections.ModdedBlocks;
import org.patryk3211.powergrid.electricity.light.bulb.LightBulbState;
import org.patryk3211.powergrid.electricity.light.fixture.LightFixtureBlockEntity;
import xyz.atmerek.contraptionlights.api.ContraptionLightsApi;
import xyz.atmerek.contraptionlights.api.LightColorProvider;
import xyz.atmerek.contraptionlights.light.color.ColorLightFeature;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public class CLCLightColors {
    public static final int WHITE = 0xFFFFFF;
    public static final int WARM = 0xFF914D;

    public static HashMap<ResourceLocation, Integer> CANDLE_COLORS = new HashMap<>();

    public static void register() {
        addCandleColors();
        if(ModList.get().isLoaded("powergrid")) registerPowerGrid();
        if(ModList.get().isLoaded("supplementaries")) registerSupplementaries();
        if(ModList.get().isLoaded("amendments")) registerAmendments();
        if(ModList.get().isLoaded("create")) registerCreate();
        if(ModList.get().isLoaded("simulated")) registerSimulated();
        if(ModList.get().isLoaded("bits_n_bobs")) registerBitsNBobs();
        if(ModList.get().isLoaded("createpropulsion")) registerPropulsionSimulated();
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
            int color = ColorMaps.SOFT_FLUIDS.getOrPass(fluid);
            if(color == LightColorProvider.PASS && level instanceof Level level2) {
                color = be.get().getSoftFluidTank().getCachedParticleColor(level2, pos);
            }
            return color;
        });
        ContraptionLightsApi.registerLightColor(ModRegistry.GOBLET.get(), (level, pos, state) -> {
            Optional<GobletBlockTile> be = level.getBlockEntity(pos, ModRegistry.GOBLET_TILE.get());
            if(be.isEmpty()) return LightColorProvider.PASS;
            ResourceLocation fluid = be.get().getSoftFluidTank().getFluid().fluidKey().location();
            int color = ColorMaps.SOFT_FLUIDS.getOrPass(fluid);
            if(color == LightColorProvider.PASS && level instanceof Level level2) {
                color = be.get().getSoftFluidTank().getCachedParticleColor(level2, pos);
            }
            return color;
        });
    }

    private static void registerAmendments() {
        ContraptionLightsApi.registerLightColor(net.mehvahdjukaar.amendments.reg.ModRegistry.LIQUID_CAULDRON.get(), (level, pos, state) -> {
            Optional<LiquidCauldronBlockTile> be = level.getBlockEntity(pos, net.mehvahdjukaar.amendments.reg.ModRegistry.LIQUID_CAULDRON_TILE.get());
            if(be.isEmpty()) return LightColorProvider.PASS;
            ResourceLocation fluid = be.get().getSoftFluidTank().getFluid().fluidKey().location();
            int color = ColorMaps.SOFT_FLUIDS.getOrPass(fluid);
            if(color == LightColorProvider.PASS && level instanceof Level level2) {
                color = be.get().getSoftFluidTank().getCachedParticleColor(level2, pos);
            }
            return color;
        });
        ContraptionLightsApi.registerLightColor(net.mehvahdjukaar.amendments.reg.ModRegistry.SKULL_CANDLE.get(), CLCLightColors::provideSkullCandleColor);
        ContraptionLightsApi.registerLightColor(net.mehvahdjukaar.amendments.reg.ModRegistry.SKULL_CANDLE_WALL.get(), CLCLightColors::provideSkullCandleColor);
        ContraptionLightsApi.registerLightColor(net.mehvahdjukaar.amendments.reg.ModRegistry.SKULL_CANDLE_SOUL.get(), CLCLightColors::provideSkullCandleColor);
        ContraptionLightsApi.registerLightColor(net.mehvahdjukaar.amendments.reg.ModRegistry.SKULL_CANDLE_SOUL_WALL.get(), CLCLightColors::provideSkullCandleColor);
    }

    private static int provideSkullCandleColor(BlockGetter level, BlockPos pos, BlockState state) {
        if(!ColorLightFeature.colorfulCandles()) return LightColorProvider.PASS;
        Optional<CandleSkullBlockTile> be = level.getBlockEntity(pos, net.mehvahdjukaar.amendments.reg.ModRegistry.SKULL_CANDLE_TILE.get());
        if(be.isEmpty()) return LightColorProvider.PASS;
        Block candle = be.get().getCandle().getBlock();
        ResourceLocation candleLocation = BuiltInRegistries.BLOCK.getKey(candle);
        return CANDLE_COLORS.getOrDefault(candleLocation, LightColorProvider.PASS);
    }

    private static void registerCreate() {
        ContraptionLightsApi.registerLightColor(AllBlocks.FLUID_TANK.get(),
                createFluidTankProvider(AllBlockEntityTypes.FLUID_TANK.get()));
        ContraptionLightsApi.registerLightColor(AllBlocks.CREATIVE_FLUID_TANK.get(),
                createFluidTankProvider(AllBlockEntityTypes.CREATIVE_FLUID_TANK.get()));
    }

    private static void registerSimulated() {
        for (DyeColor color : DyeColor.values()) {
            ContraptionLightsApi.registerLightColor(SimBlocks.PORTABLE_ENGINES.get(color).get(), CLCLightColors::handlePortableEngine);
        }
    }

    private static int handlePortableEngine(BlockGetter level, BlockPos pos, BlockState state) {
        Optional<PortableEngineBlockEntity> be = level.getBlockEntity(pos, SimBlockEntityTypes.PORTABLE_ENGINE.get());
        if(be.isEmpty()) return LightColorProvider.PASS;
        return be.get().isSuperHeated() ? 0x3385FF : 0xFF914C;
    }

    private static void registerPropulsionSimulated() {
        ContraptionLightsApi.registerLightColor(PropulsionBlocks.PLATINUM_FLUID_TANK.get(),
                createFluidTankProvider(PropulsionBlockEntities.PLATINUM_FLUID_TANK_BLOCK_ENTITY.get()));
        ContraptionLightsApi.registerLightColor(PropulsionBlocks.PLATINUM_FLUID_VESSEL.get(),
                createFluidTankProvider(PropulsionBlockEntities.PLATINUM_FLUID_VESSEL_BLOCK_ENTITY.get()));
    }

    private static void registerBitsNBobs() {
        ContraptionLightsApi.registerLightColor(BnbTrinketBlocks.HEADLAMP.get(), (level, pos, state) -> {
            Optional<HeadlampBlockEntity> be = level.getBlockEntity(pos, BnbBlockEntities.HEADLAMP.get());
            if(be.isEmpty()) return LightColorProvider.PASS;
            List<Integer> colors = new ArrayList<>();
            for(byte placement : be.get().getActivePlacements()) {
                if(placement == 0) continue;
                if(placement == 1) {
                    colors.add(ColorHelper.brighten(DyeColor.WHITE.getFireworkColor()));
                } else {
                    colors.add(ColorHelper.brighten(DyeColor.values()[placement-HeadlampConstants.DYE_COLOR_OFFSET].getFireworkColor()));
                }
            }
            return ColorHelper.mix(colors);
        });
    }

    private static LightColorProvider createFluidTankProvider(BlockEntityType<? extends FluidTankBlockEntity> beType) {
        return (level, pos, state) -> {
            Optional<? extends FluidTankBlockEntity> be = level.getBlockEntity(pos, beType);
            if(be.isEmpty()) return LightColorProvider.PASS;
            Fluid fluid = be.get().getFluid(0).getFluid();
            ResourceLocation fluidLocation = BuiltInRegistries.FLUID.getKey(fluid);
            return ColorMaps.FLUIDS.getOrPass(fluidLocation);
        };
    }

    private static void addCandleColors() {
        CANDLE_COLORS.put(ResourceLocation.parse("minecraft:red_candle"), 0xFF1A1A);
        CANDLE_COLORS.put(ResourceLocation.parse("minecraft:orange_candle"), 0xFF801A);
        CANDLE_COLORS.put(ResourceLocation.parse("minecraft:yellow_candle"), 0xFFFF1A);
        CANDLE_COLORS.put(ResourceLocation.parse("minecraft:lime_candle"), 0x1AFF1A);
        CANDLE_COLORS.put(ResourceLocation.parse("minecraft:green_candle"), 0x4CFF4C);
        CANDLE_COLORS.put(ResourceLocation.parse("minecraft:cyan_candle"), 0x4CCCFF);
        CANDLE_COLORS.put(ResourceLocation.parse("minecraft:light_blue_candle"), 0x80A6FF);
        CANDLE_COLORS.put(ResourceLocation.parse("minecraft:blue_candle"), 0x1A26FF);
        CANDLE_COLORS.put(ResourceLocation.parse("minecraft:purple_candle"), 0xB24CFF);
        CANDLE_COLORS.put(ResourceLocation.parse("minecraft:magenta_candle"), 0xFF1AFF);
        CANDLE_COLORS.put(ResourceLocation.parse("minecraft:pink_candle"), 0xFF66FF);

        CANDLE_COLORS.put(ResourceLocation.parse("dye_depot:maroon_candle"), 0xFF482B);
        CANDLE_COLORS.put(ResourceLocation.parse("dye_depot:rose_candle"), 0xFF75B3);
        CANDLE_COLORS.put(ResourceLocation.parse("dye_depot:coral_candle"), 0xFF9A73);
        CANDLE_COLORS.put(ResourceLocation.parse("dye_depot:indigo_candle"), 0xAF99FF);
        CANDLE_COLORS.put(ResourceLocation.parse("dye_depot:navy_candle"), 0x639CFF);
        CANDLE_COLORS.put(ResourceLocation.parse("dye_depot:slate_candle"), 0xA1C5FF);
        CANDLE_COLORS.put(ResourceLocation.parse("dye_depot:olive_candle"), 0xFFFC33);
        CANDLE_COLORS.put(ResourceLocation.parse("dye_depot:amber_candle"), 0xFFD000);
        CANDLE_COLORS.put(ResourceLocation.parse("dye_depot:beige_candle"), 0xFFE8BC);
        CANDLE_COLORS.put(ResourceLocation.parse("dye_depot:teal_candle"), 0x6EFFCC);
        CANDLE_COLORS.put(ResourceLocation.parse("dye_depot:mint_candle"), 0x57FFA0);
        CANDLE_COLORS.put(ResourceLocation.parse("dye_depot:aqua_candle"), 0x54FCFF);
        CANDLE_COLORS.put(ResourceLocation.parse("dye_depot:verdant_candle"), 0x6CFF5E);
        CANDLE_COLORS.put(ResourceLocation.parse("dye_depot:forest_candle"), 0x7BFF3D);
        CANDLE_COLORS.put(ResourceLocation.parse("dye_depot:ginger_candle"), 0xFF8A24);
        CANDLE_COLORS.put(ResourceLocation.parse("dye_depot:tan_candle"), 0xFFBF70);
    }

    public static void lightChanged(Level level, BlockPos pos) {
        ContraptionLightsApi.lightColorChanged(level, pos);
    }
}
