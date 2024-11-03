package net.vladitandlplayer.create_magics.block.custom.mana_storage;

import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.vladitandlplayer.create_magics.IManaStorage;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

import java.util.ArrayList;
import java.util.List;

public class ManaStorageBlockEntity extends KineticBlockEntity implements IManaStorage {
    public float Mana = 0;
    public final float MaxMana = 10000;

    // Constants for mana transmission
    private static final float ManaTransmissionPerTick = 10.0f; // Adjust the value as needed
    private static final int TransmissionRadius = 5; // Define the radius for mana usage

    public ManaStorageBlockEntity(BlockEntityType<?> typeIn, BlockPos pos, BlockState state) {
        super(typeIn, pos, state);
    }

    @Override
    public void tick() {
        super.tick();

        if (!this.level.isClientSide) {
            // Handle mana transmission to connected blocks
            List<IManaStorage> connectedBlocks = findConnectedBlocks();

            if (!connectedBlocks.isEmpty()) {
                float manaToTransmit = ManaTransmissionPerTick / connectedBlocks.size();
                for (IManaStorage block : connectedBlocks) {
                    if (block.canReceiveMana((int) manaToTransmit)) {
                        block.addMana(manaToTransmit);
                    }
                }
            }

            // Optional: Regenerate mana or any logic you want to implement
            if (Mana < MaxMana) {
                addMana(1); // Adjust the rate of regeneration
            }
        }
    }

    private List<IManaStorage> findConnectedBlocks() {
        List<IManaStorage> connectedBlocks = new ArrayList<>();
        AABB searchArea = new AABB(worldPosition).inflate(TransmissionRadius); // Create a search area

        // Iterate through blocks in the search area
        for (BlockPos pos : BlockPos.betweenClosed(worldPosition.offset(-TransmissionRadius, -TransmissionRadius, -TransmissionRadius),
                worldPosition.offset(TransmissionRadius, TransmissionRadius, TransmissionRadius))) {
            if (level.getBlockEntity(pos) instanceof IManaStorage manaBlock && !pos.equals(worldPosition)) {
                connectedBlocks.add(manaBlock);
            }
        }
        return connectedBlocks;
    }

    public void useMana(Player player) {
        // Check if the player's hand is empty
        if (!player.getMainHandItem().isEmpty()) {
            return; // The player must have an empty hand to use mana
        }

        // Emit particles in a sphere around the block
        if (level instanceof ServerLevel serverLevel) {
            double radius = TransmissionRadius; // Radius of the sphere
            Vec3 center = new Vec3(worldPosition.getX() + 0.5, worldPosition.getY() + 0.5, worldPosition.getZ() + 0.5);
            for (int i = 0; i < 100; i++) {
                double x = center.x + (level.random.nextDouble() - 0.5) * 2 * radius;
                double y = center.y + (level.random.nextDouble() - 0.5) * 2 * radius;
                double z = center.z + (level.random.nextDouble() - 0.5) * 2 * radius;
                serverLevel.sendParticles(ParticleTypes.HAPPY_VILLAGER, x, y, z, 1, 0, 0, 0, 0); // Change particle type as needed
            }
        }

        // Further logic for using mana can be added here
    }

    @Override
    public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
        super.addToGoggleTooltip(tooltip, isPlayerSneaking);
        tooltip.add(Component.literal(" ")
                .append(Component.translatable("Mana: " + String.format("%.2f", Mana) + "/" + MaxMana).withStyle(ChatFormatting.AQUA)));
        return true;
    }

    @Override
    public float calculateStressApplied() {
        float impact = 32f;
        this.lastStressApplied = impact;
        return impact;
    }

    @Override
    public void read(CompoundTag compound, boolean clientPacket) {
        super.read(compound, clientPacket);
        Mana = compound.getFloat("manaStored");
    }

    @Override
    public void write(CompoundTag compound, boolean clientPacket) {
        super.write(compound, clientPacket);
        compound.putFloat("manaStored", Mana);
    }

    @Override
    public float getMana() {
        return Mana;
    }

    @Override
    public float getMaxMana() {
        return MaxMana;
    }

    @Override
    public void setMana(float amount) {
        Mana = Math.max(0, Math.min(MaxMana, amount));
    }

    @Override
    public void addMana(float amount) {
        setMana(Mana + amount);
    }

    @Override
    public void subMana(float amount) {
        setMana(Mana - amount);
    }

    @Override
    public boolean canReceiveMana(int amount) {
        return getMana() + amount < getMaxMana();
    }

    @Override
    public boolean isConsumer() {
        return true;
    }
}
