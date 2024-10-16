package net.vladitandlplayer.create_magics.block.custom.mana_powered_motor;

import com.simibubi.create.content.kinetics.base.DirectionalKineticBlock;
import com.simibubi.create.foundation.block.IBE;
import com.simibubi.create.foundation.utility.VoxelShaper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.vladitandlplayer.create_magics.Utils.Shapes;
import net.vladitandlplayer.create_magics.block.ModBlockEntities;

public class ManaPoweredMotor extends DirectionalKineticBlock implements IBE<ManaPoweredMotorBlockEntity> {
    public static final VoxelShaper MANA_POWERED_MOTOR = Shapes.shape(0, 2, 0, 16, 13, 16).forDirectional();
    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;

    public ManaPoweredMotor(Properties properties) {
        super(properties);
        registerDefaultState(defaultBlockState().setValue(POWERED, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(POWERED);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        return MANA_POWERED_MOTOR.get(state.getValue(FACING));
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Direction preferred = getPreferredFacing(context);
        if ((context.getPlayer() != null && context.getPlayer().isShiftKeyDown()) || preferred == null)
            return super.getStateForPlacement(context);
        return defaultBlockState().setValue(FACING, preferred);
    }

    @Override
    public Class<ManaPoweredMotorBlockEntity> getBlockEntityClass() {
        return ManaPoweredMotorBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends ManaPoweredMotorBlockEntity> getBlockEntityType() {
        return ModBlockEntities.MANA_POWERED_MOTOR_ENTITY.get();
    }

    @Override
    public boolean hasShaftTowards(LevelReader world, BlockPos pos, BlockState state, Direction face) {
        return face == state.getValue(FACING);
    }

    @Override
    public Direction.Axis getRotationAxis(BlockState state) {
        return state.getValue(FACING).getAxis();
    }


    @Override
    public boolean hideStressImpact() {
        return true;
    }

    public void setPowered(Level world, BlockPos pos, boolean powered) {
        world.setBlock(pos, world.getBlockState(pos).setValue(POWERED, powered), 3);
    }

    public void neighborChanged(BlockState state, Level world, BlockPos pos, Block block, BlockPos from, boolean b) {
        if (!world.isClientSide) {
            boolean flag = state.getValue(POWERED);
            if (flag != world.hasNeighborSignal(pos)) {
                if (flag){
                    setPowered(world, pos, false);
                    world.scheduleTick(pos, this, 4);
                }
                else{
                    setPowered(world, pos, true);
                    world.setBlock(pos, state.cycle(POWERED), 2);
                }
            }
        }
    }

    @Override
    public void tick(BlockState state, ServerLevel world, BlockPos pos, RandomSource pRandom) {
        if (state.getValue(POWERED) && !world.hasNeighborSignal(pos))
            world.setBlock(pos, state.cycle(POWERED), 2);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return ModBlockEntities.MANA_POWERED_MOTOR_ENTITY.create(pos, state);
    }
}
