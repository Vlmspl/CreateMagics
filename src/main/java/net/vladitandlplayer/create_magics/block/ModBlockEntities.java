package net.vladitandlplayer.create_magics.block;

import com.simibubi.create.content.kinetics.base.HalfShaftInstance;
import com.simibubi.create.content.kinetics.base.ShaftInstance;
import com.tterrag.registrate.util.entry.BlockEntityEntry;
import net.vladitandlplayer.create_magics.CreateMagics;
import net.vladitandlplayer.create_magics.block.custom.mana_powered_motor.ManaPoweredMotorBlockEntity;
import net.vladitandlplayer.create_magics.block.custom.mana_powered_motor.ManaPoweredMotorRenderer;
import net.vladitandlplayer.create_magics.block.custom.mana_storage.ManaStorageBlockEntity;
import net.vladitandlplayer.create_magics.block.custom.mana_storage.ManaStorageRenderer;

public class ModBlockEntities {
    public static final BlockEntityEntry<ManaPoweredMotorBlockEntity> MANA_POWERED_MOTOR_ENTITY = CreateMagics.REGISTRATE
            .blockEntity("mana_powered_motor", ManaPoweredMotorBlockEntity::new)
            .instance(() -> HalfShaftInstance::new)
            .validBlocks(ModBlocks.MANA_POWERED_MOTOR)
            .renderer(() -> ManaPoweredMotorRenderer::new)
            .register();

    public static final BlockEntityEntry<ManaStorageBlockEntity> MANA_STORAGE_BLOCK_ENTITY = CreateMagics.REGISTRATE
            .blockEntity("mana_storage_block", ManaStorageBlockEntity::new)
            .instance(() -> HalfShaftInstance::new)
            .validBlocks(ModBlocks.MANA_STORAGE_BLOCK)
            .renderer(() -> ManaStorageRenderer::new)
            .register();


    //So, You might be wondering why this is needed, its needed so this class gets invoked and runs
    public static void register() {

    }
}
