package me.nelonn.easycomposer.rotation;

import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;
import org.joml.Quaternionf;

public interface Rotation3d extends me.nelonn.easycomposer.rotation.Rotation2d, Roll {

    @NotNull
    default Rotation3d add(@NotNull Rotation3d value) {
        return new ImmRotation3d(this.roll() + value.roll(), this.pitch() + value.pitch(), this.yaw() + value.yaw());
    }

    @NotNull
    default Rotation3d lerp(@NotNull Rotation3d value, float delta) {
        return new ImmRotation3d(Mth.lerp(delta, this.roll(), value.roll()), Mth.lerp(delta, this.pitch(), value.pitch()), Mth.lerp(delta, this.yaw(), value.yaw()));
    }

    @Override
    @NotNull
    default me.nelonn.easycomposer.rotation.Rotation2d add(@NotNull me.nelonn.easycomposer.rotation.Rotation2d value) {
        if (value instanceof Rotation3d) {
            Rotation3d rotation3d = (Rotation3d) value;
            return this.add(rotation3d);
        }
        return me.nelonn.easycomposer.rotation.Rotation2d.super.add(value);
    }

    @Override
    @NotNull
    default me.nelonn.easycomposer.rotation.Rotation2d lerp(@NotNull me.nelonn.easycomposer.rotation.Rotation2d value, float delta) {
        if (value instanceof Rotation3d) {
            Rotation3d rotation3d = (Rotation3d) value;
            return this.lerp(rotation3d, delta);
        }
        return Rotation2d.super.lerp(value, delta);
    }

    @NotNull
    default Quaternionf quaternion() {
        Quaternionf quaternionf = new Quaternionf();
        // Rotate local is actually global
        quaternionf.rotateLocalX(pitch());
        quaternionf.rotateLocalY(-yaw());
        quaternionf.rotateLocalZ(roll());
        return quaternionf;
    }

    @NotNull
    default me.nelonn.easycomposer.rotation.MutRotation3d mutableCopy() {
        return new MutRotation3d(this.roll(), this.pitch(), this.yaw());
    }

    Rotation3d ZERO = new ImmRotation3d(0, 0, 0);

}
