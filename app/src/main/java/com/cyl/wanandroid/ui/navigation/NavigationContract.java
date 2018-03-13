package com.cyl.wanandroid.ui.navigation;

import com.cyl.wanandroid.base.BaseContract;
import com.cyl.wanandroid.bean.Navigation;

import java.util.List;

/**
 * Created by lw on 2018/1/19.
 */

public interface NavigationContract {
    interface View extends BaseContract.BaseView {
        void setNavigations(List<Navigation> navigations);
    }

    interface Presenter extends BaseContract.BasePresenter<NavigationContract.View> {
        void loadNavigations();

        void refresh();
    }
}
