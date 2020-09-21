package com.iot.user.utils.view;

import android.graphics.Matrix;

import com.iot.user.utils.ViewUtil;

public class AutoScaleRunnable implements Runnable {

    //���ŵ�Ŀ��ֵ
    private float mTargetScale;
    //���ŵ����ĵ�
    private float x;
    private float y;

    private final float BIGGER = 1.07f;
    private final float SMALL = 0.93f;

    //��ʱ�ı���
    private float tmpScale;
    private Matrix matrix;



    public AutoScaleRunnable(float mTargetScale, float x, float y, Matrix matrix) {
        this.mTargetScale = mTargetScale;
        this.x = x;
        this.y = y;
        this.matrix = matrix;

        if(ViewUtil.getCurrentScale(matrix) < mTargetScale) {
            tmpScale = BIGGER; //Ŀ������Ŵ�
        }
        else if(ViewUtil.getCurrentScale(matrix) > mTargetScale) {
            tmpScale = SMALL; //Ŀ��������С
        }
    }

    public AutoScaleRunnable(float mMidScale, float x, float y) {

    }


    @Override
    public void run() {
        matrix.postScale(tmpScale , tmpScale , x , y);
        checkBorderAndCenterWhenScale();
//        setImageMatrix(matrix);

        float currentScale = ViewUtil.getCurrentScale(matrix);
        //if�е������ǣ����û�дﵽĿ��ֵ����һֱͨ��postDelayed()ִ��run()������ֱ������elseΪֹ
        if( (tmpScale > 1.0f && currentScale < mTargetScale) || (tmpScale < 1.0f && currentScale > mTargetScale) ) {
//            postDelayed(this , 16); //��this���Ǵ��Լ�
        }
        else {
            //������Ϊ���ǵ�Ŀ��ֵ
            float scale = mTargetScale / currentScale;
            matrix.postScale(scale, scale, x, y);
            checkBorderAndCenterWhenScale();
//            setImageMatrix(matrix);
//            isAutoScale = false;
        }
    }

    /**
     * �����ŵ�ʱ�򣬽��б߽�Ŀ��ƣ��Լ����ǵ�λ�õĿ���
     */
    private void checkBorderAndCenterWhenScale() {

//        RectF rect = getMatrixRectF();
//
//        //��ֵ
//        float deltaX = 0.0f;
//        float deltaY = 0.0f;
//
//        //�ؼ��Ŀ�Ⱥ͸߶�
//        int width = getWidth();
//        int height = getHeight();
//
//        //�аױ߳��־���ƽ�Ʋ��ױ�
//        if(rect.width() >= width) {
//            if(rect.left > 0) { //�������п�϶����������Ҫ�ֲ�
//                deltaX = -rect.left; //��ֵ����ʾӦ�������ƶ�
//            }
//            if(rect.right < width) { //����ұ��п�϶����������Ҫ�ֲ�
//                deltaX = width - rect.right; //��ֵ����ʾӦ�������ƶ�
//            }
//        }
//        if(rect.height() >= height) {
//            if(rect.top > 0) {
//                deltaY = -rect.top;
//            }
//            if(rect.bottom < height) {
//                deltaY = height - rect.bottom;
//            }
//        }
//
//        //�����Ȼ�߶�С�ڿؼ��Ŀ�Ȼ�߶ȣ��;���
//        if(rect.width() < width) {
//            deltaX = width / 2f - rect.right + rect.width() / 2f;
//        }
//        if(rect.height() < height) {
//            deltaY = height / 2f - rect.bottom + rect.height() / 2f;
//        }
//
//        //��֮ǰ�õ���ƽ�����ݸ��µ�mScaleMatrix��
//        mScaleMatrix.postTranslate(deltaX, deltaY);
    }
}
