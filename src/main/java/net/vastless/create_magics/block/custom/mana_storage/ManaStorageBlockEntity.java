package net.vastless.create_magics.block.custom.mana_storage;

import com.simibubi.create.api.equipment.goggles.IHaveGoggleInformation;
import com.simibubi.create.api.equipment.goggles.IHaveHoveringInformation;
import com.simibubi.create.foundation.blockEntity.SyncedBlockEntity;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.vastless.create_magics.IManaStorage;

import java.util.List;

public class ManaStorageBlockEntity extends SyncedBlockEntity implements IManaStorage, IHaveHoveringInformation {
    float Mana = 0.0f;
    private float prevMana = 0.0f;
    final float MAX_MANA = 15000.0f;
    String spacing = "     ";

    public ManaStorageBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }


    @Override
    public boolean addToTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
        tooltip.add(Component.literal(spacing).append(Component.translatable("Mana: " + String.format("%.2f", getMana()) + "/" + MAX_MANA).withStyle(ChatFormatting.AQUA)));
        return true;
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.putFloat("manaStored", Mana);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        Mana = tag.getFloat("manaStored");
    }

    @Override
    public float getMana() {
        return Mana;
    }

    @Override
    public float getMaxMana() {
        return MAX_MANA;
    }

    @Override
    public void setMana(float amount) {
        Mana = amount;
        notifyUpdate();
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
        return false;
    }

    @Override
    public boolean isConsumer() {
        return true;
    }

    public void tick(Level level1, BlockPos pos, BlockState state1) {

    }
}
