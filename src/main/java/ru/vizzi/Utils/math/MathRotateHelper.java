package ru.vizzi.Utils.math;

import org.lwjgl.util.vector.Quaternion;
import org.lwjgl.util.vector.Vector3f;

public class MathRotateHelper {

    public static void rotateAboutZ(Vector3f vec, double a) {
        float newX = (float) (Math.cos(a) * vec.x - Math.sin(a) * vec.y);
        vec.y = (float) (Math.sin(a) * vec.x + Math.cos(a) * vec.y);
        vec.x = newX;
    }

    public static void rotateAboutY(Vector3f vec, double a) {
        float newX = (float) (Math.cos(a) * vec.x + Math.sin(a) * vec.z);
        vec.z = (float) (-Math.sin(a) * vec.x + Math.cos(a) * vec.z);
        vec.x = newX;
    }

    public static void rotateAboutX(Vector3f vec, double a) {
        float newY = (float) (Math.cos(a) * vec.y - Math.sin(a) * vec.z);
        vec.z = (float) (Math.sin(a) * vec.y + Math.cos(a) * vec.z);
        vec.y = newY;
    }

    public static void rotateAround(Vector3f pos, Quaternion rotation, Vector3f center, Vector3f axis, float angle){
        Quaternion rot = QuaternionUtils.angleAxis(angle, axis); // get the desired rotation
//		Vector3f dir  = pos.translate(-center.x, -center.y, -center.z); // find current direction relative to center

//		dir = QuaternionUtils.multiplyQuaternionByVector3(rot, dir); // rotate the direction
//		pos.x = center.x + dir.x;// define new position
//		pos.y = center.y + dir.y;
//		pos.z = center.z + dir.z;
        // rotate object to keep looking at the center:
        Quaternion myRot = new Quaternion(rotation);
        myRot = QuaternionUtils.inverse(myRot);
        Quaternion q = Quaternion.mul(myRot, rot, null);
        Quaternion q1 = Quaternion.mul(q, myRot, null);
        Quaternion.mul(rotation, q1, rotation);
    }
}

