package com.cyl.wanandroid.ui.hotsearch;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.cyl.wanandroid.R;
import com.cyl.wanandroid.base.BaseActivity;
import com.cyl.wanandroid.bean.Article;
import com.cyl.wanandroid.bean.Friend;
import com.cyl.wanandroid.bean.HotKey;
import com.cyl.wanandroid.bean.KnowledgeSystem;
import com.cyl.wanandroid.constant.Constant;
import com.cyl.wanandroid.db.HistoryModel;
import com.cyl.wanandroid.event.LoginEvent;
import com.cyl.wanandroid.ui.article.ArticleAdapter;
import com.cyl.wanandroid.ui.article.ArticleContentActivity;
import com.cyl.wanandroid.utils.RxBus;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import io.reactivex.functions.Consumer;

/**
 * Created by lw on 2018/1/23.
 */
@Route(path = "/hotsearch/SearchActivity")
public class SearchActivity extends BaseActivity<SearchPresenter> implements SearchContract.View, ArticleAdapter.OnItemClickListener, ArticleAdapter.OnItemChildClickListener,
        SwipeRefreshLayout.OnRefreshListener, ArticleAdapter.RequestLoadMoreListener {
    @BindView(R.id.rvArticleList)
    RecyclerView mRvArticleList;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @Inject
    ArticleAdapter mArticleAdapter;
    @Autowired
    public String hotNameKey;

    private HistoryAdapter mHistoryAdapter;
    private SearchView mSearchView;
    private List<HistoryModel> mHistoryModels;
    private HotAdapter<HotKey> mHotKeyAdapter;
    private HotAdapter<Friend> mHotFriendAdapter, mBookMarkAdapter;
    private View mSearchHeadView;
    private TagFlowLayout mTflHistorys, mTtlBookMarks, mTflHotKeys, mTflHotFriends;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_search;
    }

    @Override
    protected void initInjector() {
        mActivityComponent.inject(this);
    }

    @Override
    protected void initView() {
        /**设置RecyclerView*/
        mRvArticleList.setLayoutManager(new LinearLayoutManager(this));
        mRvArticleList.setAdapter(mArticleAdapter);
        /**设置SearchHeadView*/
        mSearchHeadView = LayoutInflater.from(this).inflate(R.layout.layout_search_head, null);
        mTflHistorys = (TagFlowLayout) mSearchHeadView.findViewById(R.id.tflHistorys);
        mTtlBookMarks = (TagFlowLayout) mSearchHeadView.findViewById(R.id.tflBookMarks);
        mTflHotKeys = (TagFlowLayout) mSearchHeadView.findViewById(R.id.tflHotKeys);
        mTflHotFriends = (TagFlowLayout) mSearchHeadView.findViewById(R.id.tflHotFriends);

        mArticleAdapter.addHeaderView(mSearchHeadView);


        /**设置事件监听*/
        setListener();

        /**加载历史搜索记录*/
        mPresenter.loadHistory();

        /**加载热门记录*/
        mPresenter.loadHotData();


        /**登陆成功刷新*/
        RxBus.getInstance().toFlowable(LoginEvent.class)
                .subscribe(new Consumer<LoginEvent>() {
                    @Override
                    public void accept(LoginEvent event) throws Exception {
                        mPresenter.refresh();
                    }
                });

        mTflHistorys.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent) {
                String name = mHistoryAdapter.getItem(position).getName();
                mSearchView.setQuery(name, false);
                ARouter.getInstance().build("/hotsearch/SearchDetailActivity")
                        .withString(Constant.CONTENT_HOT_NAME_KEY, name)
                        .navigation();
                return false;
            }
        });
    }

    @Override
    protected boolean showHomeAsUp() {
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);
        mSearchView = (SearchView) menu.findItem(R.id.menuSearch).getActionView();
        mSearchView.setMaxWidth(1920);
        mSearchView.setIconified(false);
        mSearchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                SearchActivity.this.finish();
                return true;
            }
        });
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                mPresenter.addHistory(query);
                ARouter.getInstance().build("/hotsearch/SearchDetailActivity")
                        .withString(Constant.CONTENT_HOT_NAME_KEY, query)
                        .navigation();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        /**是否是从hot页面过来的*/
        mSearchView.setQuery(hotNameKey, true);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onRefresh() {
        mPresenter.refresh();
    }

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        if (view.getId() == R.id.tvChapterName) {
            List<KnowledgeSystem.ChildrenBean> childrenBeans = new ArrayList<>();
            childrenBeans.add(new KnowledgeSystem.ChildrenBean(mArticleAdapter.getItem(position).getChapterId(),
                    mArticleAdapter.getItem(position).getChapterName()));
            ARouter.getInstance().build("/article/ArticleTypeActivity")
                    .withString(Constant.CONTENT_TITLE_KEY, mArticleAdapter.getItem(position).getChapterName())
                    .withObject(Constant.CONTENT_CHILDREN_DATA_KEY, childrenBeans)
                    .navigation();
        } else if (view.getId() == R.id.ivCollect) {
            mPresenter.collectArticle(position, mArticleAdapter.getItem(position));
        }
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        ArticleContentActivity.start(mArticleAdapter.getItem(position).getId(),
                mArticleAdapter.getItem(position).getLink(), mArticleAdapter.getItem(position).getTitle(),
                mArticleAdapter.getItem(position).getAuthor());
    }

    private void setListener() {
        mArticleAdapter.setOnItemClickListener(this);
        mArticleAdapter.setOnItemChildClickListener(this);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mArticleAdapter.setOnLoadMoreListener(this);

        mTflHotKeys.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent) {
                String name = mHotKeyAdapter.getItem(position).getName();
                mSearchView.setQuery(name, false);
                ARouter.getInstance().build("/hotsearch/SearchDetailActivity")
                        .withString(Constant.CONTENT_HOT_NAME_KEY, name)
                        .navigation();
                return false;
            }
        });
        mTflHotFriends.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent) {
                ArticleContentActivity.start(mHotFriendAdapter.getItem(position).getId(),
                        mHotFriendAdapter.getItem(position).getLink(), mHotFriendAdapter.getItem(position).getName(),
                        null);
                return false;
            }
        });
    }

    @Override
    public void showLoading() {
        mSwipeRefreshLayout.setRefreshing(true);
    }

    @Override
    public void onLoadMoreRequested() {
        mPresenter.loadMore();
    }

    @Override
    public void setSearchArtcles(Article article, int loadType) {
        setLoadDataResult(mArticleAdapter, mSwipeRefreshLayout, article.getDatas(), loadType);
    }

    @Override
    public void collectArticleSuccess(int position, Article.DatasBean bean) {
        mArticleAdapter.setData(position, bean);
    }

    @Override
    public void setHistory(List<HistoryModel> historyModels) {
        this.mHistoryModels = historyModels;
        mHistoryAdapter = new HistoryAdapter(this, mHistoryModels);
        mTflHistorys.setAdapter(mHistoryAdapter);
        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void addHistorySuccess(HistoryModel historyModel) {
        if (mHistoryModels != null) mHistoryModels.add(0, historyModel);
        mHistoryAdapter.notifyDataChanged();
    }

    @Override
    public void setHotData(List<HotKey> hotKeys, List<Friend> friends) {
        mHotKeyAdapter = new HotAdapter(this, hotKeys);
        mTflHotKeys.setAdapter(mHotKeyAdapter);

        mHotFriendAdapter = new HotAdapter<>(this, friends);
        mTflHotFriends.setAdapter(mHotFriendAdapter);

        mSwipeRefreshLayout.setRefreshing(false);
    }
}
