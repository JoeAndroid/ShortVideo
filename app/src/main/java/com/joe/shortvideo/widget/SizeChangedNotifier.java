package com.joe.shortvideo.widget;

import android.view.View;

/**
 * Created by qiaobing on 17/9/27.
 */

public interface SizeChangedNotifier {

    void setOnSizeChangedListener(Listener listener);

    interface Listener {
        void onSizeChanged(View view, int w, int h, int oldw, int oldh);
    }

}