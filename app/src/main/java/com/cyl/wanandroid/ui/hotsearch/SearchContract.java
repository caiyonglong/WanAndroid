package com.cyl.wanandroid.ui.hotsearch;

import com.cyl.wanandroid.base.BaseContract;
import com.cyl.wanandroid.bean.Article;
import com.cyl.wanandroid.bean.Friend;
import com.cyl.wanandroid.bean.HotKey;
import com.cyl.wanandroid.constant.LoadType;
import com.cyl.wanandroid.db.HistoryModel;

import java.util.List;

/**
 * Created by lw on 2018/1/23.
 */

public interface SearchContract {
    interface View extends BaseContract.BaseView {
        void setSearchArtcles(Article article, @LoadType.checker int loadType);

        void collectArticleSuccess(int position, Article.DatasBean bean);

        void setHistory(List<HistoryModel> historyModels);

        void addHistorySuccess(HistoryModel historyModel);

        void setHotData(List<HotKey> hotKeys, List<Friend> friends);
    }

    interface Presenter extends BaseContract.BasePresenter<SearchContract.View> {
        void loadSearchArtcles(String k);

        void refresh();

        void loadMore();

        void collectArticle(int position, Article.DatasBean bean);

        void loadHistory();

        void loadHotData();

        void addHistory(String name);
    }
}
