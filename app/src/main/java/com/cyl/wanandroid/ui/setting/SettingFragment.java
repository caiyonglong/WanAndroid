package com.cyl.wanandroid.ui.setting;

import android.os.Bundle;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;

import com.alibaba.android.arouter.launcher.ARouter;
import com.blankj.utilcode.util.ToastUtils;
import com.cyl.wanandroid.R;
import com.cyl.wanandroid.ui.article.ArticleContentActivity;


/**
 * Created by lw on 2017-09-05.
 */

public class SettingFragment extends PreferenceFragmentCompat {
    private Preference mSettingAutoUpdate, mCheckUpdate, mAbout, mAboutMe;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.settings_preference_fragment);
        mSettingAutoUpdate = findPreference("settingAutoUpdate");
        mCheckUpdate = findPreference("checkUpdate");
        mCheckUpdate.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                ToastUtils.showShort("暂无更新");
                return false;
            }
        });
        mAbout = findPreference("about");
        mAbout.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                ARouter.getInstance().build("/setting/AboutActivity").navigation();
                return false;
            }
        });
        mAboutMe = findPreference("github");
        mAboutMe.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                String url = "https://github.com/caiyonglong";
                ArticleContentActivity.start(0, url, "Github地址", "cyl");
                return false;
            }
        });
    }

    public static SettingFragment newInstance() {
        return new SettingFragment();
    }
}
