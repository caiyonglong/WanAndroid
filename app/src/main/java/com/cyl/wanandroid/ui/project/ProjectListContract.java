package com.cyl.wanandroid.ui.project;

import com.cyl.wanandroid.base.BaseContract;
import com.cyl.wanandroid.bean.Article;
import com.cyl.wanandroid.constant.LoadType;

/**
 * Created by lw on 2018/1/23.
 */

public interface ProjectListContract {
    interface View extends BaseContract.BaseView {

        void setProjectArticles(Article article, @LoadType.checker int loadType);

        void collectProjectSuccess(int position, Article.DatasBean bean);

    }

    interface Presenter extends BaseContract.BasePresenter<ProjectListContract.View> {
        void loadProjectArticles(int cid);

        void refresh();

        void loadMore();

        void collectProject(int position, Article.DatasBean bean);
    }
}
