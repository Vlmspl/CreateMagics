package net.vladitandlplayer.create_magics.block;

import com.simibubi.create.AllTags;
import com.simibubi.create.api.stress.BlockStressValues;
import com.simibubi.create.foundation.data.SharedProperties;
import com.tterrag.registrate.util.entry.BlockEntry;
import net.createmod.catnip.data.Couple;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.vladitandlplayer.create_magics.CreateMagics;
import net.vladitandlplayer.create_magics.block.custom.mana_powered_motor.ManaPoweredMotorBlock;
import net.vladitandlplayer.create_magics.block.custom.mana_storage.ManaStorageBlock;
import net.vladitandlplayer.create_magics.item.ModCreativeModeTabs;

import static com.simibubi.create.foundation.data.ModelGen.customItemModel;

public class ModBlocks {
    public static final BlockEntry<ManaPoweredMotorBlock> MANA_POWERED_MOTOR = CreateMagics.REGISTRATE
            .block("mana_powered_motor", ManaPoweredMotorBlock::new)
            .initialProperties(SharedProperties::softMetal)
            .tag(AllTags.AllBlockTags.SAFE_NBT.tag)
            .item()
            .tab(ModCreativeModeTabs.MAIN_TAB.getKey())
            .transform(customItemModel())
            .register();

    public static final BlockEntry<ManaStorageBlock> MANA_STORAGE_BLOCK = CreateMagics.REGISTRATE
            .block("mana_storage", ManaStorageBlock::new)
            .initialProperties(SharedProperties::softMetal)
            .tag(AllTags.AllBlockTags.SAFE_NBT.tag)
            .item()
            .tab(ModCreativeModeTabs.MAIN_TAB.getKey())
            .transform(customItemModel())
            .register();

    //So, You might be wondering why this is needed, its needed so this class gets invoked and runs
    public static void register() {

    }
}
