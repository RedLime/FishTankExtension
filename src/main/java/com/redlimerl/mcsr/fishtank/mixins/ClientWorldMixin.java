package com.redlimerl.mcsr.fishtank.mixins;

import com.redlimerl.mcsr.fishtank.FishTank;
import com.redlimerl.speedrunigt.timer.InGameTimer;
import com.redlimerl.speedrunigt.timer.TimerStatus;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.FishEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.profiler.Profiler;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.MutableWorldProperties;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.function.Supplier;

@Mixin(ClientWorld.class)
public abstract class ClientWorldMixin extends World {

    protected ClientWorldMixin(MutableWorldProperties mutableWorldProperties, RegistryKey<World> registryKey, RegistryKey<DimensionType> registryKey2, DimensionType dimensionType, Supplier<Profiler> profiler, boolean bl, boolean bl2, long l) {
        super(mutableWorldProperties, registryKey, registryKey2, dimensionType, profiler, bl, bl2, l);
    }

    private final int playerRange = 8;
    private BlockPos lastWaterBlockPos = BlockPos.ORIGIN;

    @Inject(method = "addEntity", at = @At("TAIL"))
    public void onEntityUpdate(int id, Entity entity, CallbackInfo ci) {
        if (entity instanceof FishEntity && lastWaterBlockPos != BlockPos.ORIGIN
                && entity.squaredDistanceTo(lastWaterBlockPos.getX(), lastWaterBlockPos.getY(), lastWaterBlockPos.getZ()) < playerRange) {
            InGameTimer timer = InGameTimer.getInstance();

            if (timer.getCategory() == FishTank.FISH_TANK_CATEGORY && timer.getStatus() != TimerStatus.NONE
                    && FishTank.checkFishTank(this, (FishEntity) entity)) {
                InGameTimer.complete();
            }
        }
    }

    @Inject(method = "updateListeners", at = @At("TAIL"))
    public void onBlockUpdate(BlockPos pos, BlockState oldState, BlockState newState, int flags, CallbackInfo ci) {
        InGameTimer timer = InGameTimer.getInstance();

        if (timer.getCategory() == FishTank.FISH_TANK_CATEGORY && timer.getStatus() != TimerStatus.NONE) {
            if (flags == 11 && newState.getBlock() == Blocks.WATER) {
                lastWaterBlockPos = pos;
            }

            List<FishEntity> fishEntityList = this.getEntities(FishEntity.class, new Box(pos).expand(playerRange * 4), LivingEntity::isAlive);
            for (FishEntity fishEntity : fishEntityList) {
                if (FishTank.checkFishTank(this, fishEntity)) InGameTimer.complete();
            }
        }
    }

}
