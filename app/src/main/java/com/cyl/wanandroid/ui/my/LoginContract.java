package com.cyl.wanandroid.ui.my;

import com.cyl.wanandroid.base.BaseContract;
import com.cyl.wanandroid.bean.User;

/**
 * Created by lw on 2018/1/24.
 */

public interface LoginContract {
    interface View extends BaseContract.BaseView {
        void loginSuccess(User user);
    }

    interface Presenter extends BaseContract.BasePresenter<LoginContract.View> {
        void login(String username, String password);
    }
}
