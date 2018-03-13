package com.cyl.wanandroid.utils;

import com.blankj.utilcode.util.SPUtils;
import com.cyl.wanandroid.R;
import com.cyl.wanandroid.base.App;
import com.cyl.wanandroid.base.BaseContract;
import com.cyl.wanandroid.bean.Article;
import com.cyl.wanandroid.bean.DataResponse;
import com.cyl.wanandroid.constant.Constant;
import com.cyl.wanandroid.net.ApiService;
import com.cyl.wanandroid.net.RetrofitManager;
import com.cyl.wanandroid.ui.article.ArticleListContract;
import com.cyl.wanandroid.ui.home.HomeContract;
import com.cyl.wanandroid.ui.hotsearch.SearchContract;
import com.cyl.wanandroid.ui.my.LoginActivity;
import com.cyl.wanandroid.ui.my.MyCollectionContract;

import io.reactivex.functions.Consumer;

/**
 * 移植修改
 */
public class ArticleUtils {
    /**
     * 文章收藏
     *
     * @param view
     * @param position
     * @param bean
     */
    public static void collectArticle(final BaseContract.BaseView view, final int position, final Article.DatasBean bean) {
        if (SPUtils.getInstance(Constant.SHARED_NAME).getBoolean(Constant.LOGIN_KEY)) {
            if (bean.isCollect()) {
                RetrofitManager.create(ApiService.class).removeCollectArticle(bean.getId(), -1)
                        .compose(RxSchedulers.<DataResponse>applySchedulers())
                        .compose(view.<DataResponse>bindToLife())
                        .subscribe(new Consumer<DataResponse>() {
                            @Override
                            public void accept(DataResponse response) throws Exception {
                                if (response.getErrorCode() == 0) {
                                    bean.setCollect(!bean.isCollect());
                                    if (view instanceof HomeContract.View) {
                                        ((HomeContract.View) view).collectArticleSuccess(position, bean);
                                    } else if (view instanceof ArticleListContract.View) {
                                        ((ArticleListContract.View) view).collectArticleSuccess(position, bean);
                                    } else if (view instanceof MyCollectionContract.View) {
                                        ((MyCollectionContract.View) view).unCollectArticleSuccess(position);
                                    } else if (view instanceof SearchContract.View) {
                                        ((SearchContract.View) view).collectArticleSuccess(position, bean);
                                    }
                                    view.showSuccess(App.getAppContext().getString(R.string.collection_cancel_success));
                                } else {
                                    view.showFaild(App.getAppContext().getString(R.string.collection_cancel_failed, response.getData()));
                                }
                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                view.showFaild(throwable.getMessage());
                            }
                        });
            } else {
                RetrofitManager.create(ApiService.class).addCollectArticle(bean.getId())
                        .compose(RxSchedulers.<DataResponse>applySchedulers())
                        .compose(view.<DataResponse>bindToLife())
                        .subscribe(new Consumer<DataResponse>() {
                            @Override
                            public void accept(DataResponse response) throws Exception {
                                if (response.getErrorCode() == 0) {
                                    bean.setCollect(!bean.isCollect());
                                    if (view instanceof HomeContract.View) {
                                        ((HomeContract.View) view).collectArticleSuccess(position, bean);
                                    } else if (view instanceof ArticleListContract.View) {
                                        ((ArticleListContract.View) view).collectArticleSuccess(position, bean);
                                    } else if (view instanceof MyCollectionContract.View) {
                                        ((MyCollectionContract.View) view).unCollectArticleSuccess(position);
                                    } else if (view instanceof SearchContract.View) {
                                        ((SearchContract.View) view).collectArticleSuccess(position, bean);
                                    }
                                    view.showSuccess(App.getAppContext().getString(R.string.collection_success));
                                } else {
                                    view.showFaild(App.getAppContext().getString(R.string.collection_failed, response.getErrorMsg()));
                                }
                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                view.showFaild(throwable.getMessage());
                            }
                        });
            }
        } else {
            LoginActivity.start();
        }
    }
}