package com.cyl.wanandroid.ui.my;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.support.v7.widget.CardView;
import android.util.Pair;
import android.view.View;
import android.widget.Button;

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
import io.reactivex.functions.Consumer;

/**
 * Created by lw on 2018/1/24.
 */
@Route(path = "/my/LoginActivity")
public class LoginActivity extends BaseActivity<LoginPresenter> implements LoginContract.View {
    @BindView(R.id.etUsername)
    TextInputEditText mEtUsername;
    @BindView(R.id.etPassword)
    TextInputEditText mEtPassword;

    @BindView(R.id.fabLogin)
    FloatingActionButton fab;
    @BindView(R.id.btnRegister)
    Button mBtnRegister;
    @BindView(R.id.cardview)
    CardView mCardView;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    protected void initInjector() {
        mActivityComponent.inject(this);
    }

    @Override
    protected void initView() {
        mEtUsername.setText(SPUtils.getInstance(Constant.SHARED_NAME).getString(Constant.USERNAME_KEY));
        mEtPassword.setText(SPUtils.getInstance(Constant.SHARED_NAME).getString(Constant.PASSWORD_KEY));

        /**注册成功销毁Login*/
        RxBus.getInstance().toFlowable(LoginEvent.class).subscribe(new Consumer<LoginEvent>() {
            @Override
            public void accept(LoginEvent event) throws Exception {
                finish();
            }
        });
    }

    @Override
    protected boolean showHomeAsUp() {
        return true;
    }

    @OnClick(R.id.fabLogin)
    public void login() {
        String username = mEtUsername.getText().toString();
        String password = mEtPassword.getText().toString();
        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
            ToastUtils.showShort(R.string.the_username_or_password_can_not_be_empty);
            return;
        }
        mPresenter.login(username, password);
    }

    @OnClick(R.id.btnRegister)
    public void register() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            final Intent intent = new Intent(this, RegisterActivity.class);
            startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(
                    this,
                    Pair.create((View) mBtnRegister, "transition_next"),
                    Pair.create((View) fab, "transition_fab"),
                    Pair.create((View) mCardView, "transition_cardView")).toBundle());
        } else {
            RegisterActivity.start();
        }
    }

    @Override
    public void loginSuccess(User user) {
        SPUtils.getInstance(Constant.SHARED_NAME).put(Constant.LOGIN_KEY, true);
        SPUtils.getInstance(Constant.SHARED_NAME).put(Constant.USERNAME_KEY, user.getUsername());
        SPUtils.getInstance(Constant.SHARED_NAME).put(Constant.PASSWORD_KEY, user.getPassword());
        /**登陆成功通知其他界面刷新*/
        RxBus.getInstance().post(new LoginEvent());
        this.finish();
    }

    public static void start() {
        ARouter.getInstance().build("/my/LoginActivity").navigation();
    }

}
