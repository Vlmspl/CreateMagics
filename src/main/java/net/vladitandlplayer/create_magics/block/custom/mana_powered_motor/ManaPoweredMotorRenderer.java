package net.vladitandlplayer.create_magics.block.custom.mana_powered_motor;

import com.simibubi.create.AllPartialModels;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityRenderer;
import com.simibubi.create.foundation.render.CachedBufferer;
import com.simibubi.create.foundation.render.SuperByteBuffer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;

public class ManaPoweredMotorRenderer extends KineticBlockEntityRenderer {
    public ManaPoweredMotorRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    protected SuperByteBuffer getRotatedModel(KineticBlockEntity be, BlockState state) {
        Direction facing = state.getValue(ManaPoweredMotorBlock.FACING).getOpposite(); // Get opposite direction
        BlockState updatedState = state.setValue(ManaPoweredMotorBlock.FACING, facing); // Update the state with flipped direction
        return CachedBufferer.partialFacing(AllPartialModels.SHAFT_HALF, updatedState, facing); // Pass both updated state and direction
    }




}
