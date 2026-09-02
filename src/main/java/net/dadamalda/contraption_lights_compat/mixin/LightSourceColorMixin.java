package net.dadamalda.contraption_lights_compat.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import org.patryk3211.powergrid.collections.ModdedBlockEntities;
import org.patryk3211.powergrid.collections.ModdedBlocks;
import org.patryk3211.powergrid.electricity.light.bulb.LightBulbState;
import org.patryk3211.powergrid.electricity.light.fixture.LightFixtureBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.atmerek.contraptionlights.light.color.ColorLightFeature;
import xyz.atmerek.contraptionlights.light.color.LightSourceColor;

import java.awt.*;
import java.util.Optional;

@Mixin(LightSourceColor.class)
public class LightSourceColorMixin {
    @Inject(
            method = "of(Lnet/minecraft/world/level/BlockGetter;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;)[F",
            at = @At("HEAD"),
            cancellable = true
    )
    private static void injectOf(BlockGetter level, BlockPos pos, BlockState state, CallbackInfoReturnable<float[]> cir) {
        if(!ColorLightFeature.isEnabled()) return;
        if(state.is(ModdedBlocks.LIGHT_FIXTURE)) {
            Optional<LightFixtureBlockEntity> be = level.getBlockEntity(pos, ModdedBlockEntities.LIGHT_FIXTURE.get());
            if(be.isEmpty()) return;
            LightBulbState bulbState = be.get().getBulbState();
            if(bulbState == null) return;
            DyeColor dyeColor = bulbState.getColor();
            if(dyeColor == null) {
                cir.setReturnValue(LightSourceColor.WHITE);
                return;
            }
            Color color = new Color(dyeColor.getFireworkColor());
            cir.setReturnValue(color.getRGBColorComponents(null));
        }
    }
}
