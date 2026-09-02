package net.dadamalda.contraption_lights_compat.mixin;

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
import xyz.atmerek.contraptionlights.light.color.ColorLightRefresh;

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
        if(level == null) return;
        if(!level.isClientSide) return;
        int powerLevel = getPowerLevel();
        if(powerLevel == 0) return;
        ColorLightRefresh.around(worldPosition, powerLevel == 1 ? 10 : 15);
        /*
        PacketDistributor.sendToPlayersTrackingChunk(
                level,
                new ChunkPos(worldPosition),
                new ColouredLightUpdateData(worldPosition, powerLevel == 1 ? 10 : 15)
        );
        */
    }
}
