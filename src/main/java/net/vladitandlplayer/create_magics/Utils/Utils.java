package net.vladitandlplayer.create_magics.Utils;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.vladitandlplayer.create_magics.IManaStorage;
import net.vladitandlplayer.create_magics.block.custom.mana_storage.ManaStorageBlockEntity;

public class Utils {
    public static IManaStorage  HasManaStorage(Level world, BlockPos pos) {
        BlockEntity entity = world.getBlockEntity(pos);
        // Check if the block entity or the block itself is an instance of IManaStorage
        if (entity instanceof IManaStorage) {
            return (IManaStorage) entity;
        }
        return null;
    }

    public static IManaStorage HasManaStorageBlockAround(Level level, BlockPos pos) {

        // Directions: Right, Left, Forward, Back, Down, Up
        Direction[] directions = Direction.values();

        // Loop through all directions to check for adjacent blocks
        for (Direction direction : directions) {
            // Get the position of the adjacent block
            BlockPos adjacentPos = pos.relative(direction);

            // Get the BlockEntity at the adjacent position
            BlockEntity adjacentEntity = level.getBlockEntity(adjacentPos);

            // Check if the adjacent entity is an instance of ManaStorageBlockEntity
            if (adjacentEntity instanceof ManaStorageBlockEntity) {
                // Return the IManaStorage of that block entity
                return (IManaStorage) adjacentEntity;
            }
        }

        // If no ManaStorageBlockEntity is found, return null
        return null;
    }


}
