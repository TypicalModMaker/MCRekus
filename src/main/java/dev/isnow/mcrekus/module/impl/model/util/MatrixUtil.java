package dev.isnow.mcrekus.module.impl.model.util;

import java.nio.FloatBuffer;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.tuple.Triple;
import org.joml.Math;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

@UtilityClass
public class MatrixUtil {
    private final float G = 3.0F + 2.0F * org.joml.Math.sqrt(2.0F);
    private final GivensParameters PI_4 = GivensParameters.fromPositiveAngle((float) (java.lang.Math.PI / 4));
    
    public Matrix4f mulComponentWise(Matrix4f matrix, float scalar) {
        return matrix.set(
                matrix.m00() * scalar,
                matrix.m01() * scalar,
                matrix.m02() * scalar,
                matrix.m03() * scalar,
                matrix.m10() * scalar,
                matrix.m11() * scalar,
                matrix.m12() * scalar,
                matrix.m13() * scalar,
                matrix.m20() * scalar,
                matrix.m21() * scalar,
                matrix.m22() * scalar,
                matrix.m23() * scalar,
                matrix.m30() * scalar,
                matrix.m31() * scalar,
                matrix.m32() * scalar,
                matrix.m33() * scalar
        );
    }

    private GivensParameters approxGivensQuat(float a11, float a12, float a22) {
        float f = 2.0F * (a11 - a22);
        return G * a12 * a12 < f * f ? GivensParameters.fromUnnormalized(a12, f) : PI_4;
    }

    private GivensParameters qrGivensQuat(float a1, float a2) {
        float f = (float)java.lang.Math.hypot((double)a1, (double)a2);
        float g = f > 1.0E-6F ? a2 : 0.0F;
        float h = org.joml.Math.abs(a1) + Math.max(f, 1.0E-6F);
        if (a1 < 0.0F) {
            float i = g;
            g = h;
            h = i;
        }

        return GivensParameters.fromUnnormalized(g, h);
    }

    private void similarityTransform(Matrix3f X, Matrix3f A) {
        X.mul(A);
        A.transpose();
        A.mul(X);
        X.set(A);
    }

    private void stepJacobi(Matrix3f AtA, Matrix3f matrix3f, Quaternionf quaternionf, Quaternionf quaternionf2) {
        if (AtA.m01 * AtA.m01 + AtA.m10 * AtA.m10 > 1.0E-6F) {
            GivensParameters givensParameters = approxGivensQuat(AtA.m00, 0.5F * (AtA.m01 + AtA.m10), AtA.m11);
            Quaternionf quaternionf3 = givensParameters.aroundZ(quaternionf);
            quaternionf2.mul(quaternionf3);
            givensParameters.aroundZ(matrix3f);
            similarityTransform(AtA, matrix3f);
        }

        if (AtA.m02 * AtA.m02 + AtA.m20 * AtA.m20 > 1.0E-6F) {
            GivensParameters givensParameters2 = approxGivensQuat(AtA.m00, 0.5F * (AtA.m02 + AtA.m20), AtA.m22).inverse();
            Quaternionf quaternionf4 = givensParameters2.aroundY(quaternionf);
            quaternionf2.mul(quaternionf4);
            givensParameters2.aroundY(matrix3f);
            similarityTransform(AtA, matrix3f);
        }

        if (AtA.m12 * AtA.m12 + AtA.m21 * AtA.m21 > 1.0E-6F) {
            GivensParameters givensParameters3 = approxGivensQuat(AtA.m11, 0.5F * (AtA.m12 + AtA.m21), AtA.m22);
            Quaternionf quaternionf5 = givensParameters3.aroundX(quaternionf);
            quaternionf2.mul(quaternionf5);
            givensParameters3.aroundX(matrix3f);
            similarityTransform(AtA, matrix3f);
        }
    }

    public Quaternionf eigenvalueJacobi(Matrix3f AtA, int numJacobiIterations) {
        Quaternionf quaternionf = new Quaternionf();
        Matrix3f matrix3f = new Matrix3f();
        Quaternionf quaternionf2 = new Quaternionf();

        for (int i = 0; i < numJacobiIterations; i++) {
            stepJacobi(AtA, matrix3f, quaternionf2, quaternionf);
        }

        quaternionf.normalize();
        return quaternionf;
    }

    public float[] reverseFromBDEngine(float[] input) {
        float t = input[1];
        input[1] = input[4];
        input[4] = t;

        t = input[2];
        input[2] = input[8];
        input[8] = t;

        t = input[6];
        input[6] = input[9];
        input[9] = t;

        t = input[3];
        input[3] = input[12];
        input[12] = t;

        t = input[7];
        input[7] = input[13];
        input[13] = t;

        t = input[11];
        input[11] = input[14];
        input[14] = t;

        return input;
    }

    public Triple<Quaternionf, Vector3f, Quaternionf> svdDecompose(Matrix3f A) {
        Matrix3f matrix3f = new Matrix3f(A);
        matrix3f.transpose();
        matrix3f.mul(A);
        Quaternionf quaternionf = eigenvalueJacobi(matrix3f, 5);
        float f = matrix3f.m00;
        float g = matrix3f.m11;
        boolean bl = (double)f < 1.0E-6;
        boolean bl2 = (double)g < 1.0E-6;
        Matrix3f matrix3f3 = A.rotate(quaternionf);
        Quaternionf quaternionf2 = new Quaternionf();
        Quaternionf quaternionf3 = new Quaternionf();
        GivensParameters givensParameters;
        if (bl) {
            givensParameters = qrGivensQuat(matrix3f3.m11, -matrix3f3.m10);
        } else {
            givensParameters = qrGivensQuat(matrix3f3.m00, matrix3f3.m01);
        }

        Quaternionf quaternionf4 = givensParameters.aroundZ(quaternionf3);
        Matrix3f matrix3f4 = givensParameters.aroundZ(matrix3f);
        quaternionf2.mul(quaternionf4);
        matrix3f4.transpose().mul(matrix3f3);
        if (bl) {
            givensParameters = qrGivensQuat(matrix3f4.m22, -matrix3f4.m20);
        } else {
            givensParameters = qrGivensQuat(matrix3f4.m00, matrix3f4.m02);
        }

        givensParameters = givensParameters.inverse();
        Quaternionf quaternionf5 = givensParameters.aroundY(quaternionf3);
        Matrix3f matrix3f5 = givensParameters.aroundY(matrix3f3);
        quaternionf2.mul(quaternionf5);
        matrix3f5.transpose().mul(matrix3f4);
        if (bl2) {
            givensParameters = qrGivensQuat(matrix3f5.m22, -matrix3f5.m21);
        } else {
            givensParameters = qrGivensQuat(matrix3f5.m11, matrix3f5.m12);
        }

        Quaternionf quaternionf6 = givensParameters.aroundX(quaternionf3);
        Matrix3f matrix3f6 = givensParameters.aroundX(matrix3f4);
        quaternionf2.mul(quaternionf6);
        matrix3f6.transpose().mul(matrix3f5);
        Vector3f vector3f = new Vector3f(matrix3f6.m00, matrix3f6.m11, matrix3f6.m22);
        return Triple.of(quaternionf2, vector3f, quaternionf.conjugate());
    }

    public void main(final String[] args) {
        Matrix4f matrix = new Matrix4f(0.334f,0.0937f,0f,0.189f,-0.2338f,0.1338f,0f,0.8952f,0f,0f,0.2588f,0.4042f,0f,0f,0f,1f);

        float scaleFactor = 1.0F / matrix.m33();
        Triple<Quaternionf, org.joml.Vector3f, Quaternionf> triple = MatrixUtil.svdDecompose(new Matrix3f(matrix).scale(scaleFactor));

        System.out.println(Float.parseFloat(String.valueOf(triple.getLeft().x)) + " " + Float.parseFloat(String.valueOf(triple.getLeft().y)) + " " + Float.parseFloat(String.valueOf(triple.getLeft().z)) + " " + Float.parseFloat(String.valueOf(triple.getLeft().w)));
        System.out.println(Float.parseFloat(String.valueOf(triple.getMiddle().x)) + " " + Float.parseFloat(String.valueOf(triple.getMiddle().y)) + " " + Float.parseFloat(String.valueOf(triple.getMiddle().z)));
        System.out.println(Float.parseFloat(String.valueOf(triple.getRight().x)) + " " + Float.parseFloat(String.valueOf(triple.getRight().y)) + " " + Float.parseFloat(String.valueOf(triple.getRight().z)) + " " + Float.parseFloat(String.valueOf(triple.getRight().w)));

    }
}

