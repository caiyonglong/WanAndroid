package com.cyl.wanandroid.ui.project;

import android.text.Html;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.cyl.wanandroid.R;
import com.cyl.wanandroid.bean.Article;

import javax.inject.Inject;

/**
 * Created by lw on 2018/1/19.
 */

public class ProjectAdapter extends BaseQuickAdapter<Article.DatasBean, BaseViewHolder> {

    @Inject
    public ProjectAdapter() {
        super(R.layout.item_project, null);
    }

    @Override
    protected void convert(BaseViewHolder helper, Article.DatasBean item) {
        helper.setText(R.id.tvAuthor, item.getAuthor());
        helper.setText(R.id.tvNiceDate, item.getNiceDate());
        helper.setText(R.id.tvTitle, Html.fromHtml(item.getTitle()));
        helper.setText(R.id.tv_des, item.getDesc());

        helper.setImageResource(R.id.ivCollect, item.isCollect()
                ? R.drawable.ic_action_like : R.drawable.ic_action_no_like);
        helper.addOnClickListener(R.id.ivCollect);

        Glide.with(helper.itemView)
                .load(item.getEnvelopePic())
                .into((ImageView) helper.getView(R.id.iv_des));
    }

}
