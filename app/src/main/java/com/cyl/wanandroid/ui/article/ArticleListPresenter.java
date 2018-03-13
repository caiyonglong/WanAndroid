package com.cyl.wanandroid.ui.article;

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
 * Created by lw on 2018/1/23.
 */

public class ArticleListPresenter extends BasePresenter<ArticleListContract.View> implements ArticleListContract.Presenter {

    private boolean mIsRefresh;
    private int mPage, mCid;

    @Inject
    public ArticleListPresenter() {
        this.mIsRefresh = true;
    }

    @Override
    public void loadKnowledgeSystemArticles(int cid) {
        this.mCid = cid;
        RetrofitManager.create(ApiService.class).getKnowledgeSystemArticles(mPage, mCid)
                .compose(RxSchedulers.<DataResponse<Article>>applySchedulers())
                .compose(mView.<DataResponse<Article>>bindToLife())
                .subscribe(new Consumer<DataResponse<Article>>() {
                    @Override
                    public void accept(DataResponse<Article> dataResponse) throws Exception {
                        int loadType = mIsRefresh ? LoadType.TYPE_REFRESH_SUCCESS : LoadType.TYPE_LOAD_MORE_SUCCESS;
                        mView.setKnowledgeSystemArticles(dataResponse.getData(), loadType);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        int loadType = mIsRefresh ? LoadType.TYPE_REFRESH_ERROR : LoadType.TYPE_LOAD_MORE_ERROR;
                        mView.setKnowledgeSystemArticles(new Article(), loadType);
                    }
                });
    }

    @Override
    public void refresh() {
        mPage = 0;
        mIsRefresh = true;
        loadKnowledgeSystemArticles(mCid);
    }

    @Override
    public void loadMore() {
        mPage++;
        mIsRefresh = false;
        loadKnowledgeSystemArticles(mCid);
    }

    @Override
    public void collectArticle(final int position, final Article.DatasBean bean) {
        ArticleUtils.collectArticle(mView, position, bean);
    }
}
