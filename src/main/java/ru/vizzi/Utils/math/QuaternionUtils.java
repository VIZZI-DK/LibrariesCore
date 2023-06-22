package ru.vizzi.Utils.math;


import org.lwjgl.util.vector.Quaternion;
import org.lwjgl.util.vector.Vector3f;

public class QuaternionUtils {

    public static final float PIOVER180 = (float) (Math.PI / 180f);

    public static Vector3f multiplyQuaternionByVector3(Quaternion q, Vector3f v) {
        float num = q.x * 2f;
        float num2 = q.y * 2f;
        float num3 = q.z * 2f;
        float num4 = q.x * num;
        float num5 = q.y * num2;
        float num6 = q.z * num3;
        float num7 = q.x * num2;
        float num8 = q.x * num3;
        float num9 = q.y * num3;
        float num10 = q.w * num;
        float num11 = q.w * num2;
        float num12 = q.w * num3;
        Vector3f result = new Vector3f();
        result.x = (1f - (num5 + num6)) * v.x + (num7 - num12) * v.y + (num8 + num11) * v.z;
        result.y = (num7 + num12) * v.x + (1f - (num4 + num6)) * v.y + (num9 - num10) * v.z;
        result.z = (num8 - num11) * v.x + (num9 + num10) * v.y + (1f - (num4 + num5)) * v.z;
        return result;
    }

    public static Quaternion angleAxis(float degress, Vector3f axis) {
        Quaternion result = new Quaternion();
        float radians = degress * PIOVER180;
        radians *= 0.5f;
        float scale = (float) Math.sin(radians);
        float x = axis.x * scale;
        float y = axis.y * scale;
        float z = axis.z * scale;
        result.x = x;
        result.y = y;
        result.z = z;
        result.w = (float) Math.cos(radians);

        return result.normalise(result);
    }

    public static Quaternion euler(float pitch, float yaw, float roll) {
        float p = pitch * PIOVER180 / 2.0f;
        float y = yaw * PIOVER180 / 2.0f;
        float r = roll * PIOVER180 / 2.0f;
        float sinp = (float) Math.sin(p);
        float siny = (float) Math.sin(y);
        float sinr = (float) Math.sin(r);
        float cosp = (float) Math.cos(p);
        float cosy = (float) Math.cos(y);
        float cosr = (float) Math.cos(r);
        float x = sinr * cosp * cosy - cosr * sinp * siny;
        y = cosr * sinp * cosy + sinr * cosp * siny;
        float z = cosr * cosp * siny - sinr * sinp * cosy;
        float w = cosr * cosp * cosy + sinr * sinp * siny;

        Quaternion quaternion = new Quaternion(x, y, z, w);
        return quaternion.normalise(quaternion);
    }

    public static void toEulerAngles(Quaternion q, Vector3f dest) {
//        float sqw = q.w * q.w;
//        float sqx = q.x * q.x;
//        float sqy = q.y * q.y;
//        float sqz = q.z * q.z;
//        float unit = sqx + sqy + sqz + sqw; // if normalised is one, otherwise is correction factor
//        float test = q.x * q.w - q.y * q.z;
//
//        if (test > 0.4995f * unit)
//        { // singularity at north pole
//            dest.y = (float) (2f * Math.atan2(q.y, q.x));
//            dest.x = (float) (Math.PI / 2f);
//            dest.z = 0;
//            NormalizeAngles(dest);
//            return;
//        }
//        if (test < -0.4995f * unit)
//        { // singularity at south pole
//            dest.y = (float) (-2f * Math.atan2(q.y, q.x));
//            dest.x = (float) (-Math.PI / 2);
//            dest.z = 0;
//            NormalizeAngles(dest);
//            return;
//        }
//
//        Quaternion rot = new Quaternion(q.w, q.z, q.x, q.y);
//        dest.y = (float) Math.atan2(2f * rot.x * rot.w + 2f * rot.y * rot.z, 1 - 2f * (rot.z * rot.z + rot.w * rot.w));     // Yaw
//        dest.x = (float) Math.asin(2f * (rot.x * rot.z - rot.w * rot.y));                             // Pitch
//        dest.z = (float) Math.atan2(2f * rot.x * rot.y + 2f * rot.z * rot.w, 1 - 2f * (rot.y * rot.y + rot.z * rot.z));      // Roll
//        NormalizeAngles(dest);


        float sinr_cosp = 2f * (q.w * q.x + q.y * q.z);
        float cosr_cosp = 1f - 2f * (q.x * q.x + q.y * q.y);
        dest.x = (float) Math.atan2(sinr_cosp, cosr_cosp);

        float sinp = 2f * (q.w * q.y - q.z * q.x);
        if (Math.abs(sinp) >= 1f)
            dest.y = (float) Math.copySign(Math.PI / 2.0, sinp);
        else
            dest.y = (float) Math.asin(sinp);

        float siny_cosp = 2f * (q.w * q.z + q.x * q.y);
        float cosy_cosp = 1f - 2f * (q.y * q.y + q.z * q.z);
        dest.z = (float) Math.atan2(siny_cosp, cosy_cosp);
    }

    static void NormalizeAngles(Vector3f angles)
    {
        angles.x = NormalizeAngle(angles.x);
        angles.y = NormalizeAngle(angles.y);
        angles.z = NormalizeAngle(angles.z);
    }

    static float NormalizeAngle(float angle)
    {
        while (angle > Math.PI * 2f)
            angle -= Math.PI * 2f;
        while (angle < 0)
            angle += Math.PI * 2f;
        return angle;
    }

    public static Quaternion slerp(Quaternion q1, Quaternion q2, double lambda) {
        float dotProduct = q1.x * q2.x + q1.y * q2.y + q1.z * q2.z + q1.w * q2.w;
        float theta, st, sut, sout, coeff1, coeff2;

        lambda = lambda / 2.0;

        theta = (float) Math.acos(dotProduct);
        if (theta < 0.0) theta = -theta;

        st = (float) Math.sin(theta);
        sut = (float) Math.sin(lambda * theta);
        sout = (float) Math.sin((1 - lambda) * theta);
        coeff1 = sout / st;
        coeff2 = sut / st;

        Quaternion quaternion = new Quaternion(
                coeff1 * q1.x + coeff2 * q2.x,
                coeff1 * q1.y + coeff2 * q2.y,
                coeff1 * q1.z + coeff2 * q2.z,
                coeff1 * q1.w + coeff2 * q2.w
        );

        return quaternion.normalise(quaternion);
    }

    public static Quaternion inverse(Quaternion quaternion) {
        Quaternion quaternion2 = new Quaternion();
        float num2 = (((quaternion.x * quaternion.x) + (quaternion.y * quaternion.y)) + (quaternion.z * quaternion.z)) + (quaternion.w * quaternion.w);
        float num = 1f / num2;
        quaternion2.x = -quaternion.x * num;
        quaternion2.y = -quaternion.y * num;
        quaternion2.z = -quaternion.z * num;
        quaternion2.w = quaternion.w * num;
        return quaternion2;
    }
}
