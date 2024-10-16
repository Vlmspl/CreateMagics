package net.vladitandlplayer.create_magics.block.custom.mana_powered_motor;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.content.kinetics.base.GeneratingKineticBlockEntity;
import com.simibubi.create.content.kinetics.motor.KineticScrollValueBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.CenteredSideValueBoxTransform;
import com.simibubi.create.foundation.blockEntity.behaviour.scrollValue.ScrollValueBehaviour;
import com.simibubi.create.foundation.utility.Lang;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.vladitandlplayer.create_magics.CreateMagics;
import net.vladitandlplayer.create_magics.block.ModBlocks;

import java.util.List;

public class ManaPoweredMotorBlockEntity extends GeneratingKineticBlockEntity {
    protected ScrollValueBehaviour generatedSpeed;

    private boolean cc_update_rpm = false;
    private int cc_new_rpm = 32;

    private boolean active = false;

    public ManaPoweredMotorBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
        super.addBehaviours(behaviours);

        CenteredSideValueBoxTransform slot =
                new CenteredSideValueBoxTransform((motor, side) -> {
                    Direction facing = motor.getValue(ManaPoweredMotor.FACING);
                    Direction.Axis axis = facing.getAxis();
                    // Determine the perpendicular direction based on the axis
                    Direction perpendicular = axis == Direction.Axis.X ? (facing.getClockWise()) : facing.getOpposite();

                    return side == perpendicular;
                });

        generatedSpeed = new KineticScrollValueBehaviour(Lang.translateDirect("generic.speed"), this, slot);
        generatedSpeed.between(-256, 256);
        generatedSpeed.value = 32;
        generatedSpeed.withCallback(i -> this.updateGeneratedRotation(i));
        behaviours.add(generatedSpeed);
    }


    public static int step(ScrollValueBehaviour.StepContext context) {
        int current = context.currentValue;
        int step = 1;

        if (!context.shift) {
            int magnitude = Math.abs(current) - (context.forward == current > 0 ? 0 : 1);

            if (magnitude >= 4) step *= 4;
            if (magnitude >= 32) step *= 4;
            if (magnitude >= 128) step *= 4;
        }

        return step;
    }

    public float calculateAddedStressCapacity() {
        float capacity = 128;
        this.lastCapacityProvided = capacity;
        return capacity;
    }

    @Override
    public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
        super.addToGoggleTooltip(tooltip, isPlayerSneaking);
        tooltip.add(Component.literal(spacing).append(Component.translatable(CreateMagics.MOD_ID + ".tooltip.energy.consumption").withStyle(ChatFormatting.GRAY)));
        return true;
    }

    public void updateGeneratedRotation(int i) {
        super.updateGeneratedRotation();
        cc_new_rpm = i;
    }

    @Override
    public void initialize() {
        super.initialize();
        if (!hasSource() || getGeneratedSpeed() > getTheoreticalSpeed())
            updateGeneratedRotation();
    }

    @Override
    protected Block getStressConfigKey() {
        return AllBlocks.WATER_WHEEL.get();
    }

    @Override
    public void read(CompoundTag compound, boolean clientPacket) {
        super.read(compound, clientPacket);
        active = compound.getBoolean("active");
    }

    @Override
    public void write(CompoundTag compound, boolean clientPacket) {
        super.write(compound, clientPacket);
        compound.putBoolean("active", active);
    }

    @Override
    public void lazyTick() {
        super.lazyTick();
        cc_antiSpam = 5;

    }

    int cc_antiSpam = 0;
    boolean first = true;

    @Override
    public void tick() {
        super.tick();
        if(first) {
            updateGeneratedRotation();
            first = false;
        }

        if(cc_update_rpm && cc_antiSpam > 0) {
            generatedSpeed.setValue(cc_new_rpm);
            cc_update_rpm = false;
            cc_antiSpam--;
            updateGeneratedRotation();
        }

        //Old Lazy
        if(level.isClientSide()) return;
        if(!active) {
            if(!getBlockState().getValue(ManaPoweredMotor.POWERED)) {
                active = true;
                updateGeneratedRotation();
            }
        }
        else {
            if(getBlockState().getValue(ManaPoweredMotor.POWERED)) {
                active = false;
                updateGeneratedRotation();
            }
        }
    }

    @Override
    public float getGeneratedSpeed() {
        if (!ModBlocks.MANA_POWERED_MOTOR.has(getBlockState())) return 0;
        return convertToDirection(active ? generatedSpeed.getValue() : 0, getBlockState().getValue(ManaPoweredMotor.FACING));
    }


    public static int getDurationAngle(int deg, float initialProgress, float speed) {
        speed = Math.abs(speed);
        deg = Math.abs(deg);
        if(speed < 0.1f) return 0;
        double degreesPerTick = (speed * 360) / 60 / 20;
        return (int) ((1 - initialProgress) * deg / degreesPerTick + 1);
    }

    public static int getDurationDistance(int dis, float initialProgress, float speed) {
        speed = Math.abs(speed);
        dis = Math.abs(dis);
        if(speed < 0.1f) return 0;
        double metersPerTick = speed / 512;
        return (int) ((1 - initialProgress) * dis / metersPerTick);
    }

    public boolean setRPM(int rpm) {
        rpm = Math.max(Math.min(rpm, 256), -256);
        cc_new_rpm = rpm;
        cc_update_rpm = true;
        return cc_antiSpam > 0;
    }

    public int getRPM() {
        return cc_new_rpm;//generatedSpeed.getValue();
    }

    public int getGeneratedStress() {
        return (int) calculateAddedStressCapacity();
    }



}
