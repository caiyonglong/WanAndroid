package com.cyl.wanandroid.ui.project;

import com.cyl.wanandroid.base.BasePresenter;
import com.cyl.wanandroid.bean.DataResponse;
import com.cyl.wanandroid.bean.KnowledgeSystem;
import com.cyl.wanandroid.net.ApiService;
import com.cyl.wanandroid.net.RetrofitManager;
import com.cyl.wanandroid.utils.RxSchedulers;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.functions.Consumer;

/**
 * Created by lw on 2018/1/19.
 */

public class ProjectPresenter extends BasePresenter<ProjectContract.View> implements ProjectContract.Presenter {
    @Inject
    public ProjectPresenter() {

    }

    @Override
    public void loadProjects() {
        mView.showLoading();
        RetrofitManager.create(ApiService.class)
                .getProjects()
                .compose(RxSchedulers.<DataResponse<List<KnowledgeSystem.ChildrenBean>>>applySchedulers())
                .compose(mView.<DataResponse<List<KnowledgeSystem.ChildrenBean>>>bindToLife())
                .subscribe(new Consumer<DataResponse<List<KnowledgeSystem.ChildrenBean>>>() {
                    @Override
                    public void accept(DataResponse<List<KnowledgeSystem.ChildrenBean>> dataResponse) throws Exception {
                        mView.setProjects(dataResponse.getData());
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
        loadProjects();
    }
}
