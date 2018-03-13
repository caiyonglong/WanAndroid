package com.cyl.wanandroid.ui.project;

import com.cyl.wanandroid.base.BaseContract;
import com.cyl.wanandroid.bean.KnowledgeSystem;

import java.util.List;

/**
 * Created by lw on 2018/1/19.
 */

public interface ProjectContract {
    interface View extends BaseContract.BaseView {
        void setProjects(List<KnowledgeSystem.ChildrenBean> projects);
    }

    interface Presenter extends BaseContract.BasePresenter<ProjectContract.View> {
        void loadProjects();

        void refresh();
    }
}
