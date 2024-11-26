package net.vladitandlplayer.create_magics.item;

import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.vladitandlplayer.create_magics.CreateMagics;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, CreateMagics.MOD_ID);

    public static void register(IEventBus bus) {
        ITEMS.register(bus);
    }
}
