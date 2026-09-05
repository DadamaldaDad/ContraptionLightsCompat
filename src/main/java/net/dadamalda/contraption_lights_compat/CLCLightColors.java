package net.dadamalda.contraption_lights_compat;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.Level;
import net.neoforged.fml.ModList;
import org.patryk3211.powergrid.collections.ModdedBlockEntities;
import org.patryk3211.powergrid.collections.ModdedBlocks;
import org.patryk3211.powergrid.electricity.light.bulb.LightBulbState;
import org.patryk3211.powergrid.electricity.light.fixture.LightFixtureBlockEntity;
import xyz.atmerek.contraptionlights.api.ContraptionLightsApi;
import xyz.atmerek.contraptionlights.api.LightColorProvider;

import java.util.Optional;

public class CLCLightColors {
    private static final int WHITE = 0xFFFFFF;
    private static final int WARM = 0xFF914D;

    public static void register() {
        if(ModList.get().isLoaded("powergrid")) registerPowerGrid();
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

    public static void lightChanged(Level level, BlockPos pos) {
        ContraptionLightsApi.lightColorChanged(level, pos);
    }
}
