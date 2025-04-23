package svenhjol.charmony.chorus_network.client.features.chorus_network;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.ChestRenderer;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;
import svenhjol.charmony.chorus_network.common.features.chorus_network.ChestBlockEntity;

public class ChorusChestRenderer extends ChestRenderer<ChestBlockEntity> {
    public ChorusChestRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void render(ChestBlockEntity blockEntity, float f, PoseStack poseStack, MultiBufferSource multiBufferSource, int i, int j, Vec3 vec3) {
        super.render(blockEntity, f, poseStack, multiBufferSource, i, j, vec3);

        var pose = poseStack.last().pose();
        var vertexConsumer = multiBufferSource.getBuffer(RenderType.endGateway());

        var sideMinMargin = 0.25f;
        var sideMaxMargin = 0.75f;
        var vertMaxMargin = 0.60f;

        renderFace(pose, vertexConsumer, sideMinMargin, sideMaxMargin, vertMaxMargin, vertMaxMargin, sideMaxMargin, sideMaxMargin, sideMinMargin, sideMinMargin); // Up
    }

    private void renderFace(Matrix4f pose, VertexConsumer vertexConsumer, float f, float g, float h, float i, float j, float k, float l, float m) {
        vertexConsumer.addVertex(pose, f, h, j);
        vertexConsumer.addVertex(pose, g, h, k);
        vertexConsumer.addVertex(pose, g, i, l);
        vertexConsumer.addVertex(pose, f, i, m);
    }
}
