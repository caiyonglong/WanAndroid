package com.cyl.wanandroid.ui.navigation;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.cyl.wanandroid.R;
import com.cyl.wanandroid.bean.Navigation;
import com.cyl.wanandroid.ui.hotsearch.HotAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import javax.inject.Inject;

/**
 * Created by lw on 2018/1/22.
 */

public class NavigationAdapter extends BaseQuickAdapter<Navigation, BaseViewHolder> {
    private TagFlowLayout mFlowLayout;
    private HotAdapter itemAdapter;

    @Inject
    public NavigationAdapter() {
        super(R.layout.item_knowledge_system, null);
    }

    @Override
    protected void convert(BaseViewHolder helper, Navigation item) {
        helper.setText(R.id.tv_title, item.getName());

        mFlowLayout = helper.getView(R.id.tflCategory);
        itemAdapter = new HotAdapter<>(mContext, item.getArticles());
        mFlowLayout.setAdapter(itemAdapter);
    }


}
