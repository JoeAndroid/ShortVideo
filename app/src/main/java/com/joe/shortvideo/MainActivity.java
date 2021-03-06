package com.joe.shortvideo;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.joe.shortvideo.opengl.OpenglDemo1Activity;
import com.joe.shortvideo.ui.CameraRecorderActivity;

import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class MainActivity extends AppCompatActivity {

    private final int REQ_PERMISSION_AUDIO = 0x01;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void clickImage(View view) {
        startActivity(new Intent(this, ImageActivity.class));
    }

    public void clickAudio(View view) {
        if (checkPermission()) {
            startActivity(new Intent(this, AudioRecordActivity.class));
        } else {
            requestPermission();
        }
    }

    public void player(View view) {
//        startActivity(new Intent(this, PlayVideoActivity.class));
        startActivity(new Intent(this, FFmpegPlayerActivity.class));
//        startActivity(new Intent(this, FFmpegToYUVActivity.class));
    }

    public void VideoRecord(View view) {
//        startActivity(new Intent(this,VideoRecordActivity.class));
        startActivity(new Intent(this, CameraRecorderActivity.class));
    }

    public void opengl(View view) {
        startActivity(new Intent(this, OpenglDemo1Activity.class));
    }

    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(getApplicationContext(),
                WRITE_EXTERNAL_STORAGE);
        int result1 = ContextCompat.checkSelfPermission(getApplicationContext(),
                RECORD_AUDIO);
        return result == PackageManager.PERMISSION_GRANTED &&
                result1 == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new
                String[]{WRITE_EXTERNAL_STORAGE, RECORD_AUDIO}, REQ_PERMISSION_AUDIO);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQ_PERMISSION_AUDIO:
                if (grantResults.length > 0) {
                    boolean StoragePermission = grantResults[0] ==
                            PackageManager.PERMISSION_GRANTED;
                    boolean RecordPermission = grantResults[1] ==
                            PackageManager.PERMISSION_GRANTED;

                    if (StoragePermission && RecordPermission) {
                        showToast("Permission Granted");
                    } else {
                        showToast("Permission  Denied");
                    }
                }
                break;
        }
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

}
