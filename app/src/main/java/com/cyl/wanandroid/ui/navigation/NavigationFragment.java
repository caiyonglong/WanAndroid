package com.cyl.wanandroid.ui.navigation;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.alibaba.android.arouter.launcher.ARouter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.cyl.wanandroid.R;
import com.cyl.wanandroid.base.BaseFragment;
import com.cyl.wanandroid.bean.Navigation;
import com.cyl.wanandroid.constant.Constant;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

/**
 * Created by lw on 2018/1/18.
 */

public class NavigationFragment extends BaseFragment<NavigationPresenter> implements NavigationContract.View,
        NavigationAdapter.OnItemClickListener, SwipeRefreshLayout.OnRefreshListener {
    @BindView(R.id.rvNavigations)
    RecyclerView mRvNavigations;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @Inject
    NavigationAdapter mNavigationAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_navigation;
    }

    @Override
    protected void initInjector() {
        mFragmentComponent.inject(this);
    }

    @Override
    protected void initView(View view) {
        /**设置RecyclerView*/
        mRvNavigations.setLayoutManager(new LinearLayoutManager(getContext()));
        mRvNavigations.setAdapter(mNavigationAdapter);

        /**设置事件监听*/
        mNavigationAdapter.setOnItemClickListener(this);
        mSwipeRefreshLayout.setOnRefreshListener(this);

        /**请求数据*/
        mPresenter.loadNavigations();
    }

    @Override
    public void showLoading() {
        mSwipeRefreshLayout.setRefreshing(true);
    }

    @Override
    public void setNavigations(List<Navigation> knowledgeSystems) {
        mNavigationAdapter.setNewData(knowledgeSystems);
        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onRefresh() {
        mPresenter.refresh();
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        ARouter.getInstance().build("/article/ArticleTypeActivity")
                .withString(Constant.CONTENT_TITLE_KEY, mNavigationAdapter.getItem(position).getName())
                .withObject(Constant.CONTENT_CHILDREN_DATA_KEY, mNavigationAdapter.getItem(position).getArticles())
                .navigation();
    }

    public static NavigationFragment newInstance() {
        return new NavigationFragment();
    }

}
