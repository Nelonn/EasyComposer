package me.nelonn.easycomposer.rotation;

import me.nelonn.easycomposer.utility.ECMath;

public class ImmRotation2d implements Rotation2d {
    private final float pitch;
    private final float yaw;

    public ImmRotation2d(float pitch, float yaw) {
        this.pitch = ECMath.normalize360rad(pitch);
        this.yaw = ECMath.normalize360rad(yaw);
    }

    @Override
    public float pitch() {
        return pitch;
    }

    @Override
    public float yaw() {
        return yaw;
    }

}
