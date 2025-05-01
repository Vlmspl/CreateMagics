package net.vladitandlplayer.create_magics.block.custom.mana_storage;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.level.block.entity.BlockEntity;

public class ManaStorageRenderer implements BlockEntityRenderer<ManaStorageBlockEntity> {
    public ManaStorageRenderer(BlockEntityRendererProvider.Context context) {}

    @Override
    public void render(ManaStorageBlockEntity entity, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        // No rendering yet — will add mana visuals later
    }
}
