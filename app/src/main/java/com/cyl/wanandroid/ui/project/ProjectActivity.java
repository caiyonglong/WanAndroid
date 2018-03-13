package com.cyl.wanandroid.ui.project;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.cyl.wanandroid.R;
import com.cyl.wanandroid.base.BaseActivity;
import com.cyl.wanandroid.bean.KnowledgeSystem;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by lw on 2018/1/22.
 */
@Route(path = "/project/ProjectActivity")
public class ProjectActivity extends BaseActivity<ProjectPresenter> implements ProjectContract.View {

    @BindView(R.id.tabArticleTypes)
    TabLayout mTabArticleTypes;
    @BindView(R.id.vpArticleTypes)
    ViewPager mVpArticleTypes;

    List<KnowledgeSystem.ChildrenBean> childrenData = new ArrayList<>();

    ProjectFragmentPagerAdapter mArticleTypeFragmentPagerAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_article_type;
    }

    @Override
    protected void initInjector() {
        mActivityComponent.inject(this);
    }

    @Override
    protected void initView() {
        setToolbarTitle(getApplicationContext().getString(R.string.title_project));
        mPresenter.loadProjects();
    }

    @Override
    protected boolean showHomeAsUp() {
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_type_content, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menuShare) {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.putExtra(Intent.EXTRA_TEXT, getString(R.string.share_type_url, getString(R.string.app_name),
                    childrenData.get(mTabArticleTypes.getSelectedTabPosition()).getName(), childrenData.get(mTabArticleTypes.getSelectedTabPosition()).getId()));
            intent.setType("text/plain");
            startActivity(Intent.createChooser(intent, getString(R.string.share_title)));
        } else if (item.getItemId() == R.id.menuSearch) {
            ARouter.getInstance().build("/hotsearch/SearchActivity").navigation();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void setProjects(List<KnowledgeSystem.ChildrenBean> projects) {
        childrenData = projects;
        mArticleTypeFragmentPagerAdapter = new ProjectFragmentPagerAdapter(getSupportFragmentManager(), projects);
        mVpArticleTypes.setAdapter(mArticleTypeFragmentPagerAdapter);
        mTabArticleTypes.setupWithViewPager(mVpArticleTypes);
    }
}
