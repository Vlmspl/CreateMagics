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
import net.minecraft.world.level.block.state.BlockState;
import net.vladitandlplayer.create_magics.CreateMagics;
import net.vladitandlplayer.create_magics.block.custom.mana_powered_motor.ManaPoweredMotor;
import net.vladitandlplayer.create_magics.block.custom.mana_powered_motor.ManaPoweredMotorBlockEntity;

public class ManaRouterComponent extends SpellEffect {
    public ManaRouterComponent(ResourceLocation guiIcon) {
        super(guiIcon,
                new AttributeValuePair(Attribute.RADIUS, 1.0F, 1.0F, 4.0F, 1.0F, 5.0F));
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
            if (canReceivePower(world.getBlockState(blockPos), spellContext.getWorld(), blockPos)) {
                // Example: Apply mana power to the block
                applyManaPower(spellSource, world, blockPos);
                result = ComponentApplicationResult.SUCCESS;
            }
        }

        return result;
    }

    private boolean canReceivePower(BlockState blockState, Level world, BlockPos pos) {
        // Add logic to check if the block can receive power (e.g., check if it's a compatible block)
        ManaPoweredMotorBlockEntity entity = ((ManaPoweredMotor) blockState.getBlock()).getBlockEntity(world, pos);

        return blockState.getBlock() instanceof ManaPoweredMotor && entity.getMana() + 20 < entity.getMaxMana(); // Replace with actual condition
    }

    private void applyManaPower(SpellSource spellSource, Level world, BlockPos blockPos) {
        CreateMagics.LOGGER.debug("applyManaPower function was called");
        BlockState state = world.getBlockState(blockPos);

        ManaPoweredMotorBlockEntity entity = ((ManaPoweredMotor) state.getBlock()).getBlockEntity(world, blockPos);
        entity.addMana(20);
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
