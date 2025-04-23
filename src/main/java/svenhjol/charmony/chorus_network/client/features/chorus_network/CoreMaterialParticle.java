package svenhjol.charmony.chorus_network.client.features.chorus_network;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.RandomSource;
import svenhjol.charmony.core.client.CustomParticle;

public class CoreMaterialParticle extends CustomParticle {
    public CoreMaterialParticle(ClientLevel level, double x, double y, double z, double vx, double vy, double vz, SpriteSet spriteProvider) {
        super(level, x, y, z, vx, vy, vz, spriteProvider);
        this.quadSize *= 0.5f;
    }

    @Override
    public int getLightColor(float f) {
        return 15728880;
    }

    public static class Provider implements ParticleProvider<SimpleParticleType> {
        private static final RandomSource RANDOM = RandomSource.create();
        private final SpriteSet sprite;

        public Provider(SpriteSet sprite) {
            this.sprite = sprite;
        }

        public Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double r, double g, double b) {
            var particle = new CoreMaterialParticle(level, x, y, z,
                0, 0, 0, this.sprite);
            particle.setLifetime(20 + RANDOM.nextInt(15));

            var d = RANDOM.nextDouble() * 0.25d;

            var dr = (float)Math.max(0d, r - d);
            var dg = (float)Math.max(0d, g - d);
            var db = (float)Math.max(0d, b - d);

            particle.setColor(dr, dg, db);
            particle.setAlpha(1.0f);
            particle.friction = 0.73f; // some multiplier for velocity, idk
            particle.speedUpWhenYMotionIsBlocked = false; // idk
            return particle;
        }
    }
}
