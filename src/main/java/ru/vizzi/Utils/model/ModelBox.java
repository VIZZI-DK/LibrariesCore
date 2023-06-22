package ru.vizzi.Utils.model;

import net.minecraft.util.AxisAlignedBB;
import org.lwjgl.util.vector.Vector3f;
import ru.vizzi.Utils.math.MathRotateHelper;

import java.util.ArrayList;
import java.util.List;

public class ModelBox {
    public final Vector3f min = new Vector3f(), max = new Vector3f();
    private final AxisAlignedBB rotatedModelBox = AxisAlignedBB.getBoundingBox(0,0,0,0,0,0);
    public List<Vector3f> points = new ArrayList<>();

    private final List<Vector3f> rotatedPoints = new ArrayList<>();
    private final Vector3f rot = new Vector3f();
    private final Vector3f scale = new Vector3f();
    private final Vector3f translate = new Vector3f();

    ModelBox(Vector3f min, Vector3f max, String path) {
        if(min == null) throw new RuntimeException("Box creation error: " + path);

        this.min.x = min.x;
        this.min.y = min.y;
        this.min.z = min.z;
        this.max.x = max.x;
        this.max.y = max.y;
        this.max.z = max.z;

        float xs = 1f;
        float ys = 1f;
        float zs = 1f;

//		box = AxisAlignedBB.getBoundingBox(min.x * xs, min.y * ys, min.z * zs, max.x * xs, max.y * ys, max.z * zs);

        points.add(new Vector3f(min.x * xs, min.y * ys, min.z * zs));
        points.add(new Vector3f(max.x * xs, min.y * ys, min.z * zs));
        points.add(new Vector3f(max.x * xs, min.y * ys, max.z * zs));
        points.add(new Vector3f(min.x * xs, min.y * ys, max.z * zs));

        points.add(new Vector3f(min.x * xs, max.y * ys, min.z * zs));
        points.add(new Vector3f(max.x * xs, max.y * ys, min.z * zs));
        points.add(new Vector3f(max.x * xs, max.y * ys, max.z * zs));
        points.add(new Vector3f(min.x * xs, max.y * ys, max.z * zs));

        for(Vector3f point : points) {
            rotatedPoints.add(new Vector3f(point));
        }
    }

    private List<Vector3f> getRotatedPoints(Vector3f rot, Vector3f scale) {
        if (!this.rot.equals(rot) || !scale.equals(this.scale)) {
            int i = 0;
            for (Vector3f vect : points) {
                Vector3f rotatedPoint = rotatedPoints.get(i);
                rotatedPoint.x = vect.x * scale.x;
                rotatedPoint.y = vect.y * scale.y;
                rotatedPoint.z = vect.z * scale.z;
                MathRotateHelper.rotateAboutZ(rotatedPoint, Math.toRadians(rot.z));
                MathRotateHelper.rotateAboutX(rotatedPoint, Math.toRadians(rot.x));
                MathRotateHelper.rotateAboutY(rotatedPoint, Math.toRadians(rot.y));
                i++;
            }

            this.rot.x = rot.x;
            this.rot.y = rot.y;
            this.rot.z = rot.z;
            this.scale.x = scale.x;
            this.scale.y = scale.y;
            this.scale.z = scale.z;
        }

        return rotatedPoints;

    }

    private List<Vector3f> getRotatedPoints(Vector3f translate, Vector3f rot, Vector3f scale) {
        if (!this.rot.equals(rot) || !scale.equals(this.scale)) {
            int i = 0;
            for (Vector3f vect : points) {
                Vector3f rotatedPoint = rotatedPoints.get(i);
                rotatedPoint.x = translate.x + vect.x * scale.x;
                rotatedPoint.y = translate.y + vect.y * scale.y;
                rotatedPoint.z = translate.z + vect.z * scale.z;
                MathRotateHelper.rotateAboutZ(rotatedPoint, Math.toRadians(rot.z));
                MathRotateHelper.rotateAboutX(rotatedPoint, Math.toRadians(rot.x));
                MathRotateHelper.rotateAboutY(rotatedPoint, Math.toRadians(rot.y));
                i++;
            }

            this.rot.x = rot.x;
            this.rot.y = rot.y;
            this.rot.z = rot.z;
            this.scale.x = scale.x;
            this.scale.y = scale.y;
            this.scale.z = scale.z;
        }

        return rotatedPoints;
    }

    AxisAlignedBB getRotatedModelBox(Vector3f rot, Vector3f scale) {
        if (!this.rot.equals(rot) || !scale.equals(this.scale)) {
            double minX = 0;
            double minY = 0;
            double minZ = 0;
            double maxX = 0;
            double maxY = 0;
            double maxZ = 0;
            float smallSize = 0.01f;
            float add = smallSize / 2f;
            int i = 0;
            for (Vector3f vect : this.getRotatedPoints(rot, scale)) {
                if (i == 0) {
                    minX = vect.x;
                    minY = vect.y;
                    minZ = vect.z;
                    maxX = vect.x;
                    maxY = vect.y;
                    maxZ = vect.z;
                } else {
                    if (vect.x > maxX)
                        maxX = vect.x;
                    if (vect.x < minX)
                        minX = vect.x;
                    if (vect.y > maxY)
                        maxY = vect.y;
                    if (vect.y < minY)
                        minY = vect.y;
                    if (vect.z > maxZ)
                        maxZ = vect.z;
                    if (vect.z < minZ)
                        minZ = vect.z;
                }

                i++;
            }

            this.rot.x = rot.x;
            this.rot.y = rot.y;
            this.rot.z = rot.z;
            this.scale.x = scale.x;
            this.scale.y = scale.y;
            this.scale.z = scale.z;

            if (maxX - minX < smallSize) {
                maxX += add;
                minX -= add;
            }
            if (maxY - minY < smallSize) {
                maxY += add;
                minY -= add;
            }
            if (maxZ - minZ < smallSize) {
                maxZ += add;
                minZ -= add;
            }

            rotatedModelBox.minX = minX;
            rotatedModelBox.minY = minY;
            rotatedModelBox.minZ = minZ;
            rotatedModelBox.maxX = maxX;
            rotatedModelBox.maxY = maxY;
            rotatedModelBox.maxZ = maxZ;
        }

        return rotatedModelBox;
    }

    public AxisAlignedBB getRotatedModelBox(Vector3f translate, Vector3f rot, Vector3f scale) {
        if (!this.rot.equals(rot) || !scale.equals(this.scale) || !this.translate.equals(translate)) {
            float smallSize = 0.01f;
            float add = smallSize / 2f;

            double minX = 0;
            double minY = 0;
            double minZ = 0;
            double maxX = 0;
            double maxY = 0;
            double maxZ = 0;
            int i = 0;
            for (Vector3f vect : this.getRotatedPoints(translate, rot, scale)) {
                if (i == 0) {
                    minX = vect.x;
                    minY = vect.y;
                    minZ = vect.z;
                    maxX = vect.x;
                    maxY = vect.y;
                    maxZ = vect.z;
                } else {
                    if (vect.x > maxX)
                        maxX = vect.x;
                    if (vect.x < minX)
                        minX = vect.x;
                    if (vect.y > maxY)
                        maxY = vect.y;
                    if (vect.y < minY)
                        minY = vect.y;
                    if (vect.z > maxZ)
                        maxZ = vect.z;
                    if (vect.z < minZ)
                        minZ = vect.z;
                }

                i++;
            }

            this.rot.x = rot.x;
            this.rot.y = rot.y;
            this.rot.z = rot.z;
            this.scale.x = scale.x;
            this.scale.y = scale.y;
            this.scale.z = scale.z;
            this.translate.x = translate.x;
            this.translate.y = translate.y;
            this.translate.z = translate.z;

            if (maxX - minX < smallSize) {
                maxX += add;
                minX -= add;
            }
            if (maxY - minY < smallSize) {
                maxY += add;
                minY -= add;
            }
            if (maxZ - minZ < smallSize) {
                maxZ += add;
                minZ -= add;
            }

            rotatedModelBox.minX = minX;
            rotatedModelBox.minY = minY;
            rotatedModelBox.minZ = minZ;
            rotatedModelBox.maxX = maxX;
            rotatedModelBox.maxY = maxY;
            rotatedModelBox.maxZ = maxZ;
        }

        return rotatedModelBox;
    }

}
