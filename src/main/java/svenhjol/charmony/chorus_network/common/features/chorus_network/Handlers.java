package svenhjol.charmony.chorus_network.common.features.chorus_network;

import net.minecraft.core.BlockPos;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import svenhjol.charmony.api.chorus_network.ChorusCoreMaterial;
import svenhjol.charmony.api.core.Color;
import svenhjol.charmony.core.base.Setup;

@SuppressWarnings("unused")
public class Handlers extends Setup<ChorusNetwork> {
    public Handlers(ChorusNetwork feature) {
        super(feature);
    }

    public boolean canGenerateSeed(LevelAccessor level, BlockPos pos) {
        return level.getRandom().nextInt(1000) < ChorusNetwork.feature().rarity();
    }

    public boolean generateSeed(LevelAccessor level, BlockPos pos) {
        var block = feature().registers.seedBlock.get();
        return level.setBlock(pos, block.defaultBlockState(), 2);
    }

    public void dropCore(Level level, BlockPos pos, ChorusCoreMaterial material) {
        if (level instanceof ServerLevel serverLevel) {
            var item = feature().registers.coreBlockItems.get(material).get();
            var entity = new ItemEntity(serverLevel, pos.getX() + 0.5d, pos.getY() + 0.5d, pos.getZ() + 0.5d, new ItemStack(item));
            level.addFreshEntity(entity);
        }
    }

    public void addMaterialParticle(Level level, BlockPos pos, ChorusCoreMaterial material, RandomSource random, double yOffset, double chance) {
        var particle = feature().registers.particleType;
        var color = new Color(material.getColor());

        if (random.nextDouble() < chance) {
            var x = ((double) pos.getX() + 0.5d);
            var y = ((double) pos.getY() + yOffset);
            var z = ((double) pos.getZ() + 0.5d);

            level.addParticle(particle, x, y, z, color.getRed(), color.getGreen(), color.getBlue());
        }
    }

    public void serverStarted(MinecraftServer server) {
        var state = ChannelSavedData.getServerState(server);
    }
}
