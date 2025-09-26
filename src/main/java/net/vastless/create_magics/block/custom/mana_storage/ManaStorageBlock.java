package net.vastless.create_magics.block.custom.mana_storage;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.vastless.create_magics.block.ModBlockEntities;
import org.jetbrains.annotations.Nullable;

public class ManaStorageBlock extends BaseEntityBlock {

    public ManaStorageBlock(Properties properties) {
        super(properties.noOcclusion());
    }


    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return ModBlockEntities.MANA_STORAGE_BLOCK_ENTITY.create(blockPos, blockState);
    }

    @Override
    public RenderShape getRenderShape(BlockState p_49232_) {
        return RenderShape.MODEL;
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
        if(level.isClientSide()) {
            return null;
        }

        return createTickerHelper(blockEntityType, ModBlockEntities.MANA_STORAGE_BLOCK_ENTITY.get(),
                (level1, pos, state1, blockEntity) -> blockEntity.tick(level1, pos, state1));
    }

    

}
