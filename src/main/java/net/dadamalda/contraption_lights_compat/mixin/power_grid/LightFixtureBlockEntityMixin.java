package net.dadamalda.contraption_lights_compat.mixin.power_grid;

import net.dadamalda.contraption_lights_compat.CLCLightColors;
import net.minecraft.core.BlockPos;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.patryk3211.powergrid.electricity.light.fixture.AbstractLightFixtureBlockEntity;
import org.patryk3211.powergrid.electricity.light.fixture.LightFixtureBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LightFixtureBlockEntity.class)
public abstract class LightFixtureBlockEntityMixin extends AbstractLightFixtureBlockEntity {

    public LightFixtureBlockEntityMixin(BlockEntityType<?> type, BlockPos pos, BlockState state, boolean dyeable) {
        super(type, pos, state, dyeable);
    }

    @Inject(
            method = "setColor",
            at = @At("HEAD")
    )
    private void injectSetColor(DyeColor color, CallbackInfoReturnable<ItemInteractionResult> cir) {
        CLCLightColors.lightChanged(level, worldPosition);
    }
}
