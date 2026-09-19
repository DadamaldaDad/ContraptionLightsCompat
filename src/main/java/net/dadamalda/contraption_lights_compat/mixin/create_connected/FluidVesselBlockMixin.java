package net.dadamalda.contraption_lights_compat.mixin.create_connected;

import com.hlysine.create_connected.content.fluidvessel.FluidVesselBlock;
import net.dadamalda.contraption_lights_compat.CLCLightColors;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FluidVesselBlock.class)
public class FluidVesselBlockMixin {
    @Inject(
            method = "useItemOn",
            at = @At("RETURN")
    )
    private void injectUseItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult, CallbackInfoReturnable<ItemInteractionResult> cir) {
        CLCLightColors.lightChanged(level, pos);
    }
}
