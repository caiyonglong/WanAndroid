package com.cyl.wanandroid.ui.hotsearch;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.cyl.wanandroid.R;
import com.cyl.wanandroid.bean.Friend;

import javax.inject.Inject;

/**
 * Created by lw on 2018/1/23.
 */

public class CommonHotAdapter extends BaseQuickAdapter<Friend, BaseViewHolder> {
    @Inject
    public CommonHotAdapter() {
        super(R.layout.item_hot, null);
    }

    @Override
    protected void convert(BaseViewHolder helper, Friend item) {
        helper.setText(R.id.tvTitle, item.getName());
    }
}
