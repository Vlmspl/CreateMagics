package net.vladitandlplayer.create_magics.spells.component;

import com.mna.api.affinity.Affinity;
import com.mna.api.spells.ComponentApplicationResult;
import com.mna.api.spells.attributes.Attribute;
import com.mna.api.spells.attributes.AttributeValuePair;
import com.mna.api.spells.base.IModifiedSpellPart;
import com.mna.api.spells.parts.SpellEffect;
import com.mna.api.spells.targeting.SpellContext;
import com.mna.api.spells.targeting.SpellSource;
import com.mna.api.spells.targeting.SpellTarget;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.vladitandlplayer.create_magics.CreateMagics;
import net.vladitandlplayer.create_magics.IManaStorage;
import net.vladitandlplayer.create_magics.block.custom.mana_powered_motor.ManaPoweredMotor;
import net.vladitandlplayer.create_magics.block.custom.mana_powered_motor.ManaPoweredMotorBlockEntity;

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
        BlockEntity entity = world.getBlockEntity(pos);
        return entity instanceof IManaStorage && ((IManaStorage) entity).isConsumer();
    }

    private void applyManaPower(SpellSource spellSource, Level world, BlockPos blockPos) {
        CreateMagics.LOGGER.debug("applyManaPower function was called");
        BlockEntity entity = world.getBlockEntity(blockPos);

        if (entity instanceof IManaStorage) {
            ((IManaStorage) entity).addMana(20);
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
