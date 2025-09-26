package net.vastless.create_magics.item;

import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.vastless.create_magics.CreateMagics;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, CreateMagics.MOD_ID);

    public static final RegistryObject<Item> CRYSTAL_ITEM = ITEMS.register("crystal",
            () -> new Item(new Item.Properties()));

    public static void register(IEventBus bus) {
        ITEMS.register(bus);
    }
}
