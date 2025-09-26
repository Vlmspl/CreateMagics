package net.vastless.create_magics.item;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllCreativeModeTabs;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import net.vastless.create_magics.CreateMagics;
import net.vastless.create_magics.block.ModBlocks;

public class ModCreativeModeTabs {
    private static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TAB_REGISTER = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, CreateMagics.MOD_ID);

    public static final RegistryObject<CreativeModeTab> MAIN_TAB = CREATIVE_MODE_TAB_REGISTER.register("main",
            () -> CreativeModeTab.builder().title(Component.translatable("itemGroup.create_magics:main")).icon(() ->new ItemStack(ModBlocks.MANA_POWERED_MOTOR.get().asItem())).build());


    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TAB_REGISTER.register(eventBus);


    }
}