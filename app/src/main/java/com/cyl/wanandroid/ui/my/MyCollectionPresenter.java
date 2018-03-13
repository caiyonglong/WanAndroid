package com.cyl.wanandroid.ui.my;

import com.cyl.wanandroid.base.BasePresenter;
import com.cyl.wanandroid.bean.Article;
import com.cyl.wanandroid.bean.DataResponse;
import com.cyl.wanandroid.constant.LoadType;
import com.cyl.wanandroid.net.ApiService;
import com.cyl.wanandroid.net.RetrofitManager;
import com.cyl.wanandroid.utils.ArticleUtils;
import com.cyl.wanandroid.utils.RxSchedulers;

import javax.inject.Inject;

import io.reactivex.functions.Consumer;

/**
 * Created by lw on 2018/2/2.
 */

public class MyCollectionPresenter extends BasePresenter<MyCollectionContract.View> implements MyCollectionContract.Presenter {
    private int mPage;
    private boolean mIsRefresh;

    @Inject
    public MyCollectionPresenter() {
        this.mIsRefresh = true;
    }

    @Override
    public void loadMyCollectArticles() {
        if (mIsRefresh) mView.showLoading();
        RetrofitManager.create(ApiService.class)
                .getCollectArticles(mPage)
                .compose(RxSchedulers.<DataResponse<Article>>applySchedulers())
                .compose(mView.<DataResponse<Article>>bindToLife())
                .subscribe(new Consumer<DataResponse<Article>>() {
                    @Override
                    public void accept(DataResponse<Article> dataResponse) throws Exception {
                        int loadType = mIsRefresh ? LoadType.TYPE_REFRESH_SUCCESS : LoadType.TYPE_LOAD_MORE_SUCCESS;
                        mView.setMyCollectArticles(dataResponse.getData(), loadType);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        int loadType = mIsRefresh ? LoadType.TYPE_REFRESH_ERROR : LoadType.TYPE_LOAD_MORE_ERROR;
                        mView.setMyCollectArticles(new Article(), loadType);
                    }
                });
    }

    @Override
    public void refresh() {
        mPage = 0;
        mIsRefresh = true;
        loadMyCollectArticles();
    }

    @Override
    public void loadMore() {
        mPage++;
        mIsRefresh = false;
        loadMyCollectArticles();
    }

    @Override
    public void unCollectArticle(final int position, final Article.DatasBean bean) {
        ArticleUtils.collectArticle(mView, position, bean);
    }
}
