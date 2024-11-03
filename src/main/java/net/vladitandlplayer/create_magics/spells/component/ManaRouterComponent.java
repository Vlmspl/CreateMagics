package net.vladitandlplayer.create_magics.spells.component;

import com.mna.api.affinity.Affinity;
import com.mna.api.spells.ComponentApplicationResult;
import com.mna.api.spells.base.IModifiedSpellPart;
import com.mna.api.spells.parts.SpellEffect;
import com.mna.api.spells.targeting.SpellContext;
import com.mna.api.spells.targeting.SpellSource;
import com.mna.api.spells.targeting.SpellTarget;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.vladitandlplayer.create_magics.CreateMagics;
import net.vladitandlplayer.create_magics.IManaStorage;
import net.vladitandlplayer.create_magics.Utils.Utils;

public class ManaRouterComponent extends SpellEffect {
    public ManaRouterComponent(ResourceLocation guiIcon) {
        super(guiIcon);
    }

    @Override
    public ComponentApplicationResult ApplyEffect(SpellSource spellSource, SpellTarget spellTarget, IModifiedSpellPart<SpellEffect> iModifiedSpellPart, SpellContext spellContext) {
        ComponentApplicationResult result = ComponentApplicationResult.FAIL;

        // Check if the target is a block
        if (spellTarget.isBlock()) {
            // Get the targeted block
            BlockPos blockPos = spellTarget.getBlock();
            Level world = spellContext.getServerWorld();

            // Check if the block can receive power
            if (canReceivePower(spellContext.getWorld(), blockPos)) {
                // Example: Apply mana power to the block
                applyManaPower(spellSource, world, blockPos);
                result = ComponentApplicationResult.SUCCESS;
            }
        }

        return result;
    }

    private boolean canReceivePower(Level world, BlockPos pos) {
        IManaStorage storage = Utils.HasManaStorage(world, pos);
        return storage != null && storage.isConsumer();
    }

    private void applyManaPower(SpellSource spellSource, Level world, BlockPos blockPos) {
        IManaStorage storage = Utils.HasManaStorage(world, blockPos);

        if (storage != null) {
            CreateMagics.LOGGER.debug("applyManaPower function was called");
            storage.addMana(20);
        }

    }

    @Override
    public Affinity getAffinity() {
        return Affinity.UNKNOWN;
    }

    @Override
    public float initialComplexity() {
        return 1.0f;
    }

    @Override
    public int requiredXPForRote() {
        return 500;
    }
}
