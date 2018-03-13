package com.cyl.wanandroid.ui.navigation;

import com.cyl.wanandroid.base.BasePresenter;
import com.cyl.wanandroid.bean.DataResponse;
import com.cyl.wanandroid.bean.Navigation;
import com.cyl.wanandroid.net.ApiService;
import com.cyl.wanandroid.net.RetrofitManager;
import com.cyl.wanandroid.utils.RxSchedulers;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.functions.Consumer;

/**
 * Created by lw on 2018/1/19.
 */

public class NavigationPresenter extends BasePresenter<NavigationContract.View> implements NavigationContract.Presenter {
    @Inject
    public NavigationPresenter() {

    }

    @Override
    public void loadNavigations() {
        mView.showLoading();
        RetrofitManager.create(ApiService.class)
                .getNavigations()
                .compose(RxSchedulers.<DataResponse<List<Navigation>>>applySchedulers())
                .compose(mView.<DataResponse<List<Navigation>>>bindToLife())
                .subscribe(new Consumer<DataResponse<List<Navigation>>>() {
                    @Override
                    public void accept(DataResponse<List<Navigation>> dataResponse) throws Exception {
                        mView.setNavigations(dataResponse.getData());
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        mView.showFaild(throwable.getMessage());
                    }
                });
    }

    @Override
    public void refresh() {
        loadNavigations();
    }
}
