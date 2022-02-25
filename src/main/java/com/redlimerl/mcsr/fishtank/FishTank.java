package com.redlimerl.mcsr.fishtank;

import com.redlimerl.speedrunigt.timer.running.RunCategory;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.block.AbstractGlassBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.passive.FishEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class FishTank implements ClientModInitializer {

    public static final RunCategory FISH_TANK_CATEGORY = new RunCategory("fishtank", "mc_juice#FishTank", "FishTank");

    @Override
    public void onInitializeClient() {
    }

    public static boolean checkFishTank(World world, FishEntity fish) {
        BlockPos pos = fish.getBlockPos();

        for (int x = 0; x < 3; x++) {
            for (int z = 0; z < 3; z++) {
                for (int y = 0; y < 2; y++) {
                    BlockState blockState = world.getBlockState(pos.add(x - 1, y - 1, z - 1));
                    if (x == 1 && z == 1 && y == 1 && blockState.getBlock() == Blocks.WATER) {
                        continue;
                    }
                    if (!(blockState.getBlock() instanceof AbstractGlassBlock)) {
                        return false;
                    }
                }
            }
        }

        return checkFishIsSafe(world, fish);
    }

    private static boolean checkFishIsSafe(World world, FishEntity fish) {
        BlockPos fishPos = fish.getBlockPos();
        BlockState fishTankRoof = world.getBlockState(fishPos.add(0, 1, 0));

        boolean isLavaSafe = true;
        if (!fishTankRoof.getMaterial().isSolid()) {
            for (int x = 0; x < 7; x++) {
                for (int y = 0; y < 5; y++) {
                    for (int z = 0; z < 7; z++) {
                        BlockPos blockPos = fishPos.add(x - 4, y, z - 4);
                        BlockState blockState = world.getBlockState(blockPos);
                        if (blockState.getBlock() == Blocks.LAVA) {
                            isLavaSafe = false;
                            break;
                        }
                    }
                }
            }
        }

        return isLavaSafe;
    }
}
