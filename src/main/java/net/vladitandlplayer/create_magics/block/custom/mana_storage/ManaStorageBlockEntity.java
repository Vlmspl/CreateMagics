package net.vladitandlplayer.create_magics.block.custom.mana_storage;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.vladitandlplayer.create_magics.CreateMagics;
import net.vladitandlplayer.create_magics.IManaStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ManaStorageBlockEntity extends BlockEntity implements IManaStorage {
    private static final int SEARCH_RADIUS = 5; // Circular search radius
    private float mana = 0; // Current mana
    private static final float MAX_MANA = 8192; // Maximum mana

    public ManaStorageBlockEntity(BlockPos pos, BlockState state) {
        super(CreateMagics.MANA_STORAGE_ENTITY_TYPE, pos, state); // Register your block entity type
    }

    @Override
    public float getMana() {
        return mana;
    }

    @Override
    public float getMaxMana() {
        return MAX_MANA;
    }

    @Override
    public void setMana(float amount) {
        this.mana = Math.max(0, Math.min(MAX_MANA, amount));
        // Mark the block as changed to update its state
        setChanged();
    }

    @Override
    public void addMana(float amount) {
        setMana(mana + amount);
    }

    @Override
    public void subMana(float amount) {
        setMana(mana - amount);
    }

    @Override
    public boolean canReceiveMana(int amount) {
        return getMana() + amount < getMaxMana();
    }

    public InteractionResult use(BlockState blockState, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (!world.isClientSide) {
            if (getMana() >= 10) { // Check if there's enough mana to perform the search
                searchAndTransferMana(world, pos);
                setMana(getMana() - 10); // Deduct the mana cost
            }
        }
        return InteractionResult.SUCCESS;
    }

    private void searchAndTransferMana(Level world, BlockPos pos) {
        List<IManaStorage> nearbyEntities = findNearbyManaReceivers(world, pos, SEARCH_RADIUS); // Use constant for radius

        for (IManaStorage entity : nearbyEntities) {
            // Check if the entity can receive mana
            if (entity.canReceiveMana(1)) { // Check if it can receive at least 1 mana
                // Transfer 1 mana
                entity.addMana(1); // Give 1 mana
            }
        }

        // Emit particles in a circular direction
        emitManaParticles(world, pos);
    }

    private List<IManaStorage> findNearbyManaReceivers(Level world, BlockPos pos, int radius) {
        List<IManaStorage> receivers = new ArrayList<>();

        for (int x = -radius; x <= radius; x++) {
            for (int y = -radius; y <= radius; y++) {
                for (int z = -radius; z <= radius; z++) {
                    BlockPos targetPos = pos.offset(x, y, z);
                    // Check if the target position is within the search radius
                    if (targetPos.distSqr(pos) <= radius * radius) {
                        IManaStorage entity = (IManaStorage) world.getBlockEntity(targetPos);
                        if (entity != null && entity.isConsumer() && !(entity instanceof ManaStorageBlockEntity)) {
                            receivers.add(entity);
                        }
                    }
                }
            }
        }

        return receivers;
    }

    // New method to determine if this block is a consumer of mana
    @Override
    public boolean isConsumer() {
        return true; // This ManaStorage block is a consumer of mana
    }

    private void emitManaParticles(Level world, BlockPos pos) {
        Random random = new Random();
        double radius = 1.5; // Radius of the circle
        int particlesToEmit = 100; // Number of particles to emit

        for (int i = 0; i < particlesToEmit; i++) {
            // Generate a random angle
            double angle = random.nextDouble() * 2 * Math.PI; // Angle in radians

            // Calculate x and z positions for the circular effect
            double x = radius * Math.cos(angle);
            double z = radius * Math.sin(angle);

            // Spawn the particle at the new position (y stays the same)
            world.addParticle(ParticleTypes.HAPPY_VILLAGER,
                    pos.getX() + 0.5 + x, // Offset from the block position
                    pos.getY() + 0.5,      // Keep y constant
                    pos.getZ() + 0.5 + z,
                    0, 0, 0);
        }
    }
}
