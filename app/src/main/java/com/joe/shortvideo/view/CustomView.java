package com.joe.shortvideo.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import java.io.File;

/**
 * 自定义 view 绘制图片
 * Created by qiaobing on 2018/3/6.
 */
public class CustomView extends View {

    Paint paint;
    Bitmap bitmap;

    public CustomView(Context context) {
        super(context,null);
    }

    public CustomView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        paint=new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        bitmap= BitmapFactory.decodeFile(Environment.getExternalStorageDirectory().getPath()+ File.separator+"11.jpg");
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 不建议在onDraw做任何分配内存的操作
        if (bitmap!=null){
            canvas.drawBitmap(bitmap,0,0,paint);
        }
    }
}
