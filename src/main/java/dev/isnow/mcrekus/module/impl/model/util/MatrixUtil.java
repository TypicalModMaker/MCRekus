package dev.isnow.mcrekus.module.impl.model.util;

import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.tuple.Triple;
import org.joml.Math;
import org.joml.Matrix3f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

@UtilityClass
public class MatrixUtil {
    private final float CONSTANT_G = 3.0F + 2.0F * org.joml.Math.sqrt(2.0F);
    private final GivensParameters QUARTER_PI_GIVENS = GivensParameters.fromPositiveAngle((float) (java.lang.Math.PI / 4));

    private GivensParameters calculateApproxGivens(final float diagonalElement1, final float offDiagonalElement, final float diagonalElement2) {
        final float diff = 2.0F * (diagonalElement1 - diagonalElement2);

        return CONSTANT_G * offDiagonalElement * offDiagonalElement < diff * diff ? GivensParameters.fromUnnormalized(offDiagonalElement, diff) : QUARTER_PI_GIVENS;
    }

    private GivensParameters calculateQrGivens(final float value1, final float value2) {
        final float magnitude = (float) java.lang.Math.hypot(value1, value2);

        float cos = magnitude > 1.0E-6F ? value2 : 0.0F;
        float sin = org.joml.Math.abs(value1) + Math.max(magnitude, 1.0E-6F);

        // Reverse
        if (value1 < 0.0F) {
            final float temp = cos;
            cos = sin;
            sin = temp;
        }

        return GivensParameters.fromUnnormalized(cos, sin);
    }

    private void applySimilarityTransform(final Matrix3f matrix, final Matrix3f transform) {
        matrix.mul(transform);
        transform.transpose();
        transform.mul(matrix);
        matrix.set(transform);
    }

    private void performJacobiStep(final Matrix3f symmetricMatrix, final Matrix3f rotationMatrix, final Quaternionf tempQuaternion, final Quaternionf outputQuaternion) {
        // Z-axis rotation
        if (symmetricMatrix.m01 * symmetricMatrix.m01 + symmetricMatrix.m10 * symmetricMatrix.m10 > 1.0E-6F) {
            final GivensParameters givensParameters = calculateApproxGivens(symmetricMatrix.m00, 0.5F * (symmetricMatrix.m01 + symmetricMatrix.m10), symmetricMatrix.m11);
            final Quaternionf zRotationQuaternion = givensParameters.aroundZ(tempQuaternion);
            outputQuaternion.mul(zRotationQuaternion);
            givensParameters.aroundZ(rotationMatrix);
            applySimilarityTransform(symmetricMatrix, rotationMatrix);
        }

        // Y-axis rotation
        if (symmetricMatrix.m02 * symmetricMatrix.m02 + symmetricMatrix.m20 * symmetricMatrix.m20 > 1.0E-6F) {
            final GivensParameters givensParameters = calculateApproxGivens(symmetricMatrix.m00, 0.5F * (symmetricMatrix.m02 + symmetricMatrix.m20), symmetricMatrix.m22).inverse();
            final Quaternionf yRotationQuaternion = givensParameters.aroundY(tempQuaternion);
            outputQuaternion.mul(yRotationQuaternion);
            givensParameters.aroundY(rotationMatrix);
            applySimilarityTransform(symmetricMatrix, rotationMatrix);
        }

        // X-axis rotation
        if (symmetricMatrix.m12 * symmetricMatrix.m12 + symmetricMatrix.m21 * symmetricMatrix.m21 > 1.0E-6F) {
            final GivensParameters givensParameters = calculateApproxGivens(symmetricMatrix.m11, 0.5F * (symmetricMatrix.m12 + symmetricMatrix.m21), symmetricMatrix.m22);
            final Quaternionf xRotationQuaternion = givensParameters.aroundX(tempQuaternion);
            outputQuaternion.mul(xRotationQuaternion);
            givensParameters.aroundX(rotationMatrix);
            applySimilarityTransform(symmetricMatrix, rotationMatrix);
        }
    }

    public Quaternionf computeEigenvalueJacobi(final Matrix3f symmetricMatrix, final int maxIterations) {
        final Quaternionf outputQuaternion = new Quaternionf();
        final Matrix3f rotationMatrix = new Matrix3f();
        final Quaternionf tempQuaternion = new Quaternionf();

        for (int i = 0; i < maxIterations; i++) {
            performJacobiStep(symmetricMatrix, rotationMatrix, tempQuaternion, outputQuaternion);
        }

        outputQuaternion.normalize();
        return outputQuaternion;
    }

    public float[] reverseFromBDEngine(final float[] matrixElements) {
        float temp;

        temp = matrixElements[1];
        matrixElements[1] = matrixElements[4];
        matrixElements[4] = temp;

        temp = matrixElements[2];
        matrixElements[2] = matrixElements[8];
        matrixElements[8] = temp;

        temp = matrixElements[6];
        matrixElements[6] = matrixElements[9];
        matrixElements[9] = temp;

        temp = matrixElements[3];
        matrixElements[3] = matrixElements[12];
        matrixElements[12] = temp;

        temp = matrixElements[7];
        matrixElements[7] = matrixElements[13];
        matrixElements[13] = temp;

        temp = matrixElements[11];
        matrixElements[11] = matrixElements[14];
        matrixElements[14] = temp;

        return matrixElements;
    }

    public Triple<Quaternionf, Vector3f, Quaternionf> decomposeSVD(final Matrix3f inputMatrix) {
        final Matrix3f transposeMultMatrix = new Matrix3f(inputMatrix);
        transposeMultMatrix.transpose();
        transposeMultMatrix.mul(inputMatrix);

        final Quaternionf eigenvalueQuaternion = computeEigenvalueJacobi(transposeMultMatrix, 5);

        final float eigenvalue1 = transposeMultMatrix.m00;
        final float eigenvalue2 = transposeMultMatrix.m11;

        final boolean isEigenvalue1Small = eigenvalue1 < 1.0E-6;
        final boolean isEigenvalue2Small = eigenvalue2 < 1.0E-6;

        final Matrix3f rotatedMatrix = inputMatrix.rotate(eigenvalueQuaternion);
        final Quaternionf tempQuaternion = new Quaternionf();
        final Quaternionf finalLeftQuaternion = new Quaternionf();

        GivensParameters givensParameters;
        if (isEigenvalue1Small) {
            givensParameters = calculateQrGivens(rotatedMatrix.m11, -rotatedMatrix.m10);
        } else {
            givensParameters = calculateQrGivens(rotatedMatrix.m00, rotatedMatrix.m01);
        }

        final Quaternionf zRotationQuaternion = givensParameters.aroundZ(tempQuaternion);
        final Matrix3f adjustedMatrixZ = givensParameters.aroundZ(transposeMultMatrix);
        finalLeftQuaternion.mul(zRotationQuaternion);
        adjustedMatrixZ.transpose().mul(rotatedMatrix);

        if (isEigenvalue1Small) {
            givensParameters = calculateQrGivens(adjustedMatrixZ.m22, -adjustedMatrixZ.m20);
        } else {
            givensParameters = calculateQrGivens(adjustedMatrixZ.m00, adjustedMatrixZ.m02);
        }

        givensParameters = givensParameters.inverse();

        final Quaternionf yRotationQuaternion = givensParameters.aroundY(tempQuaternion);
        final Matrix3f adjustedMatrixY = givensParameters.aroundY(rotatedMatrix);

        finalLeftQuaternion.mul(yRotationQuaternion);

        adjustedMatrixY.transpose().mul(adjustedMatrixZ);

        if (isEigenvalue2Small) {
            givensParameters = calculateQrGivens(adjustedMatrixY.m22, -adjustedMatrixY.m21);
        } else {
            givensParameters = calculateQrGivens(adjustedMatrixY.m11, adjustedMatrixY.m12);
        }

        final Quaternionf xRotationQuaternion = givensParameters.aroundX(tempQuaternion);
        final Matrix3f adjustedMatrixX = givensParameters.aroundX(adjustedMatrixZ);

        finalLeftQuaternion.mul(xRotationQuaternion);
        adjustedMatrixX.transpose().mul(adjustedMatrixY);

        final Vector3f singularValues = new Vector3f(adjustedMatrixX.m00, adjustedMatrixX.m11, adjustedMatrixX.m22);

        return Triple.of(finalLeftQuaternion, singularValues, eigenvalueQuaternion.conjugate());
    }
}
