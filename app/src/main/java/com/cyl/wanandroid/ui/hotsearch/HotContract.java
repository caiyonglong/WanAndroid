package com.cyl.wanandroid.ui.hotsearch;

import com.cyl.wanandroid.base.BaseContract;
import com.cyl.wanandroid.bean.Friend;
import com.cyl.wanandroid.bean.HotKey;

import java.util.List;

/**
 * Created by lw on 2018/1/23.
 */

public interface HotContract {
    interface View extends BaseContract.BaseView {
        void setHotData(List<HotKey> hotKeys, List<Friend> friends);
    }

    interface Presenter extends BaseContract.BasePresenter<View> {
        void loadHotData();

        void refresh();
    }
}
