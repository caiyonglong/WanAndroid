package com.cyl.wanandroid.di.component;

import android.app.Activity;
import android.content.Context;

import com.cyl.wanandroid.di.module.FragmentModule;
import com.cyl.wanandroid.di.scope.ContextLife;
import com.cyl.wanandroid.di.scope.PerFragment;
import com.cyl.wanandroid.ui.article.ArticleListFragment;
import com.cyl.wanandroid.ui.home.HomeFragment;
import com.cyl.wanandroid.ui.hotsearch.HotFragment;
import com.cyl.wanandroid.ui.knowledgesystem.KnowledgeSystemFragment;
import com.cyl.wanandroid.ui.my.MyFragment;

import dagger.Component;

/**
 * Created by lw on 2017/1/19.
 */
@PerFragment
@Component(dependencies = ApplicationComponent.class, modules = FragmentModule.class)
public interface FragmentComponent {
    @ContextLife("Activity")
    Context getActivityContext();

    @ContextLife("Application")
    Context getApplicationContext();

    Activity getActivity();

    void inject(HomeFragment fragment);

    void inject(KnowledgeSystemFragment fragment);

    void inject(MyFragment fragment);

    void inject(ArticleListFragment fragment);

    void inject(HotFragment fragment);
}
