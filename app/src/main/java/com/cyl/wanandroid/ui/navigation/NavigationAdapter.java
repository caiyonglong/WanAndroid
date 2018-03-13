package com.cyl.wanandroid.ui.navigation;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.cyl.wanandroid.R;
import com.cyl.wanandroid.bean.Navigation;

import javax.inject.Inject;

/**
 * Created by lw on 2018/1/22.
 */

public class NavigationAdapter extends BaseQuickAdapter<Navigation, BaseViewHolder> {
    @Inject
    public NavigationAdapter() {
        super(R.layout.item_knowledge_system, null);
    }

    @Override
    protected void convert(BaseViewHolder helper, Navigation item) {
        helper.setText(R.id.typeItemFirst, item.getName());
        StringBuffer sb = new StringBuffer();
        for (Navigation.ArticlesBean articlesBean : item.getArticles()) {
            sb.append(articlesBean.getTitle() + "     ");
        }
        helper.setText(R.id.typeItemSecond, sb.toString());
    }
}
