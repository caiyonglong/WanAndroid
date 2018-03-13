package com.cyl.wanandroid;

import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.cyl.wanandroid.base.BaseActivity;
import com.cyl.wanandroid.base.BaseFragment;
import com.cyl.wanandroid.constant.Constant;
import com.cyl.wanandroid.event.LoginEvent;
import com.cyl.wanandroid.net.CookiesManager;
import com.cyl.wanandroid.ui.home.HomeFragment;
import com.cyl.wanandroid.ui.knowledgesystem.KnowledgeSystemFragment;
import com.cyl.wanandroid.ui.my.LoginActivity;
import com.cyl.wanandroid.utils.RxBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.functions.Consumer;

public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;
    @BindView(R.id.nav_view)
    NavigationView mNavigation;

    private CircleImageView mCivAvatar;
    private TextView mTvNick;
    private LinearLayout mLlLogout;

    private List<BaseFragment> mFragments;
    private int mCurPosition;
    private long mExitTime;
    private boolean mIsLogin;

    private HomeFragment homeFragment;
//    private HotFragment hotFragment;
    private KnowledgeSystemFragment knowledgeSystemFragment;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initInjector() {
    }

    @Override
    protected void initView() {
        mNavigation.setNavigationItemSelectedListener(this);
        mNavigation.setItemIconTintList(null);
        mNavigation.getMenu().findItem(R.id.navigation_home).setChecked(true);
        initNavView();
        initFragment();
        switchFragment(0);

        setUserStatusInfo();
        /**登陆成功重新设置用户新*/
        RxBus.getInstance().toFlowable(LoginEvent.class).subscribe(new Consumer<LoginEvent>() {
            @Override
            public void accept(LoginEvent event) throws Exception {
                setUserStatusInfo();
            }
        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        mNavigation.getMenu().findItem(R.id.navigation_home).setChecked(false);
        mNavigation.getMenu().findItem(R.id.navigation_knowledgesystem).setChecked(false);
        switch (item.getItemId()) {
            case R.id.navigation_home:
                item.setChecked(true);
                mToolbar.setTitle(R.string.app_name);
                switchFragment(0);
                break;
            case R.id.navigation_knowledgesystem:
                item.setChecked(true);
                mToolbar.setTitle(R.string.title_knowledgesystem);
                switchFragment(1);
                break;

            case R.id.navigation_project:
                ARouter.getInstance().build("/project/ProjectActivity").navigation();
                break;
            case R.id.navigation_collection:
                if (mIsLogin) ARouter.getInstance().build("/my/MyCollectionActivity").navigation();
                else ToastUtils.showShort(R.string.not_login);
                break;
            case R.id.navigation_bookmark:
                if (mIsLogin) ARouter.getInstance().build("/my/MyBookmarkActivity").navigation();
                else ToastUtils.showShort(R.string.not_login);
                break;
            case R.id.navigation_settings:
                ARouter.getInstance().build("/setting/SettingActivity").navigation();
                break;
        }
        mDrawerLayout.closeDrawers();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected boolean showHomeAsUp() {
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menuSearch) {
            ARouter.getInstance().build("/hotsearch/SearchActivity").navigation();
        } else if (item.getItemId() == android.R.id.home) {
            mDrawerLayout.openDrawer(GravityCompat.START);
        }
        return true;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
                mDrawerLayout.closeDrawers();
            } else if (mCurPosition != 0) {
                mToolbar.setTitle(R.string.app_name);
                switchFragment(0);
            } else if ((System.currentTimeMillis() - mExitTime) > 2000) {
                ToastUtils.showShort(R.string.exit_system);
                mExitTime = System.currentTimeMillis();
            } else {
                this.finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 初始化fragment
     */
    private void initFragment() {
        mFragments = new ArrayList<>();
        mFragments.add(HomeFragment.newInstance());
        mFragments.add(KnowledgeSystemFragment.newInstance());
//        mFragments.add(HotFragment.newInstance());
    }

    /**
     * 切换fragment
     *
     * @param position 要显示的fragment的下标
     */
    private void switchFragment(int position) {
        if (position >= mFragments.size()) {
            return;
        }
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment targetFg = mFragments.get(position);
        for (int i = 0; i < mFragments.size(); i++) {
            ft.hide(mFragments.get(i));
        }
        if (!targetFg.isAdded())
            ft.add(R.id.layout_fragment, targetFg);
        ft.show(targetFg);
        mCurPosition = position;
        ft.commitAllowingStateLoss();
    }

    @Override
    public void onAttachFragment(Fragment fragment) {
        Log.e("TAG", fragment.getClass().getName() + "--");
        if (homeFragment == null && fragment instanceof HomeFragment)
            homeFragment = (HomeFragment) fragment;
//        if (hotFragment == null && fragment instanceof HotFragment)
//            hotFragment = (HotFragment) fragment;
        if (knowledgeSystemFragment == null && fragment instanceof KnowledgeSystemFragment)
            knowledgeSystemFragment = (KnowledgeSystemFragment) fragment;
    }

    private void initNavView() {
        mCivAvatar = mNavigation.getHeaderView(0).findViewById(R.id.civAvatar);
        mTvNick = mNavigation.getHeaderView(0).findViewById(R.id.tvNick);
        mLlLogout = mNavigation.getHeaderView(0).findViewById(R.id.llLogout);
        mNavigation.getHeaderView(0).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mIsLogin) LoginActivity.start();
            }
        });
        mLlLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });
    }

    /**
     * 退出登陆
     */
    private void logout() {
        /**设置退出登陆*/
        SPUtils.getInstance(Constant.SHARED_NAME).clear();
        setUserStatusInfo();
        /**清除cookies*/
        CookiesManager.clearAllCookies();
        /**发送退出登陆的消息*/
        RxBus.getInstance().post(new LoginEvent());
    }

    /**
     * 设置用户状态信息
     */
    private void setUserStatusInfo() {
        mIsLogin = SPUtils.getInstance(Constant.SHARED_NAME).getBoolean(Constant.LOGIN_KEY);
        if (mIsLogin) {
            mCivAvatar.setImageResource(R.drawable.ic_head_portrait);
            mTvNick.setText(SPUtils.getInstance(Constant.SHARED_NAME).getString(Constant.USERNAME_KEY));
            mLlLogout.setVisibility(View.VISIBLE);
        } else {
            mCivAvatar.setImageResource(R.drawable.ic_avatar);
            mTvNick.setText(R.string.click_avatar_login);
            mLlLogout.setVisibility(View.GONE);
        }
    }
}
