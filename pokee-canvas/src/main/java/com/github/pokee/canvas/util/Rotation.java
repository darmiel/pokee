package com.github.pokee.canvas.util;

public class Rotation {

    /**
     * Rotate a point around the X, Y, and Z axis.
     *
     * @param point  the point to rotate
     * @param angleX the angle to rotate around the X axis
     * @param angleY the angle to rotate around the Y axis
     * @param angleZ the angle to rotate around the Z axis
     * @return the rotated point
     */
    public static double[] rotatePoint(double[] point, double angleX, double angleY, double angleZ) {
        // Rotate around X-axis
        double[] rx = {
                1, 0, 0,
                0, Math.cos(angleX), -Math.sin(angleX),
                0, Math.sin(angleX), Math.cos(angleX)
        };
        point = multiplyMatrix(point, rx);

        // Rotate around Y-axis
        double[] ry = {
                Math.cos(angleY), 0, Math.sin(angleY),
                0, 1, 0,
                -Math.sin(angleY), 0, Math.cos(angleY)
        };
        point = multiplyMatrix(point, ry);

        // Rotate around Z-axis
        double[] rz = {
                Math.cos(angleZ), -Math.sin(angleZ), 0,
                Math.sin(angleZ), Math.cos(angleZ), 0,
                0, 0, 1
        };
        point = multiplyMatrix(point, rz);

        return point;
    }

    /**
     * Multiply a point by a 3x3 matrix.
     *
     * @param point  the point to multiply
     * @param matrix the matrix to multiply by
     * @return the result of the multiplication
     */
    private static double[] multiplyMatrix(double[] point, double[] matrix) {
        double[] result = new double[3];
        result[0] = point[0] * matrix[0] + point[1] * matrix[1] + point[2] * matrix[2];
        result[1] = point[0] * matrix[3] + point[1] * matrix[4] + point[2] * matrix[5];
        result[2] = point[0] * matrix[6] + point[1] * matrix[7] + point[2] * matrix[8];
        return result;
    }

}
