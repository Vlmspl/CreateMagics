package net.vladitandlplayer.create_magics.block;

import com.simibubi.create.content.kinetics.base.HalfShaftInstance;
import com.tterrag.registrate.util.entry.BlockEntityEntry;
import net.vladitandlplayer.create_magics.CreateMagics;
import net.vladitandlplayer.create_magics.block.custom.mana_powered_motor.ManaPoweredMotorBlockEntity;
import net.vladitandlplayer.create_magics.block.custom.mana_powered_motor.ManaPoweredMotorRenderer;

public class ModBlockEntities {
    public static final BlockEntityEntry<ManaPoweredMotorBlockEntity> MANA_POWERED_MOTOR_ENTITY = CreateMagics.REGISTRATE
            .blockEntity("mana_powered_motor", ManaPoweredMotorBlockEntity::new)
            .instance(() -> HalfShaftInstance::new)
            .validBlocks(ModBlocks.MANA_POWERED_MOTOR)
            .renderer(() -> ManaPoweredMotorRenderer::new)
            .register();

    //So, You might be wondering why this is needed, its needed so this class gets invoked and runs
    public static void register() {

    }
}
