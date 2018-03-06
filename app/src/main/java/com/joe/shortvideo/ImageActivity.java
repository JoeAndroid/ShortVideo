package com.joe.shortvideo;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.ImageView;

import com.joe.shortvideo.view.CustomView;

import java.io.File;

/**
 * 通过三种方式绘制图片
 */
public class ImageActivity extends AppCompatActivity {

    private ImageView imageView;
    private SurfaceView surfaceView;
    private CustomView customView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);

        imageView = (ImageView) findViewById(R.id.imageview);
        //获取 bitmap图像
        Bitmap bitmap = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory().getPath() + File.separator + "11.jpg");
        imageView.setImageBitmap(bitmap);

        surfaceView = (SurfaceView) findViewById(R.id.imageSurface);
        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                if (holder == null) {
                    return;
                }

                //创建画笔
                Paint paint = new Paint();
                paint.setAntiAlias(true);
                paint.setStyle(Paint.Style.STROKE);
                //获取 bitmap图像
                Bitmap bitmap = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory().getPath() + File.separator + "11.jpg");
                //锁定当前 surfaceview的画布
                Canvas canvas = holder.lockCanvas();
                //绘制图像
                canvas.drawBitmap(bitmap, 0, 0, paint);
                //接触锁定并显示在画面上
                holder.unlockCanvasAndPost(canvas);
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {

            }
        });
        customView = (CustomView) findViewById(R.id.imageCustom);
    }
}
