package com.joe.shortvideo;

import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.joe.shortvideo.util.NativeFunUtils;

public class FFmpegToYUVActivity extends AppCompatActivity {

    private TextView tvInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ffmpeg_to_yuv);
        tvInfo = findViewById(R.id.tvInfo);
        tvInfo.setMovementMethod(ScrollingMovementMethod.getInstance());
    }

    public void mp4toyuv(View view) {
        String folderurl = Environment.getExternalStorageDirectory().getPath();

        String inputurl = Environment.getExternalStorageDirectory()
                .getAbsolutePath() + "/video.mp4";

        String outputurl = Environment.getExternalStorageDirectory()
                .getAbsolutePath() + "/video.yuv";

        Log.e("ws-----------inputurl", inputurl);
        Log.e("ws------------outputurl", outputurl);
        NativeFunUtils.decode(inputurl, outputurl);
    }

    public void infoShow(View view) {
        tvInfo.setText(NativeFunUtils.avcodecinfo());
    }
}
