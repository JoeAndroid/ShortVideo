package com.joe.shortvideo.adapter;


import com.joe.shortvideo.Entity.FilterChoose;
import com.joe.shortvideo.widget.WheelView.WheelView;

import java.util.List;

/**
 * Created by Administrator on 2017/10/20.
 */

public class SelectorFilterAdapter extends WheelView.WheelAdapter {

	private List<FilterChoose> strs;

	public SelectorFilterAdapter(List<FilterChoose> filterChooses) {
		strs = filterChooses;
	}

	@Override
	protected int getItemCount() {
		return strs.size();
	}

	@Override
	protected String getItem(int index) {
		return strs.get(index).getName();
	}
}
