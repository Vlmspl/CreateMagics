package net.vladitandlplayer.create_magics.item;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.vladitandlplayer.create_magics.block.ModBlocks;

public class ModCreativeModeTabs {
    public static final CreativeModeTab MAIN = new CreativeModeTab("main_tab") {
        @Override
        public Component getDisplayName() {
            return Component.translatable("itemGroup.create_magics:main");
        }

        @Override
        public ItemStack makeIcon() {
            return new ItemStack(ModBlocks.MANA_POWERED_MOTOR.get());
        }
    };

}
