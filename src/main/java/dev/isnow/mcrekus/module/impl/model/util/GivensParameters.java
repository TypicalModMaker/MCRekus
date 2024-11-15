package dev.isnow.mcrekus.module.impl.model.util;

import org.joml.Math;
import org.joml.Matrix3f;
import org.joml.Quaternionf;

public record GivensParameters(float sinHalf, float cosHalf) {
    public static GivensParameters fromUnnormalized(final float a, final float b) {
        final float normalization = org.joml.Math.invsqrt(a * a + b * b);

        return new GivensParameters(normalization * a, normalization * b);
    }

    public static GivensParameters fromPositiveAngle(final float radians) {
        final float sinHalf = org.joml.Math.sin(radians / 2.0F);
        final float cosHalf = Math.cosFromSin(sinHalf, radians / 2.0F);

        return new GivensParameters(sinHalf, cosHalf);
    }

    public GivensParameters inverse() {
        return new GivensParameters(-sinHalf, cosHalf);
    }

    public Quaternionf aroundX(final Quaternionf quaternion) {
        return quaternion.set(sinHalf, 0.0F, 0.0F, cosHalf);
    }

    public Quaternionf aroundY(final Quaternionf quaternion) {
        return quaternion.set(0.0F, sinHalf, 0.0F, cosHalf);
    }

    public Quaternionf aroundZ(final Quaternionf quaternion) {
        return quaternion.set(0.0F, 0.0F, sinHalf, cosHalf);
    }

    public float cos() {
        return cosHalf * cosHalf - sinHalf * sinHalf;
    }

    public float sin() {
        return 2.0F * sinHalf * cosHalf;
    }

    public Matrix3f aroundX(final Matrix3f matrix) {
        matrix.m01 = 0.0F;
        matrix.m02 = 0.0F;
        matrix.m10 = 0.0F;
        matrix.m20 = 0.0F;

        final float cos = cos();
        final float sin = sin();
        matrix.m11 = cos;
        matrix.m22 = cos;
        matrix.m12 = sin;
        matrix.m21 = -sin;

        matrix.m00 = 1.0F;
        return matrix;
    }

    public Matrix3f aroundY(final Matrix3f matrix) {
        matrix.m01 = 0.0F;
        matrix.m10 = 0.0F;
        matrix.m12 = 0.0F;
        matrix.m21 = 0.0F;

        final float cos = cos();
        final float sin = sin();
        matrix.m00 = cos;
        matrix.m22 = cos;
        matrix.m02 = -sin;
        matrix.m20 = sin;

        matrix.m11 = 1.0F;
        return matrix;
    }

    public Matrix3f aroundZ(final Matrix3f matrix) {
        matrix.m02 = 0.0F;
        matrix.m12 = 0.0F;
        matrix.m20 = 0.0F;
        matrix.m21 = 0.0F;

        final float cos = cos();
        final float sin = sin();
        matrix.m00 = cos;
        matrix.m11 = cos;
        matrix.m01 = sin;
        matrix.m10 = -sin;
        
        matrix.m22 = 1.0F;
        return matrix;
    }
}
