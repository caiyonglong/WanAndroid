package com.cyl.wanandroid.ui.project;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.cyl.wanandroid.R;
import com.cyl.wanandroid.base.BaseFragment;
import com.cyl.wanandroid.bean.Article;
import com.cyl.wanandroid.event.LoginEvent;
import com.cyl.wanandroid.ui.article.ArticleAdapter;
import com.cyl.wanandroid.ui.article.ArticleContentActivity;
import com.cyl.wanandroid.utils.RxBus;

import javax.inject.Inject;

import butterknife.BindView;
import io.reactivex.functions.Consumer;

/**
 * Created by lw on 2018/1/22.
 */
@Route(path = "/project/ProjectListFragment")
public class ProjectListFragment extends BaseFragment<ProjectListPresenter> implements ProjectListContract.View, ArticleAdapter.OnItemClickListener, ArticleAdapter.OnItemChildClickListener,
        SwipeRefreshLayout.OnRefreshListener, ArticleAdapter.RequestLoadMoreListener {
    @BindView(R.id.rvArticleList)
    RecyclerView mRvArticleList;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @Autowired
    public int cid;
    @Inject
    ProjectAdapter mProjectAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_article_list;
    }

    @Override
    protected void initInjector() {
        mFragmentComponent.inject(this);
    }

    @Override
    protected void initView(View view) {

        /**设置RecyclerView*/
        mRvArticleList.setLayoutManager(new LinearLayoutManager(getContext()));
        mRvArticleList.setAdapter(mProjectAdapter);

        /**设置事件监听*/
        mProjectAdapter.setOnItemClickListener(this);
        mProjectAdapter.setOnItemChildClickListener(this);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mProjectAdapter.setOnLoadMoreListener(this);

        /**请求数据*/
        mPresenter.loadProjectArticles(cid);

        /**登陆成功刷新*/
        RxBus.getInstance().toFlowable(LoginEvent.class)
                .subscribe(new Consumer<LoginEvent>() {
                    @Override
                    public void accept(LoginEvent event) throws Exception {
                        mPresenter.refresh();
                    }
                });
    }

    @Override
    public void showLoading() {
        mSwipeRefreshLayout.setRefreshing(true);
    }

    @Override
    public void onRefresh() {
        mPresenter.refresh();
    }

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        if (view.getId() == R.id.ivCollect) {
            mPresenter.collectProject(position, mProjectAdapter.getItem(position));
        }
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        ArticleContentActivity.start(mProjectAdapter.getItem(position).getId(),
                mProjectAdapter.getItem(position).getLink(), mProjectAdapter.getItem(position).getTitle(),
                mProjectAdapter.getItem(position).getAuthor());
    }

    @Override
    public void onLoadMoreRequested() {
        mPresenter.loadMore();
    }

    @Override
    public void setProjectArticles(Article article, int loadType) {
        setLoadDataResult(mProjectAdapter, mSwipeRefreshLayout, article.getDatas(), loadType);
    }

    @Override
    public void collectProjectSuccess(int position, Article.DatasBean bean) {
        mProjectAdapter.setData(position, bean);
    }
}
