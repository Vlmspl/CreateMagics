package net.vladitandlplayer.create_magics.block;

import com.simibubi.create.AllTags;
import com.simibubi.create.content.kinetics.BlockStressDefaults;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.foundation.data.SharedProperties;
import com.simibubi.create.foundation.utility.Couple;
import com.tterrag.registrate.util.entry.BlockEntry;
import net.minecraft.world.item.CreativeModeTab;
import net.vladitandlplayer.create_magics.CreateMagics;
import net.vladitandlplayer.create_magics.block.custom.mana_powered_motor.ManaPoweredMotorBlock;
import net.vladitandlplayer.create_magics.item.ModCreativeModeTabs;

import java.util.Arrays;

import static com.simibubi.create.foundation.data.ModelGen.customItemModel;

public class ModBlocks {
    public static final BlockEntry<ManaPoweredMotorBlock> MANA_POWERED_MOTOR = CreateMagics.REGISTRATE
            .block("mana_powered_motor", ManaPoweredMotorBlock::new)
            .initialProperties(SharedProperties::softMetal)
            .tag(AllTags.AllBlockTags.SAFE_NBT.tag)
            .transform(BlockStressDefaults.setCapacity(128f))
            .transform(BlockStressDefaults.setGeneratorSpeed(() -> Couple.create(0, 256)))
            .item()
            .tab(() -> ModCreativeModeTabs.MAIN)
            .transform(customItemModel())
            .register();
    //So, You might be wondering why this is needed, its needed so this class gets invoked and runs
    public static void register() {

    }
}
