package com.cyl.wanandroid.ui.my;

import com.cyl.wanandroid.base.BaseContract;

/**
 * Created by lw on 2018/1/19.
 */

public interface MyContract {
    interface View extends BaseContract.BaseView {
    }

    interface Presenter extends BaseContract.BasePresenter<MyContract.View> {
    }
}
