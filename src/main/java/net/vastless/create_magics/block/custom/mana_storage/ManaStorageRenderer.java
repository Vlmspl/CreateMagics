package net.vastless.create_magics.block.custom.mana_storage;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.vastless.create_magics.item.ModItems;
import org.joml.Quaternionf;

public class ManaStorageRenderer implements BlockEntityRenderer<ManaStorageBlockEntity> {
    public ManaStorageRenderer(BlockEntityRendererProvider.Context context) {}

    // Table: {px0, py0, pz0, px1, py1, pz1, rotX, rotY, rotZ, scale}
    private static final float[][] CRYSTAL_TRANSFORMS = {
            {0.5f, 0.7f, 0.5f, 0.5f, 0.2f, 0.5f, 180f, 0f, 0f, 1f},       // central
            {0.47f, 0.375f, 0.63f, 0.47f, 0.075f, 0.778f, 157.5f, 0f, 0f, 2f/3f},
            {0.53f, 0.375f, 0.37f, 0.53f, 0.075f, 0.222f, 202.5f, 0f, 0f, 2f/3f},
            {0.37f, 0.375f, 0.47f, 0.222f, 0.075f, 0.47f, 180f, 0f, 22.5f, 2f/3f},
            {0.63f, 0.375f, 0.53f, 0.778f, 0.075f, 0.53f, 180f, 0f, -22.5f, 2f/3f}
    };

    @Override
    public void render(ManaStorageBlockEntity entity, float partialTicks, PoseStack poseStack,
                       MultiBufferSource bufferSource, int packedLight, int packedOverlay) {


        ItemStack crystalStack = new ItemStack(ModItems.CRYSTAL_ITEM.get());
        int light = getLightLevel(entity.getLevel(), entity.getBlockPos());

        int[] result = calculateCrystals(entity.getMana(), entity.getMaxMana());
        int crystalsToRender = result[0];
        float lastScaleFactor = Float.intBitsToFloat(result[1]);

        for (int i = 0; i < crystalsToRender; i++) {
            float scale = CRYSTAL_TRANSFORMS[i][9];
            if (i == crystalsToRender - 1) {
                scale *= lastScaleFactor;
            }

            // interpolate positions between 0-scale and 1-scale
            float lerpFactor = scale/CRYSTAL_TRANSFORMS[i][9];
            float px = CRYSTAL_TRANSFORMS[i][0] + ((CRYSTAL_TRANSFORMS[i][3] - CRYSTAL_TRANSFORMS[i][0]) * lerpFactor);
            float py = CRYSTAL_TRANSFORMS[i][1] + ((CRYSTAL_TRANSFORMS[i][4] - CRYSTAL_TRANSFORMS[i][1]) * lerpFactor);
            float pz = CRYSTAL_TRANSFORMS[i][2] + ((CRYSTAL_TRANSFORMS[i][5] - CRYSTAL_TRANSFORMS[i][2]) * lerpFactor);

            renderCrystal(crystalStack, poseStack, bufferSource, entity.getLevel(), entity.getBlockPos(),
                    px, py, pz,
                    CRYSTAL_TRANSFORMS[i][6], CRYSTAL_TRANSFORMS[i][7], CRYSTAL_TRANSFORMS[i][8],
                    scale, light
            );
        }
    }

    private int[] calculateCrystals(float currentMana, float maxMana) {
        float halfMax = maxMana / 2f;
        if (currentMana <= halfMax) {
            float factor = currentMana / halfMax;
            return new int[]{1, Float.floatToIntBits(factor)}; // central crystal
        } else {
            float remaining = currentMana - halfMax;
            float segment = halfMax / 4f; // outer 4 crystals
            int fullCrystals = (int)(remaining / segment); // floor division
            fullCrystals = Math.min(fullCrystals, 4); // clamp max 4 outer crystals

            // Only count partial if strictly less than next segment start
            float lastScale = (remaining % segment) / segment;
            boolean hasPartial = lastScale > 0f && fullCrystals < 4;
            int crystalsToRender = 1 + fullCrystals + (hasPartial ? 1 : 0); // central + full + partial
            if (!hasPartial) lastScale = 1f; // last crystal is full scale if no partial
            return new int[]{crystalsToRender, Float.floatToIntBits(lastScale)};
        }
    }


    private void renderCrystal(ItemStack stack, PoseStack poseStack, MultiBufferSource bufferSource,
                               Level level, BlockPos pos,
                               float px, float py, float pz,
                               float rotX, float rotY, float rotZ,
                               float scale, int light) {

        poseStack.pushPose();
        poseStack.translate(px, py, pz);
        poseStack.mulPose(Axis.XP.rotationDegrees(rotX));
        poseStack.mulPose(Axis.YP.rotationDegrees(rotY));
        poseStack.mulPose(Axis.ZP.rotationDegrees(rotZ));
        poseStack.scale(scale, scale, scale);

        Minecraft.getInstance().getItemRenderer().renderStatic(
                stack,
                ItemDisplayContext.FIXED,
                light,
                OverlayTexture.NO_OVERLAY,
                poseStack,
                bufferSource,
                level,
                1
        );
        poseStack.popPose();
    }

    private int getLightLevel(Level level, BlockPos pos) {
        int bLight = level.getBrightness(LightLayer.BLOCK, pos);
        int sLight = level.getBrightness(LightLayer.SKY, pos);
        return LightTexture.pack(bLight, sLight);
    }
}
