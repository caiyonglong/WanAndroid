package com.cyl.wanandroid.ui.project;

import com.blankj.utilcode.util.SPUtils;
import com.cyl.wanandroid.R;
import com.cyl.wanandroid.base.App;
import com.cyl.wanandroid.base.BasePresenter;
import com.cyl.wanandroid.bean.Article;
import com.cyl.wanandroid.bean.DataResponse;
import com.cyl.wanandroid.constant.Constant;
import com.cyl.wanandroid.constant.LoadType;
import com.cyl.wanandroid.net.ApiService;
import com.cyl.wanandroid.net.RetrofitManager;
import com.cyl.wanandroid.ui.my.LoginActivity;
import com.cyl.wanandroid.utils.RxSchedulers;

import javax.inject.Inject;

import io.reactivex.functions.Consumer;

/**
 * Created by lw on 2018/1/23.
 */

public class ProjectListPresenter extends BasePresenter<ProjectListContract.View> implements ProjectListContract.Presenter {

    private boolean mIsRefresh;
    private int mPage, mCid;

    @Inject
    public ProjectListPresenter() {
        this.mIsRefresh = true;
    }

    @Override
    public void loadProjectArticles(int cid) {
        this.mCid = cid;
        RetrofitManager.create(ApiService.class).getProjectArticles(mPage, mCid)
                .compose(RxSchedulers.<DataResponse<Article>>applySchedulers())
                .compose(mView.<DataResponse<Article>>bindToLife())
                .subscribe(new Consumer<DataResponse<Article>>() {
                    @Override
                    public void accept(DataResponse<Article> dataResponse) throws Exception {
                        int loadType = mIsRefresh ? LoadType.TYPE_REFRESH_SUCCESS : LoadType.TYPE_LOAD_MORE_SUCCESS;
                        mView.setProjectArticles(dataResponse.getData(), loadType);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        int loadType = mIsRefresh ? LoadType.TYPE_REFRESH_ERROR : LoadType.TYPE_LOAD_MORE_ERROR;
                        mView.setProjectArticles(new Article(), loadType);
                    }
                });
    }

    @Override
    public void refresh() {
        mPage = 0;
        mIsRefresh = true;
        loadProjectArticles(mCid);
    }

    @Override
    public void loadMore() {
        mPage++;
        mIsRefresh = false;
        loadProjectArticles(mCid);
    }

    @Override
    public void collectProject(final int position, final Article.DatasBean bean) {
        if (SPUtils.getInstance(Constant.SHARED_NAME).getBoolean(Constant.LOGIN_KEY)) {
            if (bean.isCollect()) {
                RetrofitManager.create(ApiService.class).removeCollectArticle(bean.getId(), -1)
                        .compose(RxSchedulers.<DataResponse>applySchedulers())
                        .compose(mView.<DataResponse>bindToLife())
                        .subscribe(new Consumer<DataResponse>() {
                            @Override
                            public void accept(DataResponse response) throws Exception {
                                if (response.getErrorCode() == 0) {
                                    bean.setCollect(!bean.isCollect());
                                    mView.collectProjectSuccess(position, bean);
                                    mView.showSuccess(App.getAppContext().getString(R.string.collection_cancel_success));
                                } else {
                                    mView.showFaild(App.getAppContext().getString(R.string.collection_cancel_failed, response.getData()));
                                }
                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                mView.showFaild(throwable.getMessage());
                            }
                        });
            } else {
                RetrofitManager.create(ApiService.class).addCollectArticle(bean.getId())
                        .compose(RxSchedulers.<DataResponse>applySchedulers())
                        .compose(mView.<DataResponse>bindToLife())
                        .subscribe(new Consumer<DataResponse>() {
                            @Override
                            public void accept(DataResponse response) throws Exception {
                                if (response.getErrorCode() == 0) {
                                    bean.setCollect(!bean.isCollect());
                                    mView.collectProjectSuccess(position, bean);
                                    mView.showSuccess(App.getAppContext().getString(R.string.collection_success));
                                } else {
                                    mView.showFaild(App.getAppContext().getString(R.string.collection_failed, response.getErrorMsg()));
                                }
                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                mView.showFaild(throwable.getMessage());
                            }
                        });
            }
        } else {
            LoginActivity.start();
        }
    }
}
