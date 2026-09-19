package net.dadamalda.contraption_lights_compat.mixin.create_connected;

import com.hlysine.create_connected.content.fluidvessel.FluidVesselBlockEntity;
import com.simibubi.create.content.fluids.tank.FluidTankBlockEntity;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
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

@Mixin(FluidVesselBlockEntity.class)
public abstract class FluidVesselBlockEntityMixin extends FluidTankBlockEntity {
    public FluidVesselBlockEntityMixin(BlockEntityType<?> type, BlockPos pos, BlockState state) {
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
