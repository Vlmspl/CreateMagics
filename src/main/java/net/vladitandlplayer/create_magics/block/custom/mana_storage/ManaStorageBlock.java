package net.vladitandlplayer.create_magics.block.custom.mana_storage;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.vladitandlplayer.create_magics.block.ModBlockEntities;
import org.jetbrains.annotations.Nullable;

public class ManaStorageBlock extends BaseEntityBlock {

    public ManaStorageBlock(Properties properties) {
        super(properties);
    }


    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return ModBlockEntities.MANA_STORAGE_BLOCK_ENTITY.create(blockPos, blockState);
    }
}
