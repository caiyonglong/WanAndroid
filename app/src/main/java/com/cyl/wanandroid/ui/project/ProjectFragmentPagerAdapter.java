package com.cyl.wanandroid.ui.project;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.alibaba.android.arouter.launcher.ARouter;
import com.cyl.wanandroid.bean.KnowledgeSystem;
import com.cyl.wanandroid.constant.Constant;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by lw on 2018/1/22.
 */

public class ProjectFragmentPagerAdapter extends FragmentPagerAdapter {

    @Nullable
    private List<KnowledgeSystem.ChildrenBean> mChildrenData;
    private List<ProjectListFragment> mArticleTypeFragments;

    @Inject
    public ProjectFragmentPagerAdapter(FragmentManager fm, List<KnowledgeSystem.ChildrenBean> childrenData) {
        super(fm);
        this.mChildrenData = childrenData;
        mArticleTypeFragments = new ArrayList<>();
        if (mChildrenData == null) return;
        for (KnowledgeSystem.ChildrenBean childrenBean : mChildrenData) {
            ProjectListFragment articleListFragment = (ProjectListFragment) ARouter.getInstance()
                    .build("/project/ProjectListFragment")
                    .withInt(Constant.CONTENT_CID_KEY, childrenBean.getId())
                    .navigation();
            mArticleTypeFragments.add(articleListFragment);
        }
    }

    @Override
    public Fragment getItem(int position) {
        return mArticleTypeFragments.get(position);
    }

    @Override
    public int getCount() {
        return mArticleTypeFragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mChildrenData.get(position).getName();
    }
}
