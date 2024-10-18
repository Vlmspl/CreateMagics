package net.vladitandlplayer.create_magics.block.custom.mana_storage

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;

import javax.annotation.Nullable;

public class ManaStorage extends Block {
    public ManaStorage() {
        super(BlockBehaviour.Properties.of(Material.STONE));
    }

    @Override
    public void onPlace(BlockState state, Level world, BlockPos pos, BlockState oldState, boolean isMoving) {
        if (!world.isClientSide) {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof ManaStorageBlockEntity) {
                ((ManaStorageBlockEntity) blockEntity).setMana(0); // Initialize or reset mana
            }
        }
    }
}