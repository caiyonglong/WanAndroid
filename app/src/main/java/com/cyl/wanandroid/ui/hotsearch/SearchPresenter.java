package com.cyl.wanandroid.ui.hotsearch;

import com.blankj.utilcode.util.SPUtils;
import com.cyl.wanandroid.R;
import com.cyl.wanandroid.base.App;
import com.cyl.wanandroid.base.BasePresenter;
import com.cyl.wanandroid.bean.Article;
import com.cyl.wanandroid.bean.DataResponse;
import com.cyl.wanandroid.bean.Friend;
import com.cyl.wanandroid.bean.HotKey;
import com.cyl.wanandroid.constant.Constant;
import com.cyl.wanandroid.constant.LoadType;
import com.cyl.wanandroid.db.HistoryModel;
import com.cyl.wanandroid.db.HistoryModel_Table;
import com.cyl.wanandroid.net.ApiService;
import com.cyl.wanandroid.net.RetrofitManager;
import com.cyl.wanandroid.ui.my.LoginActivity;
import com.cyl.wanandroid.utils.RxSchedulers;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;

/**
 * Created by lw on 2018/1/23.
 */

public class SearchPresenter extends BasePresenter<SearchContract.View> implements SearchContract.Presenter {
    private int mPage;
    private boolean mIsRefresh;
    private String mK;

    @Inject
    public SearchPresenter() {
        this.mIsRefresh = true;
    }

    @Override
    public void loadSearchArtcles(String k) {
        this.mK = k;
        RetrofitManager.create(ApiService.class)
                .getSearchArticles(mPage, mK)
                .compose(RxSchedulers.<DataResponse<Article>>applySchedulers())
                .compose(mView.<DataResponse<Article>>bindToLife())
                .subscribe(new Consumer<DataResponse<Article>>() {
                    @Override
                    public void accept(DataResponse<Article> dataResponse) throws Exception {
                        int loadType = mIsRefresh ? LoadType.TYPE_REFRESH_SUCCESS : LoadType.TYPE_LOAD_MORE_SUCCESS;
                        mView.setSearchArtcles(dataResponse.getData(), loadType);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        int loadType = mIsRefresh ? LoadType.TYPE_REFRESH_ERROR : LoadType.TYPE_LOAD_MORE_ERROR;
                        mView.setSearchArtcles(new Article(), loadType);
                    }
                });

    }

    @Override
    public void refresh() {
        mPage = 0;
        mIsRefresh = true;
        loadSearchArtcles(mK);
        loadHotData();
    }

    @Override
    public void loadMore() {
        mPage++;
        mIsRefresh = false;
        loadSearchArtcles(mK);
    }

    @Override
    public void collectArticle(final int position, final Article.DatasBean bean) {
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
                                    mView.collectArticleSuccess(position, bean);
                                    mView.showSuccess(App.getAppContext().getString(R.string.collection_cancel_success));
                                } else {
                                    mView.showFaild(App.getAppContext().getString(R.string.collection_cancel_failed, response.getErrorMsg()));
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
                                    mView.collectArticleSuccess(position, bean);
                                    mView.showSuccess(App.getAppContext().getString(R.string.collection_success));
                                } else {
                                    mView.showFaild(App.getAppContext().getString(R.string.collection_failed, response.getData()));
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

    @Override
    public void loadHistory() {
        mView.showLoading();
        Observable.create(new ObservableOnSubscribe<List<HistoryModel>>() {
            @Override
            public void subscribe(ObservableEmitter<List<HistoryModel>> e) throws Exception {
                List<HistoryModel> historyModels = SQLite.select().from(HistoryModel.class)
                        .orderBy(HistoryModel_Table.date, false)
                        .limit(10).offset(0)
                        .queryList();
                e.onNext(historyModels);
            }
        }).compose(RxSchedulers.<List<HistoryModel>>applySchedulers()).compose(mView.<List<HistoryModel>>bindToLife()).subscribe(new Consumer<List<HistoryModel>>() {
            @Override
            public void accept(List<HistoryModel> historyModels) throws Exception {
                mView.setHistory(historyModels);
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                mView.showFaild(throwable.getMessage());
            }
        });

    }

    @Override
    public void loadHotData() {
        mView.showLoading();
        Observable<DataResponse<List<Friend>>> observableFriend = RetrofitManager.create(ApiService.class).getHotFriends();
        Observable<DataResponse<List<HotKey>>> observableHotKey = RetrofitManager.create(ApiService.class).getHotKeys();
        Observable.zip(observableFriend, observableHotKey, new BiFunction<DataResponse<List<Friend>>, DataResponse<List<HotKey>>, Map<String, Object>>() {
            @Override
            public Map<String, Object> apply(DataResponse<List<Friend>> response, DataResponse<List<HotKey>> response2) throws Exception {
                Map<String, Object> objMap = new HashMap<>();
                objMap.put(Constant.CONTENT_HOT_KEY, response2.getData());
                objMap.put(Constant.CONTENT_HOT_FRIEND_KEY, response.getData());
                return objMap;
            }
        }).compose(RxSchedulers.<Map<String, Object>>applySchedulers()).compose(mView.<Map<String, Object>>bindToLife()).subscribe(new Consumer<Map<String, Object>>() {
            @Override
            public void accept(Map<String, Object> map) throws Exception {
                List<HotKey> hotKeys = (List<HotKey>) map.get(Constant.CONTENT_HOT_KEY);
                List<Friend> friends = (List<Friend>) map.get(Constant.CONTENT_HOT_FRIEND_KEY);
                mView.setHotData(hotKeys, friends);
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                mView.showFaild(throwable.getMessage());
            }
        });
    }

    @Override
    public void addHistory(String name) {
        HistoryModel historyModel = new HistoryModel();
        historyModel.setName(name);
        historyModel.setDate(new Date());
        long id = historyModel.insert();
        if (id > 0) mView.addHistorySuccess(historyModel);
    }
}
