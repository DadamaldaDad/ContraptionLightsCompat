package net.dadamalda.create_color_grid.mixin;

import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import org.patryk3211.powergrid.electricity.base.ElectricBlockEntity;
import org.patryk3211.powergrid.electricity.light.bulb.LightBulbState;
import org.patryk3211.powergrid.electricity.light.fixture.AbstractLightFixtureBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.atmerek.contraptionlights.light.color.ColorLightRefresh;

import java.util.Optional;

@Mixin(AbstractLightFixtureBlockEntity.class)
public abstract class AbstractLightFixtureBlockEntityMixin extends ElectricBlockEntity {

    @Shadow @Nullable protected LightBulbState bulbState;

    public AbstractLightFixtureBlockEntityMixin(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Inject(method = "read", at = @At("HEAD"))
    private void injectReadAtHead(CompoundTag tag, HolderLookup.Provider registries, boolean clientPacket, CallbackInfo ci, @Share("previousColour") LocalRef<Optional<DyeColor>> lastColour) {
        if(bulbState == null) {
            lastColour.set(Optional.empty());
            return;
        }
        DyeColor dyeColor = bulbState.getColor();
        if(dyeColor == null) {
            lastColour.set(Optional.empty());
            return;
        }
        lastColour.set(Optional.of(dyeColor));
    }

    @Inject(method = "read", at = @At("TAIL"))
    private void injectReaAtTail(CompoundTag tag, HolderLookup.Provider registries, boolean clientPacket, CallbackInfo ci, @Share("previousColour") LocalRef<Optional<DyeColor>> lastColour) {
        if(level == null) return;
        if(!level.isClientSide) return;
        if (bulbState == null) return;
        int powerLevel = bulbState.getPowerLevel();
        if(powerLevel == 0) return;
        DyeColor dyeColor = bulbState.getColor();
        if(lastColour.get().isPresent()) {
            if (dyeColor == null || dyeColor != lastColour.get().get()) {
                ColorLightRefresh.around(worldPosition, powerLevel == 1 ? 10 : 15);
            }
        } else {
            if (dyeColor != null) {
                ColorLightRefresh.around(worldPosition, powerLevel == 1 ? 10 : 15);
            }
        }
    }
}
