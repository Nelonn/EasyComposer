package me.nelonn.easycomposer.rotation;

import org.jetbrains.annotations.NotNull;

public interface Rotation2d extends Pitch, Yaw {

    float pitch();

    float yaw();

    @NotNull
    default Rotation2d with(float pitch, float yaw) {
        return new ImmRotation2d(pitch, yaw);
    }

    @NotNull
    default Rotation2d add(@NotNull Rotation2d value) {
        return with(this.pitch() + value.pitch(), this.yaw() + value.yaw());
    }

    @NotNull
    default Rotation2d lerp(@NotNull Rotation2d value, float delta) {
        return with(lerpAngle(this.pitch(), value.pitch(), delta), lerpAngle(this.yaw(), value.yaw(), delta));
    }

    static float lerpAngle(float start, float target, float delta) {
        float difference = target - start;
        while (difference < -180.0F) difference += 360.0F;
        while (difference >= 180.0F) difference -= 360.0F;
        return start + delta * difference;
    }

    @NotNull
    default MutRotation2d mutableCopy() {
        return new MutRotation2d(this.pitch(), this.yaw());
    }

    Rotation2d ZERO = new ImmRotation2d(0, 0);

}
