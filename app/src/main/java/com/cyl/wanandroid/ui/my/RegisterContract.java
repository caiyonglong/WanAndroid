package com.cyl.wanandroid.ui.my;

import com.cyl.wanandroid.base.BaseContract;
import com.cyl.wanandroid.bean.User;

/**
 * Created by lw on 2018/1/24.
 */

public interface RegisterContract {
    interface View extends BaseContract.BaseView {
        void registerSuccess(User user);
    }

    interface Presenter extends BaseContract.BasePresenter<RegisterContract.View> {
        void register(String username, String password, String repassword);
    }
}
