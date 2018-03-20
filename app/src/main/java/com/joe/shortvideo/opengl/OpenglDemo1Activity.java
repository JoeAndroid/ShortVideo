package com.joe.shortvideo.opengl;

import android.graphics.BitmapFactory;
import android.opengl.GLSurfaceView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.joe.shortvideo.R;

public class OpenglDemo1Activity extends AppCompatActivity {

    private GLSurfaceView glSurfaceView;

    private Texture texture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opengl_demo1);
        glSurfaceView = findViewById(R.id.glSurfaceView);
        glSurfaceView.setEGLContextClientVersion(2);
        texture = new Texture();
        texture.setmBitmap(BitmapFactory.decodeResource(this.getResources(), R.mipmap.image));
        glSurfaceView.setRenderer(texture);
        glSurfaceView.setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
    }

    @Override
    protected void onResume() {
        super.onResume();
        glSurfaceView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        glSurfaceView.onPause();
    }
}
