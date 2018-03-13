package com.cyl.wanandroid.ui.setting;

import android.content.pm.PackageManager;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.cyl.wanandroid.R;
import com.cyl.wanandroid.base.App;
import com.cyl.wanandroid.base.BaseActivity;
import com.cyl.wanandroid.ui.article.ArticleContentActivity;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by lw on 2018/2/12.
 */
@Route(path = "/setting/AboutActivity")
public class AboutActivity extends BaseActivity {
    private static final String TAG = "AboutActivity";
    @BindView(R.id.version)
    TextView mVersion;

    @OnClick(R.id.tv_project)
    void show() {
        String url = "https://github.com/caiyonglong/WanAndroid";
        ArticleContentActivity.start(0, url, "项目地址", "cyl");
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_about;
    }

    @Override
    protected void initInjector() {

    }

    @Override
    protected void initView() {
        String version = "";
        try {
            version = App.getAppContext()
                    .getPackageManager()
                    .getPackageInfo(App.getAppContext().getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        mVersion.setText(version);
    }

    @Override
    protected boolean showHomeAsUp() {
        return true;
    }
}
