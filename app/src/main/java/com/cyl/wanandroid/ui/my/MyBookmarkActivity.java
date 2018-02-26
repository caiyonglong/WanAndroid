package com.cyl.wanandroid.ui.my;

import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.cyl.wanandroid.R;
import com.cyl.wanandroid.base.BaseActivity;
import com.cyl.wanandroid.bean.Friend;
import com.cyl.wanandroid.ui.article.ArticleContentActivity;
import com.cyl.wanandroid.ui.hotsearch.HotAdapter;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.List;

import butterknife.BindView;

/**
 * Created by lw on 2018/1/25.
 */
@Route(path = "/my/MyBookmarkActivity")
public class MyBookmarkActivity extends BaseActivity<MyBookmarkPresenter> implements MyBookmarkContract.View, SwipeRefreshLayout.OnRefreshListener {
    @BindView(R.id.tflMyBookmarks)
    TagFlowLayout mTflMyBookmarks;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout mSwipeRefreshLayout;
    private HotAdapter<Friend> mBookmarkAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_my_bookmark;
    }

    @Override
    protected void initInjector() {
        mActivityComponent.inject(this);
    }

    @Override
    protected void initView() {
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mPresenter.loadMyBookmarks();

        mTflMyBookmarks.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent) {
                ArticleContentActivity.start(mBookmarkAdapter.getItem(position).getId(),
                        mBookmarkAdapter.getItem(position).getLink(), mBookmarkAdapter.getItem(position).getName(),
                        null);
                return false;
            }
        });

    }

    @Override
    protected boolean showHomeAsUp() {
        return true;
    }

    @Override
    public void setMyBookmarks(List<Friend> bookmarks) {
        mBookmarkAdapter = new HotAdapter(this, bookmarks);
        mTflMyBookmarks.setAdapter(mBookmarkAdapter);
        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void showLoading() {
        mSwipeRefreshLayout.setRefreshing(true);
    }

    @Override
    public void onRefresh() {
        mPresenter.refresh();
    }
}
