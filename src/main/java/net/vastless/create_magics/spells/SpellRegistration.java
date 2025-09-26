package net.vastless.create_magics.spells;

import com.mna.Registries;
import com.mna.api.ManaAndArtificeMod;
import com.mna.api.spells.parts.SpellEffect;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegisterEvent;
import net.vastless.create_magics.CreateMagics;
import net.vastless.create_magics.spells.component.ManaRouterComponent;

@Mod.EventBusSubscriber(modid = CreateMagics.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class SpellRegistration {
    public static final SpellEffect MANA_ROUTE = new ManaRouterComponent(new ResourceLocation("create_magics", "textures/spell/component/mana_route.png"));

    @SubscribeEvent
    public static void registerComponents(RegisterEvent event) {
        if (!ModList.get().isLoaded(ManaAndArtificeMod.ID)) {
            return;
        }

        event.register(((IForgeRegistry) Registries.SpellEffect.get()).getRegistryKey(), (helper) -> {
                helper.register(new ResourceLocation("create_magics", "components/mana_route"), MANA_ROUTE);
        });
    }
}
