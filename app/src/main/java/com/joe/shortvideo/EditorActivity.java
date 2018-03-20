/*
 * Created by Wuwang on 2017/9/11
 * Copyright © 2017年 深圳哎吖科技. All rights reserved.
 */
package com.joe.shortvideo;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.opengl.GLES20;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.seu.magicfilter.filter.advanced.GPUImageExtTexFilter;
import com.seu.magicfilter.filter.base.GPUImageFilterGroup;
import com.seu.magicfilter.filter.base.gpuimage.GPUImageFilter;
import com.seu.magicfilter.filter.helper.MagicFilterFactory;
import com.seu.magicfilter.filter.helper.MagicFilterType;
import com.seu.magicfilter.utils.MagicParams;
import com.joe.shortvideo.aavt.av.Mp4Processor;
import com.joe.shortvideo.aavt.core.Renderer;
import com.joe.shortvideo.aavt.utils.MatrixUtils;
import com.joe.shortvideo.adapter.FilterAdapter;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;


public class EditorActivity extends Activity {
    private RelativeLayout title_bar;
    private RecyclerView mFilterListView;
    private FilterAdapter mAdapter;

    private ObjectAnimator animator;
    private VideoSurfaceView videoSurfaceView = null;

    private RelativeLayout rlGlViewContainer;

    private MediaPlayer mediaPlayer = null;
    private Mp4Processor mProcessor;

    private GPUImageFilter mFilter2;
    private GPUImageFilter mFilter;

    private int screenHeight;

    private int screenWidth;

    private int preheight;
    private int prewidth;

    private String path;

    private String outPath;

    private boolean isStop;

    private ProgressDialog progressDialog;
    private final MagicFilterType[] types = new MagicFilterType[]{
            MagicFilterType.NONE,
//            MagicFilterType.BEAUTY,
            MagicFilterType.FAIRYTALE,
            MagicFilterType.SUNRISE,
            MagicFilterType.SUNSET,
            MagicFilterType.WHITECAT,
            MagicFilterType.BLACKCAT,
            MagicFilterType.SKINWHITEN,
            MagicFilterType.HEALTHY,
            MagicFilterType.SWEETS,
            MagicFilterType.ROMANCE,
            MagicFilterType.SAKURA,
            MagicFilterType.WARM,
            MagicFilterType.ANTIQUE,
            MagicFilterType.NOSTALGIA,
            MagicFilterType.CALM,
            MagicFilterType.LATTE,
            MagicFilterType.TENDER,
            MagicFilterType.COOL,
            MagicFilterType.EMERALD,
            MagicFilterType.EVERGREEN,
            MagicFilterType.CRAYON,
            MagicFilterType.SKETCH
//            MagicFilterType.AMARO,
//            MagicFilterType.BRANNAN,
//            MagicFilterType.BROOKLYN,
//            MagicFilterType.EARLYBIRD,
//            MagicFilterType.FREUD,
//            MagicFilterType.HEFE,
//            MagicFilterType.HUDSON,
//            MagicFilterType.INKWELL,
//            MagicFilterType.KEVIN,
//            MagicFilterType.LOMO,
//            MagicFilterType.N1977,
//            MagicFilterType.NASHVILLE,
//            MagicFilterType.PIXAR,
//            MagicFilterType.RISE,
//            MagicFilterType.SIERRA,
//            MagicFilterType.SUTRO,
//            MagicFilterType.TOASTER2,
//            MagicFilterType.VALENCIA,
//            MagicFilterType.WALDEN,
//            MagicFilterType.XPROII
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mp4);
        title_bar = (RelativeLayout) findViewById(R.id.title_bar);
        mFilterListView = (RecyclerView) findViewById(R.id.filter_listView);
        if (null != this.getIntent().getExtras()) {
            path = this.getIntent().getExtras().getString("path", null);
        }
        MagicParams.context = this;
        outPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/temp.mp4";
        screenHeight = this.getResources().getDisplayMetrics().heightPixels;
        screenWidth = this.getResources().getDisplayMetrics().widthPixels;
        initGLView();
        mFilter2 = new GPUImageFilter();
        mProcessor = new Mp4Processor();
        mProcessor.setOutputPath(outPath);
        mProcessor.setInputPath(path);
        mProcessor.setOnCompleteListener(new Mp4Processor.OnProgressListener() {
            @Override
            public void onProgress(long max, long current) {
                Log.i("onProgress=", current + "");
                progressDialog.setProgress((int) current);
            }

            @Override
            public void onComplete(String path) {
                if (!isStop) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), "处理完毕", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent();
                            intent.putExtra("outPath", outPath);
                            intent.setClass(EditorActivity.this, PlayActivity.class);
                            startActivity(intent);
                            progressDialog.dismiss();
                        }
                    });
                } else {
                    isStop = false;
                }
            }
        });
        mProcessor.setRenderer(new Renderer() {

            protected FloatBuffer mVertexBuffer;
            protected FloatBuffer mTextureBuffer;

            @Override
            public void create() {
                ByteBuffer vertex = ByteBuffer.allocateDirect(32);
                vertex.order(ByteOrder.nativeOrder());
                mVertexBuffer = vertex.asFloatBuffer();
                mVertexBuffer.put(MatrixUtils.getOriginalVertexCo());
                mVertexBuffer.position(0);
                ByteBuffer texture = ByteBuffer.allocateDirect(32);
                texture.order(ByteOrder.nativeOrder());
                mTextureBuffer = texture.asFloatBuffer();
                mTextureBuffer.put(MatrixUtils.getOriginalTextureCo());
                mTextureBuffer.position(0);
                mFilter2.init();
                GLES20.glUseProgram(mFilter2.getProgram());
            }

            @Override
            public void sizeChanged(int width, int height) {
                mFilter2.onInputSizeChanged(width, height);
            }

            @Override
            public void draw(int texture) {
                mFilter2.onDrawFrame(texture, mVertexBuffer, mTextureBuffer);
            }

            @Override
            public void destroy() {
                mFilter2.destroy();
            }
        });


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mFilterListView.setLayoutManager(linearLayoutManager);

        mAdapter = new FilterAdapter(this, types);
        mFilterListView.setAdapter(mAdapter);
        mAdapter.setOnFilterChangeListener(onFilterChangeListener);

    }

    public void initGLView() {
        rlGlViewContainer = (RelativeLayout) findViewById(R.id.rlGlViewContainer);
        mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(path);
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mediaPlayer.setLooping(true);
        int vWidth = mediaPlayer.getVideoWidth();
        int vHeight = mediaPlayer.getVideoHeight();
        if (vWidth > screenWidth || vHeight > screenHeight) {
            // 如果video的宽或者高超出了当前屏幕的大小，则要进行缩放
            float wRatio = (float) vWidth / (float) screenWidth;
            float hRatio = (float) vHeight / (float) screenHeight;
            float ratio = Math.max(wRatio, hRatio);
            vWidth = (int) Math.ceil(vWidth / ratio);
            vHeight = (int) Math.ceil(vHeight / ratio);
        }
        if (mediaPlayer.getVideoWidth() == mediaPlayer.getVideoHeight()) {
            preheight = screenWidth;
        } else if (mediaPlayer.getVideoWidth() > mediaPlayer.getVideoHeight()) {
            preheight = mediaPlayer.getVideoHeight() * screenWidth / mediaPlayer.getVideoWidth();
        } else if (mediaPlayer.getVideoWidth() < mediaPlayer.getVideoHeight()) {
            if (mediaPlayer.getVideoHeight() >= screenHeight) {
                preheight = screenHeight;
            } else {
                preheight = mediaPlayer.getVideoHeight() * screenWidth / mediaPlayer.getVideoWidth();
            }
        }
        videoSurfaceView = new VideoSurfaceView(this, mediaPlayer);
        videoSurfaceView.setSourceSize(vWidth, vHeight);
        RelativeLayout.LayoutParams rllp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, preheight);
        rlGlViewContainer.addView(videoSurfaceView, rllp);
        mediaPlayer.start();
        if (preheight == screenHeight) {
            title_bar.setBackgroundColor(Color.parseColor("#7f232A42"));
            mFilterListView.setBackgroundColor(Color.parseColor("#7f232A42"));
        }
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_right:
                try {
                    mProcessor.start();
                    progressDialog = new ProgressDialog(this);
                    progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                    progressDialog.setCancelable(true);
                    progressDialog.setMessage("合成中");
                    progressDialog.setMax(100);
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            try {
                                isStop = true;
                                mProcessor.stop();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                    progressDialog.show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.iv_left:
                finish();
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        isStop = false;
        if (mediaPlayer != null) {
            mediaPlayer.start();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mediaPlayer != null) {
            mediaPlayer.pause();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    private void switchFilterTo(final GPUImageFilter filter) {
        if (mFilter == null
                || (filter != null && !mFilter.getClass().equals(filter.getClass()))) {
            mFilter = filter;
            try {
                Log.i("FilterName", filter.getClass().getName());
                Class classType = Class.forName("" + filter.getClass().getName());
                mFilter2 = (GPUImageFilter) classType.newInstance();
            } catch (Exception e) {
                e.printStackTrace();
            }
            GPUImageFilterGroup filterGroup = new GPUImageFilterGroup();
            filterGroup.addFilter(new GPUImageExtTexFilter());
            filterGroup.addFilter(mFilter);
            videoSurfaceView.setFilter(filterGroup);
        }
    }


    private FilterAdapter.onFilterChangeListener onFilterChangeListener = new FilterAdapter.onFilterChangeListener() {

        @Override
        public void onFilterChanged(MagicFilterType filterType) {
            switchFilterTo(MagicFilterFactory.initFilters(filterType));
        }
    };
}
