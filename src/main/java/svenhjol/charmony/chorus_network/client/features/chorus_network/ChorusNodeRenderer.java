package svenhjol.charmony.chorus_network.client.features.chorus_network;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;
import svenhjol.charmony.chorus_network.common.features.chorus_network.ChorusNodeBlockEntity;

public class ChorusNodeRenderer<T extends ChorusNodeBlockEntity> implements BlockEntityRenderer<T> {
    public ChorusNodeRenderer(BlockEntityRendererProvider.Context context) {}

    /**
     * @see net.minecraft.client.renderer.blockentity.TheEndPortalRenderer
     */
    @Override
    public void render(T node, float f, PoseStack poseStack, MultiBufferSource multiBufferSource, int i, int j, Vec3 vec3) {
        var pose = poseStack.last().pose();
        var vertexConsumer = multiBufferSource.getBuffer(RenderType.endGateway());

        var sideMinMargin = 0.01f;
        var sideMaxMargin = 0.99f;
        var vertMinMargin = 0.01f;
        var vertMaxMargin = 0.99f;

        renderFace(pose, vertexConsumer, sideMinMargin, sideMaxMargin, vertMinMargin, vertMaxMargin, sideMaxMargin, sideMaxMargin, sideMaxMargin, sideMaxMargin); // South
        renderFace(pose, vertexConsumer, sideMinMargin, sideMaxMargin, vertMaxMargin, vertMinMargin, sideMinMargin, sideMinMargin, sideMinMargin, sideMinMargin); // North
        renderFace(pose, vertexConsumer, sideMaxMargin, sideMaxMargin, vertMaxMargin, vertMinMargin, sideMinMargin, sideMaxMargin, sideMaxMargin, sideMinMargin); // East
        renderFace(pose, vertexConsumer, sideMinMargin, sideMinMargin, vertMinMargin, vertMaxMargin, sideMinMargin, sideMaxMargin, sideMaxMargin, sideMinMargin); // West
        renderFace(pose, vertexConsumer, sideMinMargin, sideMaxMargin, vertMinMargin, vertMinMargin, sideMinMargin, sideMinMargin, sideMaxMargin, sideMaxMargin); // Down
        renderFace(pose, vertexConsumer, sideMinMargin, sideMaxMargin, vertMaxMargin, vertMaxMargin, sideMaxMargin, sideMaxMargin, sideMinMargin, sideMinMargin); // Up
    }

    private void renderFace(Matrix4f pose, VertexConsumer vertexConsumer, float f, float g, float h, float i, float j, float k, float l, float m) {
        vertexConsumer.addVertex(pose, f, h, j);
        vertexConsumer.addVertex(pose, g, h, k);
        vertexConsumer.addVertex(pose, g, i, l);
        vertexConsumer.addVertex(pose, f, i, m);
    }

    @Override
    public int getViewDistance() {
        return 68;
    }
}
