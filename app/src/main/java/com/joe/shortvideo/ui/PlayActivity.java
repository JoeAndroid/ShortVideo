package com.joe.shortvideo.ui;

import android.app.Activity;
import android.os.Bundle;
import android.widget.MediaController;
import android.widget.VideoView;

import com.joe.shortvideo.R;

public class PlayActivity extends Activity {

    VideoView videoView;

    private String path;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);
        if (null != this.getIntent().getExtras()) {
            path = this.getIntent().getExtras().getString("outPath", null);
        }
        videoView = (VideoView) findViewById(R.id.videoview);
        videoView.setVideoPath(path);
        videoView.setMediaController(new MediaController(this));
        videoView.start();
    }
}
