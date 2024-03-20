package me.nelonn.easycomposer.math;

public class Rotator {
    private float pitch;
    private float yaw;
    private float roll;

    public Rotator() {
    }

    public Rotator(float pitch, float yaw, float roll) {
        this.pitch = pitch;
        this.yaw = yaw;
        this.roll = roll;
    }

    public float pitch() {
        return pitch;
    }

    public Rotator pitch(float pitch) {
        this.pitch = pitch;
        return this;
    }

    public float yaw() {
        return yaw;
    }

    public Rotator yaw(float yaw) {
        this.yaw = yaw;
        return this;
    }

    public float roll() {
        return roll;
    }

    public Rotator roll(float roll) {
        this.roll = roll;
        return this;
    }

    public me.nelonn.easycomposer.math.Quat quaternion() {
        float DEG_TO_RAD = (((float) Math.PI) / (180.f));
        float RADS_DIVIDED_BY_2 = DEG_TO_RAD/2.f;
        float SP, SY, SR;
        float CP, CY, CR;

	    float PitchNoWinding = (float) Math.IEEEremainder(pitch, 360.0f);
	    float YawNoWinding = (float) Math.IEEEremainder(yaw, 360.0f);
	    float RollNoWinding = (float) Math.IEEEremainder(roll, 360.0f);

        float tempPitch = PitchNoWinding * RADS_DIVIDED_BY_2;
        float tempYaw = YawNoWinding * RADS_DIVIDED_BY_2;
        float tempRoll = RollNoWinding * RADS_DIVIDED_BY_2;

        SP = (float) Math.sin(tempPitch);
        CP = (float) Math.cos(tempPitch);
        SY = (float) Math.sin(tempYaw);
        CY = (float) Math.cos(tempYaw);
        SR = (float) Math.sin(tempRoll);
        CR = (float) Math.cos(tempRoll);

        me.nelonn.easycomposer.math.Quat RotationQuat = new Quat();
        RotationQuat.x( CR*SP*SY - SR*CP*CY);
        RotationQuat.y(-CR*SP*CY - SR*CP*SY);
        RotationQuat.z( CR*CP*SY - SR*SP*CY);
        RotationQuat.w( CR*CP*CY + SR*SP*SY);

        return RotationQuat;
    }

    @Override
    public String toString() {
        return String.format("P=%.1f Y=%.1f R=%.1f", pitch, yaw, roll);
    }

    public static float normalizeAxis(float degrees) {
        degrees %= 360.0F;
        if (degrees >= 180.0F) {
            degrees -= 360.0F;
        } else if (degrees < -180.0F) {
            degrees += 360.0F;
        }
        return degrees;
    }
}
