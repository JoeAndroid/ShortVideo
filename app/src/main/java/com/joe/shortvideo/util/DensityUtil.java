package com.joe.shortvideo.util;

import android.content.Context;

/**
 * Created by qiaobing on 17/9/27.
 */

public class DensityUtil {
    public DensityUtil() {
    }

    public static int dip2px(Context paramContext, float paramFloat) {
        return (int)(0.5F + paramFloat * paramContext.getResources().getDisplayMetrics().density);
    }

    public static int px2dip(Context paramContext, float paramFloat) {
        return (int)(0.5F + paramFloat / paramContext.getResources().getDisplayMetrics().density);
    }

    public static int sp2px(Context context, float spValue) {
        float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int)(spValue * fontScale + 0.5F);
    }

    public static int dip2px(float value) {
        return (int)(0.5F + value * MySystemParams.getInstance().scale);
    }

    public static int px2dip(float value) {
        return (int)(0.5F + value / MySystemParams.getInstance().scale);
    }

    public static int sp2px(float value) {
        return (int)(0.5F + value * MySystemParams.getInstance().fontScale);
    }

    public static int getActualScreenWidth() {
        boolean width = false;
        MySystemParams systemparams = MySystemParams.getInstance();
        int width1;
        if(systemparams.screenOrientation == 2) {
            width1 = systemparams.screenHeight;
        } else {
            width1 = systemparams.screenWidth;
        }

        return width1;
    }

    public static int getActualScreenHeight() {
        boolean height = false;
        MySystemParams systemparams = MySystemParams.getInstance();
        int height1;
        if(systemparams.screenOrientation == 2) {
            height1 = systemparams.screenWidth;
        } else {
            height1 = systemparams.screenHeight;
        }

        return height1;
    }
}
