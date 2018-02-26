package com.cyl.wanandroid.ui.my;

import com.cyl.wanandroid.base.BaseContract;
import com.cyl.wanandroid.bean.Article;
import com.cyl.wanandroid.constant.LoadType;

/**
 * Created by lw on 2018/2/2.
 */

public interface MyCollectionContract {
    interface View extends BaseContract.BaseView {
        void setMyCollectArticles(Article article, @LoadType.checker int loadType);

        void unCollectArticleSuccess(int position);
    }

    interface Presenter extends BaseContract.BasePresenter<MyCollectionContract.View> {
        void loadMyCollectArticles();

        void refresh();

        void loadMore();

        void unCollectArticle(int position, Article.DatasBean bean);
    }
}
