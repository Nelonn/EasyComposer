package me.nelonn.easycomposer.rotation;

import me.nelonn.easycomposer.utility.ECMath;

public class ImmRotation3d extends ImmRotation2d implements Rotation3d {
    private final float roll;

    public ImmRotation3d(float roll, float pitch, float yaw) {
        super(pitch, yaw);
        this.roll = ECMath.normalize360rad(roll);
    }

    @Override
    public float roll() {
        return this.roll;
    }

}
