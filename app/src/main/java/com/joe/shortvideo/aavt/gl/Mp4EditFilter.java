package com.joe.shortvideo.aavt.gl;

import android.content.res.Resources;

/**
 * Created by qiaobing on 2018/3/20.
 */

public class Mp4EditFilter extends GroupFilter {
    private ChooseFilter chooseFilter;
    private DistortionFilter distortionFilter;

    public Mp4EditFilter(Resources resource) {
        super(resource);
    }

    @Override
    protected void initBuffer() {
        super.initBuffer();
        chooseFilter = new ChooseFilter(mRes);
        distortionFilter = new DistortionFilter(mRes);
        addFilter(chooseFilter);
        addFilter(distortionFilter);
    }

    public ChooseFilter getChooseFilter() {
        return chooseFilter;
    }

    public DistortionFilter getDistortionFilter() {
        return distortionFilter;
    }
}
