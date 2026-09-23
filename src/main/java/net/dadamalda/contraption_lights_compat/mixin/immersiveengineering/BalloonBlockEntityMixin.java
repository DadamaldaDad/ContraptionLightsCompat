package net.dadamalda.contraption_lights_compat.mixin.immersiveengineering;

import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces;
import blusunrize.immersiveengineering.common.blocks.cloth.BalloonBlockEntity;
import blusunrize.immersiveengineering.common.blocks.generic.ImmersiveConnectableBlockEntity;
import net.dadamalda.contraption_lights_compat.CLCLightColors;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BalloonBlockEntity.class)
public abstract class BalloonBlockEntityMixin extends ImmersiveConnectableBlockEntity implements IEBlockInterfaces.IPlayerInteraction, IEBlockInterfaces.IHammerInteraction, IEBlockInterfaces.IBlockBounds {
    public BalloonBlockEntityMixin(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Inject(
            method = "readCustomNBT",
            at = @At("RETURN")
    )
    private void injectReadCustomNBT(CompoundTag nbt, boolean descPacket, HolderLookup.Provider provider, CallbackInfo ci) {
        CLCLightColors.lightChanged(level, worldPosition);
    }

    @Inject(
            method = "interact",
            at = @At("RETURN")
    )
    private void injectInteract(Direction side, Player player, InteractionHand hand, ItemStack heldItem, float hitX, float hitY, float hitZ, CallbackInfoReturnable<ItemInteractionResult> cir) {
        CLCLightColors.lightChanged(level, worldPosition);
    }
}
