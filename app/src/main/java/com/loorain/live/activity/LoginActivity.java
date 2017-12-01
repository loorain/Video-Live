package com.loorain.live.activity;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.loorain.live.R;
import com.loorain.live.common.http.request.LoginRequest;
import com.loorain.live.presenter.LoginPresenter;
import com.loorain.live.presenter.ipresenter.ILoginPresenter;

/**
 * @author loorain
 * @time 2017/5/17  下午10:08
 * @desc ${TODD}
 */


public class LoginActivity extends BaseActivity implements View.OnClickListener,
        ILoginPresenter.ILoginView {


    private Button mBtnLogin, mBtnPhoneLogin, mBtnPhoneResgist;

    private EditText mEtUsername, mEtPassword;
    private LoginPresenter mPresenter;


    public static void invoke(Context context){
        Intent intent = new Intent(context,LoginActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    protected void initView() {
        mBtnLogin = obtainView(R.id.btn_login);
        mBtnPhoneLogin = obtainView(R.id.btn_phone_login);
        mBtnPhoneResgist = obtainView(R.id.btn_phone_resgist);

        mEtUsername = obtainView(R.id.et_username);
        mEtPassword = obtainView(R.id.et_password);
        mEtUsername.setText("18960865775");
        mEtPassword.setText("mima7412369");//41B5CA2D76007694008582B2BE3E2B7B71ADDBB98D41795C11729F

    }

    @Override
    protected void initListener() {
        mBtnLogin.setOnClickListener(this);
        mBtnPhoneLogin.setOnClickListener(this);
        mBtnPhoneResgist.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        mPresenter = new LoginPresenter(this);
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login :
                mPresenter.normalLogin(mEtUsername.getText().toString(),
                        mEtPassword.getText().toString(), LoginRequest.CNIAO_LOGIN);
                break;
            case R.id.btn_phone_login:
                mPresenter.phoneLogin(mEtUsername.getText().toString(),
                        mEtPassword.getText().toString());
                break;
            case R.id.btn_phone_resgist:
                RegistActivity.invoke(this);
                break;
            default:
        }
    }

    @Override
    public void showLoading() {
        showDialog();
    }

    @Override
    public void dismissLoading() {
        dismissDialog();
    }

    @Override
    public void showMsg(String msg) {
        showToast(msg);
    }

    @Override
    public void showMsg(int resId) {
        showToast(resId);
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void normalLoginSuccess() {
        MainActivity.invoke(this);
    }

    @Override
    public void normalLoginFailed(String errorMsg) {
        showToast(errorMsg);
    }

    @Override
    public void phoneLoginSuccess() {

    }

    @Override
    public void pnoneLoginFailed(String errorMsg) {
        showToast(errorMsg);
    }

    @Override
    public void usernaeError(String errorMsg) {
        showToast(errorMsg);
    }

    @Override
    public void phoneError(String errorMsg) {
        showToast(errorMsg);
    }

    @Override
    public void passwordError(String errorMsg) {
        showToast(errorMsg);
    }

    @Override
    public void verifyCodeError(String errorMsg) {
        showToast(errorMsg);
    }
}
