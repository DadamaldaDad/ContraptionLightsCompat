package net.dadamalda.contraption_lights_compat.mixin.bits_n_bobs;

import com.kipti.bnb.content.trinkets.light.headlamp.HeadlampBlockEntity;
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

@Mixin(HeadlampBlockEntity.class)
public abstract class HeadlampBlockEntityMixin extends SmartBlockEntity {
    public HeadlampBlockEntityMixin(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Inject(
            method = "read",
            at = @At("TAIL")
    )
    private void injectSomewhere(CompoundTag tag, HolderLookup.Provider registries, boolean clientPacket, CallbackInfo ci) {
        CLCLightColors.lightChanged(level, worldPosition);
    }
}
