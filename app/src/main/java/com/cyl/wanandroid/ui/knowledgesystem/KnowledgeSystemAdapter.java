package com.cyl.wanandroid.ui.knowledgesystem;

import android.view.View;

import com.alibaba.android.arouter.launcher.ARouter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.cyl.wanandroid.R;
import com.cyl.wanandroid.bean.KnowledgeSystem;
import com.cyl.wanandroid.constant.Constant;
import com.cyl.wanandroid.ui.hotsearch.HotAdapter;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagFlowLayout;

import javax.inject.Inject;

/**
 * Created by lw on 2018/1/22.
 */

public class KnowledgeSystemAdapter extends BaseQuickAdapter<KnowledgeSystem, BaseViewHolder> {
    private TagFlowLayout mFlowLayout;
    private HotAdapter itemAdapter;

    @Inject
    public KnowledgeSystemAdapter() {
        super(R.layout.item_knowledge_system, null);
    }

    @Override
    protected void convert(BaseViewHolder helper, final KnowledgeSystem item) {
        helper.setText(R.id.tv_title, item.getName());
        mFlowLayout = helper.getView(R.id.tflCategory);
        itemAdapter = new HotAdapter<>(mContext, item.getChildren());
        mFlowLayout.setAdapter(itemAdapter);
        mFlowLayout.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent) {
                ARouter.getInstance().build("/article/ArticleTypeActivity")
                        .withString(Constant.CONTENT_TITLE_KEY, item.getName())
                        .withObject(Constant.CONTENT_CHILDREN_DATA_KEY, item.getChildren())
                        .withInt(Constant.CONTENT_CHILDREN_POSITION_KEY, position)
                        .navigation();
                return true;
            }
        });
    }

}
