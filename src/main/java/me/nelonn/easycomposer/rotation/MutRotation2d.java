package me.nelonn.easycomposer.rotation;

import me.nelonn.easycomposer.utility.ECMath;
import org.jetbrains.annotations.NotNull;

public class MutRotation2d implements Rotation2d {
    private float pitch;
    private float yaw;

    public MutRotation2d(float pitch, float yaw) {
        this.pitch = ECMath.normalize360rad(pitch);
        this.yaw = ECMath.normalize360rad(yaw);
    }

    @Override
    public float pitch() {
        return pitch;
    }

    @NotNull
    public MutRotation2d pitch(float pitch) {
        this.pitch = ECMath.normalize360rad(pitch);
        return this;
    }

    @Override
    public float yaw() {
        return yaw;
    }

    @NotNull
    public MutRotation2d yaw(float yaw) {
        this.yaw = ECMath.normalize360rad(yaw);
        return this;
    }
}
