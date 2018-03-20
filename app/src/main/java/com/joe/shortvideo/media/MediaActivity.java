package com.joe.shortvideo.media;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.joe.shortvideo.EditorActivity;
import com.joe.shortvideo.R;

public class MediaActivity extends AppCompatActivity implements View.OnClickListener, SelectedMediaAdapter.OnItemViewCallback {

    private MediaStorage storage;
    private GalleryDirChooser galleryDirChooser;
    private ThumbnailGenerator thumbnailGenerator;
    private GalleryMediaChooser galleryMediaChooser;
    private RecyclerView galleryView;
    private RecyclerView mRvSelectedVideo;
    private TextView mTvTotalDuration;
    private ImageButton back;
    private TextView title;
    private Button mBtnNextStep;

    private SelectedMediaAdapter mSelectedVideoAdapter;
    private MediaInfo mCurrMediaInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media);
        mBtnNextStep = (Button) findViewById(R.id.btn_next_step);
        galleryView = (RecyclerView) findViewById(R.id.gallery_media);
        title = (TextView) findViewById(R.id.gallery_title);
//        title.setText(R.string.gallery_all_media);
        title.setText("导入");
        back = (ImageButton) findViewById(R.id.gallery_close);
        back.setOnClickListener(this);
        storage = new MediaStorage(this, new JSONSupportImpl());
        thumbnailGenerator = new ThumbnailGenerator(this);
        galleryDirChooser = new GalleryDirChooser(this, findViewById(R.id.topPanel), thumbnailGenerator, storage);
        galleryMediaChooser = new GalleryMediaChooser(galleryView, galleryDirChooser, storage, thumbnailGenerator);
        storage.setSortMode(MediaStorage.SORT_MODE_MERGE);
        storage.startFetchmedias();
        storage.setOnMediaDirChangeListener(new MediaStorage.OnMediaDirChange() {
            @Override
            public void onMediaDirChanged() {
                MediaDir dir = storage.getCurrentDir();
                if (dir.id == -1) {
                    title.setText(getString(R.string.gallery_all_media));
                } else {
                    title.setText(dir.dirName);
                }
                galleryMediaChooser.changeMediaDir(dir);
            }
        });
        storage.setOnCurrentMediaInfoChangeListener(new MediaStorage.OnCurrentMediaInfoChange() {
            @Override
            public void onCurrentMediaInfoChanged(MediaInfo info) {
                MediaInfo infoCopy = new MediaInfo();
                infoCopy.addTime = info.addTime;
                if (info.mimeType.startsWith("image")) {
                    infoCopy.duration = 5000;//图片的时长设置为3s
                } else {
                    infoCopy.duration = info.duration;
                }
                infoCopy.filePath = info.filePath;
                infoCopy.id = info.id;
                infoCopy.isSquare = info.isSquare;
                infoCopy.mimeType = info.mimeType;
                infoCopy.thumbnailPath = info.thumbnailPath;
                infoCopy.title = info.title;
                infoCopy.type = info.type;
                mSelectedVideoAdapter.addMedia(infoCopy);
//                mImport.addVideo(infoCopy.filePath, 3000, AliyunDisplayMode.DEFAULT);    //导入器中添加视频
//                mTransCoder.addMedia(infoCopy);
                mCurrMediaInfo = infoCopy;
            }
        });
        mRvSelectedVideo = (RecyclerView) findViewById(R.id.rv_selected_video);
        mTvTotalDuration = (TextView) findViewById(R.id.tv_duration_value);
        mSelectedVideoAdapter = new SelectedMediaAdapter(new MediaImageLoader(this), 5 * 60 * 1000);//最大时长5分钟
        mSelectedVideoAdapter.setItemViewCallback(this);
        mRvSelectedVideo.setAdapter(mSelectedVideoAdapter);
        mRvSelectedVideo.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        mTvTotalDuration.setText(convertDuration2Text(0));
        mTvTotalDuration.setActivated(false);
    }

    @Override
    public void onClick(View v) {
        if (v == back) {
            finish();
        } else if (v.getId() == R.id.btn_next_step) {//点击下一步
            if (mCurrMediaInfo != null) {
                startActivity(new Intent(this, EditorActivity.class).putExtra("path", mCurrMediaInfo.filePath));
            } else {
                Toast.makeText(this, "请选择视频", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private String convertDuration2Text(long duration) {
        int sec = Math.round(((float) duration) / 1000);
        int hour = sec / 3600;
        int min = (sec % 3600) / 60;
        sec = (sec % 60);
        return String.format(getString(R.string.video_duration),
                hour,
                min,
                sec);
    }

    @Override
    public void onItemPhotoClick(MediaInfo info, int position) {

    }

    @Override
    public void onItemDeleteClick(MediaInfo info) {
        mCurrMediaInfo = null;
    }

    @Override
    public void onDurationChange(long currDuration, boolean isReachedMaxDuration) {

    }
}
