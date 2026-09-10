package net.dadamalda.contraption_lights_compat.mixin.create;

import com.simibubi.create.content.fluids.tank.CreativeFluidTankBlockEntity;
import com.simibubi.create.content.fluids.tank.FluidTankBlockEntity;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import net.dadamalda.contraption_lights_compat.CLCLightColors;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.fluids.FluidStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FluidTankBlockEntity.class)
public abstract class FluidTankBlockEntityMixin extends SmartBlockEntity {
    public FluidTankBlockEntityMixin(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Inject(
            method = "onFluidStackChanged",
            at = @At("TAIL")
    )
    private void injectOnFluidStateChange(FluidStack newFluidStack, CallbackInfo ci) {
        if((Object)this instanceof CreativeFluidTankBlockEntity) CLCLightColors.lightChanged(level, worldPosition);
    }
}
