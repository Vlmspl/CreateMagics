package net.vladitandlplayer.create_magics.Utils;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.vladitandlplayer.create_magics.IManaStorage;

public class Utils {
    public static IManaStorage  HasManaStorage(Level world, BlockPos pos) {
        BlockEntity entity = world.getBlockEntity(pos);
        // Check if the block entity or the block itself is an instance of IManaStorage
        if (entity instanceof IManaStorage) {
            return (IManaStorage) entity;
        }
        return null;
    }

}
