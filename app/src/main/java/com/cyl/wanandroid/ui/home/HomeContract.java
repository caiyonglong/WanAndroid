package com.cyl.wanandroid.ui.home;

import com.cyl.wanandroid.base.BaseContract;
import com.cyl.wanandroid.bean.Article;
import com.cyl.wanandroid.bean.Banner;
import com.cyl.wanandroid.constant.LoadType;

import java.util.List;

/**
 * Created by lw on 2018/1/18.
 */

public interface HomeContract {

    interface View extends BaseContract.BaseView {
        void setHomeBanners(List<Banner> banners);

        void setHomeArticles(Article article, @LoadType.checker int loadType);

        void collectArticleSuccess(int position, Article.DatasBean bean);
    }

    interface Presenter extends BaseContract.BasePresenter<View> {
        void loadHomeBanners();

        void loadHomeArticles();

        void refresh();

        void loadMore();

        void collectArticle(int position, Article.DatasBean bean);

        void loadHomeData();
    }
}
