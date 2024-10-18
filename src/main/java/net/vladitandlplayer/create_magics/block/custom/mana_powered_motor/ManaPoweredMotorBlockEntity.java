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
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.vladitandlplayer.create_magics.CreateMagics;
import net.vladitandlplayer.create_magics.ManaStorage;
import net.vladitandlplayer.create_magics.block.ModBlocks;

import java.util.List;

public class ManaPoweredMotorBlockEntity extends GeneratingKineticBlockEntity implements ManaStorage {
    private Direction getMappedDirection(Direction toMap) {
        if (toMap == Direction.SOUTH) {
            return Direction.WEST;
        } else if (toMap == Direction.NORTH) {
            return Direction.EAST;
        } else if (toMap == Direction.WEST) {
            return Direction.SOUTH;
        } else if (toMap == Direction.EAST) {
            return Direction.NORTH;
        } else if (toMap == Direction.UP) {
            return Direction.WEST;
        } else if (toMap == Direction.DOWN) {
            return Direction.EAST;
        }
        return null;
    }

    protected ScrollValueBehaviour generatedSpeed;

    private boolean cc_update_rpm = false;
    private int cc_new_rpm = 32;

    private boolean active = false;

    // Mana storage variable
    private float mana = 0.0f;
    private static final float MAX_MANA = 1000.0f; // Define a maximum mana capacity
    private static final float BASE_MANA_CONSUMPTION = 0.1f; // Define a base mana consumption value
    private static final float MAX_STRESS = 8192.0f; // Define a maximum stress capacity, adjust as needed



    public ManaPoweredMotorBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
        super.addBehaviours(behaviours);

        CenteredSideValueBoxTransform slot =
                new CenteredSideValueBoxTransform((motor, side) -> side == getMappedDirection(motor.getValue(ManaPoweredMotor.FACING)));

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
        float speed = Math.abs(generatedSpeed.getValue());

        // Mathematical calculation for stress capacity
        float capacity = (speed <= 10)
                ? MAX_STRESS
                : Math.max(512, Math.min(MAX_STRESS, -(speed / 256.0f * MAX_STRESS - MAX_STRESS)));

        this.lastCapacityProvided = capacity;
        return capacity;
    }




    @Override
    public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
        super.addToGoggleTooltip(tooltip, isPlayerSneaking);
        tooltip.add(Component.literal(spacing).append(Component.translatable(CreateMagics.MOD_ID + ".tooltip.energy.consumption").withStyle(ChatFormatting.GRAY)));
        tooltip.add(Component.literal(spacing)
                .append(Component.translatable("Mana: " + String.format("%.2f", mana) + "/" + MAX_MANA).withStyle(ChatFormatting.AQUA)));
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
        mana = compound.getFloat("manaStored"); // Corrected to getFloat
    }

    @Override
    public void write(CompoundTag compound, boolean clientPacket) {
        super.write(compound, clientPacket);
        compound.putBoolean("active", active);
        compound.putFloat("manaStored", mana); // Save as float for more precision
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
        if (first) {
            updateGeneratedRotation();
            first = false;
        }

        if (cc_update_rpm && cc_antiSpam > 0) {
            generatedSpeed.setValue(cc_new_rpm);
            cc_update_rpm = false;
            cc_antiSpam--;
            updateGeneratedRotation();
        }

        // Old Lazy
        if (level.isClientSide()) return;

        // Check for mana presence
        if (mana > 0) {
            // Calculate mana consumption based on speed
            float speed = Math.abs(generatedSpeed.getValue());
            float manaConsumption = BASE_MANA_CONSUMPTION * (speed / 32.0f); // Adjust the divisor for balance

            if (!active) {
                // If not active and sufficient mana, activate the motor
                if (mana >= manaConsumption) {
                    active = true;
                    updateGeneratedRotation();
                }
            } else {
                // If active, check if we have enough mana
                if (mana >= manaConsumption) {
                    mana -= manaConsumption; // Consume mana
                    updateGeneratedRotation();
                } else {
                    // If not enough mana, deactivate the motor and save mana for later
                    active = false;
                    updateGeneratedRotation();
                }
            }
        } else {
            // If there's no mana, deactivate the motor
            if (active) {
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
        if (speed < 0.1f) return 0;
        double degreesPerTick = (speed * 360) / 60 / 20;
        return (int) ((1 - initialProgress) * deg / degreesPerTick + 1);
    }

    public static int getDurationDistance(int dis, float initialProgress, float speed) {
        speed = Math.abs(speed);
        dis = Math.abs(dis);
        if (speed < 0.1f) return 0;
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
        return cc_new_rpm; //generatedSpeed.getValue();
    }

    public int getGeneratedStress() {
        return (int) calculateAddedStressCapacity();
    }


    @Override
    public float getMana() {
        return mana;
    }

    public float getMaxMana() {
        return MAX_MANA;
    }

    @Override
    public void setMana(float amount) {
        mana = amount;
    }

    @Override
    public void addMana(float amount) {
        mana += amount;
    }

    @Override
    public void subMana(float amount) {
        mana -= amount;
    }
}
