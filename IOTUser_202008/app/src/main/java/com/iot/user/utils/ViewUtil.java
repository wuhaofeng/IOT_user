package com.iot.user.utils;

import android.graphics.Matrix;

public class ViewUtil {

    /**
     * �õ���ǰ�����ű���
     */
    public static float getCurrentScale(Matrix matrix) {
        float[] values = new float[9];
        matrix.getValues(values);
        return values[Matrix.MSCALE_X];
    }
}
