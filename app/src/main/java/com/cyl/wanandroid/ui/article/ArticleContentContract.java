package com.cyl.wanandroid.ui.article;

import com.cyl.wanandroid.base.BaseContract;

/**
 * Created by lw on 2018/1/25.
 */

public interface ArticleContentContract {
    interface View extends BaseContract.BaseView {

    }

    interface Presenter extends BaseContract.BasePresenter<ArticleContentContract.View> {
        void collectArticle(int id);

        void collectOutsideArticle(String title, String author, String link);
    }
}
