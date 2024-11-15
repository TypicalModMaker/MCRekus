package dev.isnow.mcrekus.module.impl.model.parser.impl.object;

import com.github.retrooper.packetevents.util.Quaternion4f;
import com.github.retrooper.packetevents.util.Vector3f;
import dev.isnow.mcrekus.module.impl.model.util.MatrixUtil;
import dev.isnow.mcrekus.util.RekusLogger;
import java.util.Arrays;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.tuple.Triple;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Quaternionf;

@Getter
@Setter
public class Transformable {
    final Quaternion4f leftRotation, rightRotation;
    final Vector3f scale, translation;

    public Transformable(final float[] matrixArray) {
        this.translation = new Vector3f(matrixArray[3], matrixArray[7], matrixArray[11]);

        // TODO: FIX GROUPS
        final Matrix4f matrix = new Matrix4f();
        final float[] reversed = MatrixUtil.reverseFromBDEngine(matrixArray);
        matrix.set(reversed);

        final float scaleFactor = 1.0F / matrix.m33();
        final Triple<Quaternionf, org.joml.Vector3f, Quaternionf> triple = MatrixUtil.decomposeSVD(new Matrix3f(matrix).scale(scaleFactor));

        final Quaternionf jomlLeftRotation = new Quaternionf(triple.getLeft());
        this.leftRotation = new Quaternion4f(jomlLeftRotation.x, jomlLeftRotation.y, jomlLeftRotation.z, jomlLeftRotation.w);

        final org.joml.Vector3f jomlScale = new org.joml.Vector3f(triple.getMiddle());
        this.scale = new Vector3f(jomlScale.x, jomlScale.y, jomlScale.z);

        final Quaternionf jomlRightRotation = new Quaternionf(triple.getRight());
        this.rightRotation = new Quaternion4f(jomlRightRotation.x, jomlRightRotation.y, jomlRightRotation.z, jomlRightRotation.w);
    }
}
