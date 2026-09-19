package net.dadamalda.contraption_lights_compat.mixin.createpropulsion;

import com.simibubi.create.content.fluids.tank.FluidTankBlockEntity;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import dev.propulsionteam.propulsionsimulated.content.platinum.PlatinumFluidTankBlockEntity;
import net.dadamalda.contraption_lights_compat.CLCLightColors;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlatinumFluidTankBlockEntity.class)
public abstract class PlatinumFluidTankBlockEntityMixin extends SmartBlockEntity {
    public PlatinumFluidTankBlockEntityMixin(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Inject(
            method = "read",
            at = @At("RETURN")
    )
    private void injectRead(CompoundTag compound, HolderLookup.Provider registries, boolean clientPacket, CallbackInfo ci) {
        CLCLightColors.lightChanged(level, worldPosition);
    }
}
