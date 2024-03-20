package me.nelonn.easycomposer.math;

public class Quat {
    public float x;
    public float y;
    public float z;
    public float w;

    public Quat() {
    }

    public Quat(float x, float y, float z, float w) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }

    public float x() {
        return x;
    }

    public Quat x(float x) {
        this.x = x;
        return this;
    }

    public float y() {
        return y;
    }

    public Quat y(float y) {
        this.y = y;
        return this;
    }

    public float z() {
        return z;
    }

    public Quat z(float z) {
        this.z = z;
        return this;
    }

    public float w() {
        return w;
    }

    public Quat w(float w) {
        this.w = w;
        return this;
    }

    public Rotator rotator() {
        float SingularityTest = z * x - w * y;
		float YawY = 2.f * (w * z + x * y);
		float YawX = (1.f - 2.f * ((y * y) + (z * z)));

        // reference 
        // http://en.wikipedia.org/wiki/Conversion_between_quaternions_and_Euler_angles
        // http://www.euclideanspace.com/maths/geometry/rotations/conversions/quaternionToEuler/

        // this value was found from experience, the above websites recommend different values
        // but that isn't the case for us, so I went through different testing, and finally found the case 
        // where both of world lives happily. 
		float SINGULARITY_THRESHOLD = 0.4999995f;
		float RAD_TO_DEG = (180.f / (float) Math.PI);
        float Pitch, Yaw, Roll;

        if (SingularityTest < -SINGULARITY_THRESHOLD)
        {
            Pitch = -90.f;
            Yaw = (((float) Math.atan2(YawY, YawX)) * RAD_TO_DEG);
            Roll = Rotator.normalizeAxis(-Yaw - (2.f * ((float) Math.atan2(x, w)) * RAD_TO_DEG));
        }
        else if (SingularityTest > SINGULARITY_THRESHOLD)
        {
            Pitch = 90.f;
            Yaw = (((float) Math.atan2(YawY, YawX)) * RAD_TO_DEG);
            Roll = Rotator.normalizeAxis(Yaw - (2.f * ((float) Math.atan2(x, w)) * RAD_TO_DEG));
        }
        else
        {
            Pitch = (((float) Math.asin(2.f * SingularityTest)) * RAD_TO_DEG);
            Yaw = (((float) Math.atan2(YawY, YawX)) * RAD_TO_DEG);
            Roll =(((float) Math.atan2(-2.f * (w*x + y*z), (1.f - 2.f * ((x * x) + (y * y))))) * RAD_TO_DEG);
        }

        Rotator RotatorFromQuat = new Rotator(Pitch, Yaw, Roll);

        return RotatorFromQuat;
    }

    @Override
    public String toString() {
        return String.format("X=%.9f Y=%.9f Z=%.9f W=%.9f", x, y, z, w);
    }
}
