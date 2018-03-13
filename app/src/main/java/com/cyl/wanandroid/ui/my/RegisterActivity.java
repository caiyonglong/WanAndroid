package com.cyl.wanandroid.ui.my;

import android.annotation.TargetApi;
import android.os.Build;
import android.support.design.widget.TextInputEditText;
import android.transition.Transition;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.cyl.wanandroid.R;
import com.cyl.wanandroid.base.BaseActivity;
import com.cyl.wanandroid.bean.User;
import com.cyl.wanandroid.constant.Constant;
import com.cyl.wanandroid.event.LoginEvent;
import com.cyl.wanandroid.utils.RxBus;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by lw on 2018/1/24.
 */
@Route(path = "/my/RegisterActivity")
public class RegisterActivity extends BaseActivity<RegisterPresenter> implements RegisterContract.View {
    @BindView(R.id.etUsername)
    TextInputEditText mEtUsername;
    @BindView(R.id.etPassword)
    TextInputEditText mEtPassword;
    @BindView(R.id.etRePassword)
    TextInputEditText mEtRePassword;

    @OnClick(R.id.fab)
    void backLogin() {
        onBackPressed();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_register;
    }

    @Override
    protected void initInjector() {
        mActivityComponent.inject(this);
    }

    @Override
    protected void initView() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().getEnterTransition().addListener(new EnterTransitionListener());
        }
        mEtUsername.setText(SPUtils.getInstance(Constant.SHARED_NAME).getString(Constant.USERNAME_KEY));
        mEtPassword.setText(SPUtils.getInstance(Constant.SHARED_NAME).getString(Constant.PASSWORD_KEY));
        mEtRePassword.setText(SPUtils.getInstance(Constant.SHARED_NAME).getString(Constant.PASSWORD_KEY));
    }

    @Override
    protected boolean showHomeAsUp() {
        return true;
    }

    @OnClick(R.id.btnRegister)
    public void login() {
        String username = mEtUsername.getText().toString();
        String password = mEtPassword.getText().toString();
        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
            ToastUtils.showShort(R.string.the_username_or_password_can_not_be_empty);
            return;
        }
        mPresenter.register(username, password, password);
    }

    public static void start() {
        ARouter.getInstance().build("/my/RegisterActivity").navigation();
    }

    @Override
    public void registerSuccess(User user) {
        SPUtils.getInstance(Constant.SHARED_NAME).put(Constant.LOGIN_KEY, true);
        SPUtils.getInstance(Constant.SHARED_NAME).put(Constant.USERNAME_KEY, user.getUsername());
        SPUtils.getInstance(Constant.SHARED_NAME).put(Constant.PASSWORD_KEY, user.getPassword());
        /**登陆成功通知其他界面刷新*/
        RxBus.getInstance().post(new LoginEvent());
        LoginActivity.start();
        this.finish();
    }


    @TargetApi(Build.VERSION_CODES.KITKAT)
    private class EnterTransitionListener implements Transition.TransitionListener {
        @Override
        public void onTransitionStart(Transition transition) {

        }

        @Override
        public void onTransitionEnd(Transition transition) {
        }

        @Override
        public void onTransitionCancel(Transition transition) {

        }

        @Override
        public void onTransitionPause(Transition transition) {

        }

        @Override
        public void onTransitionResume(Transition transition) {

        }
    }
}
