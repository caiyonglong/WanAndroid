package com.cyl.wanandroid.di.component;

import android.app.Activity;
import android.content.Context;

import com.cyl.wanandroid.di.module.ActivityModule;
import com.cyl.wanandroid.di.scope.ContextLife;
import com.cyl.wanandroid.di.scope.PerActivity;
import com.cyl.wanandroid.ui.article.ArticleContentActivity;
import com.cyl.wanandroid.ui.hotsearch.SearchActivity;
import com.cyl.wanandroid.ui.my.LoginActivity;
import com.cyl.wanandroid.ui.my.MyBookmarkActivity;
import com.cyl.wanandroid.ui.my.MyCollectionActivity;
import com.cyl.wanandroid.ui.my.RegisterActivity;

import dagger.Component;

/**
 * Created by lw on 2017/1/19.
 */
@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = ActivityModule.class)
public interface ActivityComponent {

    @ContextLife("Activity")
    Context getActivityContext();

    @ContextLife("Application")
    Context getApplicationContext();

    Activity getActivity();

    void inject(SearchActivity activity);

    void inject(LoginActivity activity);

    void inject(RegisterActivity activity);

    void inject(ArticleContentActivity activity);

    void inject(MyCollectionActivity activity);

    void inject(MyBookmarkActivity activity);
}
