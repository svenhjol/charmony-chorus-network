package svenhjol.charmony.chorus_network.client.features.chorus_network;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;

public class CoreMaterialParticle extends TextureSheetParticle {
    private final double xStart;
    private final double yStart;
    private final double zStart;

    public CoreMaterialParticle(ClientLevel level, double x, double y, double z, double vx, double vy, double vz) {
        super(level, x, y, z);
        this.xd = vx;
        this.yd = vy;
        this.zd = vz;
        this.x = x;
        this.y = y;
        this.z = z;
        this.xStart = this.x;
        this.yStart = this.y;
        this.zStart = this.z;
        this.quadSize = 0.1F * (this.random.nextFloat() * 0.2F + 0.5F);
        float j = this.random.nextFloat() * 0.6F + 0.4F;
        this.rCol = j * 0.9F;
        this.gCol = j * 0.3F;
        this.bCol = j;
        this.lifetime = (int)(Math.random() * 10.0) + 40;
    }


    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_OPAQUE;
    }

    @Override
    public void move(double d, double e, double f) {
        this.setBoundingBox(this.getBoundingBox().move(d, e, f));
        this.setLocationFromBoundingbox();
    }

    @Override
    public float getQuadSize(float f) {
        float g = (this.age + f) / this.lifetime;
        g = 1.0F - g;
        g *= g;
        g = 1.0F - g;
        return this.quadSize * g;
    }

    @Override
    public int getLightColor(float f) {
        return 15728880;
    }

    @Override
    public void tick() {
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;
        if (this.age++ >= this.lifetime) {
            this.remove();
        } else {
            float f = (float)this.age / this.lifetime;
            float var3 = -f + f * f * 2.0F;
            float var4 = 1.0F - var3;
            this.x = this.xStart + this.xd * var4;
            this.y = this.yStart + this.yd * var4 + (1.0F - f);
            this.z = this.zStart + this.zd * var4;
        }
    }

    public static class Provider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprite;

        public Provider(SpriteSet sprite) {
            this.sprite = sprite;
        }

        public Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double r, double g, double b) {
            var random = level.getRandom();
            var ix = random.nextInt(2) * 2 - 1;
            var iz = random.nextInt(2) * 2 - 1;
            double vx = random.nextDouble() * ix;
            double vy = random.nextDouble() * (random.nextBoolean() ? ix : iz);
            double vz = random.nextDouble() * iz;

            var particle = new CoreMaterialParticle(level, x, y, z,
                vx, vy, vz);

            var d = random.nextDouble() * 0.25d;

            var dr = (float)Math.max(0d, r - d);
            var dg = (float)Math.max(0d, g - d);
            var db = (float)Math.max(0d, b - d);

            particle.setColor(dr, dg, db);
            particle.setAlpha(1.0f);
            particle.setLifetime(20 + random.nextInt(20));
            particle.pickSprite(sprite);
//            particle.friction = 0.73f; // some multiplier for velocity, idk
//            particle.speedUpWhenYMotionIsBlocked = false; // idk
            return particle;
        }
    }
}
