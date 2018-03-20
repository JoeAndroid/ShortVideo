package com.joe.shortvideo.ui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.TextView;

import com.joe.shortvideo.R;
import com.joe.shortvideo.aavt.av.CameraRecorder2;
import com.joe.shortvideo.aavt.gl.BeautyFilter;
import com.joe.shortvideo.aavt.gl.GroupFilter;
import com.joe.shortvideo.aavt.gl.StickFigureFilter;
import com.joe.shortvideo.aavt.gl.WaterMarkFilter;

public class CameraRecorderActivity extends Activity {

    private SurfaceView mSurfaceView;
    private TextView mTvPreview, mTvRecord;
    private boolean isPreviewOpen = false;
    private boolean isRecordOpen = false;

    private CameraRecorder2 mCamera;

    private GroupFilter mFilter;

    private String tempPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/test.mp4";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_record);
        mSurfaceView = findViewById(R.id.mSurfaceView);
        mTvRecord = findViewById(R.id.mTvRec);
        mTvPreview = findViewById(R.id.mTvShow);
        mFilter = new GroupFilter(getResources());
        mCamera = new CameraRecorder2();
        mCamera.setOutputPath(tempPath);
        mSurfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                mCamera.setRenderer(mFilter);
//                filter.addFilter(new StickFigureFilter(getResources()));
                mFilter.addFilter(new BeautyFilter(getResources()).setBeautyLevel(4));
                mFilter.addFilter(new WaterMarkFilter().setMarkPosition(30, 10, 100, 76).setMark(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher)));
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
                mCamera.open();
                mCamera.setSurface(holder.getSurface());
                mCamera.setPreviewSize(width, height);
                mCamera.startPreview();
                isPreviewOpen = true;
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                mCamera.stopPreview();
                mCamera.close();
            }
        });
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.mTvShow:
                isPreviewOpen = !isPreviewOpen;
                mTvPreview.setText(isPreviewOpen ? "关预览" : "开预览");
                if (isPreviewOpen) {
                    mCamera.startPreview();
                } else {
                    mCamera.stopPreview();
                }
                break;
            case R.id.mTvRec:
                isRecordOpen = !isRecordOpen;
                mTvRecord.setText(isRecordOpen ? "关录制" : "开录制");
                if (isRecordOpen) {
                    mCamera.startRecord();
                } else {
                    mCamera.stopRecord();
                    new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent v = new Intent(CameraRecorderActivity.this, EditorActivity.class);
                            v.putExtra("path", tempPath);
                            startActivity(v);
                        }
                    }, 1000);
                }
                break;
            case R.id.tvFilter:
                mFilter.addFilter(new StickFigureFilter(getResources()));
                break;
            default:
                break;
        }
    }

    /**
     * 设置滤镜选择控件
     */
   /* private void initWheel() {
        final List<FilterChoose> filterChooses = new ArrayList<>();
        filterChooses.add(new FilterChoose(1, "默认"));
        filterChooses.add(new FilterChoose(2, "寒冷"));
        filterChooses.add(new FilterChoose(3, "温暖"));
        filterChooses.add(new FilterChoose(4, "灰度"));
        filterChooses.add(new FilterChoose(5, "浮雕"));
        WheelView wheelView = (WheelView) findViewById(R.id.change_fliter);
        wheelView.setAdapter(new WheelViewFilterAdapter(filterChooses));
        wheelView.setOnItemSelectedListener(new WheelView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                changeFilter(filterChooses.get(index).getIndex());
                filterIndex = filterChooses.get(index).getIndex();

            }
        });
    }*/

}
