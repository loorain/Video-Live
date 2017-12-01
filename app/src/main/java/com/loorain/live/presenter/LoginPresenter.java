package com.loorain.live.presenter;

import android.text.TextUtils;

import com.loorain.live.common.http.AsyncHttp;
import com.loorain.live.common.http.request.LoginRequest;
import com.loorain.live.common.http.request.PhoneLoginRequest;
import com.loorain.live.common.http.request.RequestComm;
import com.loorain.live.common.http.response.Response;
import com.loorain.live.common.logic.IMLogin;
import com.loorain.live.common.logic.IUserInfoMgrListener;
import com.loorain.live.common.logic.UserInfoMgr;
import com.loorain.live.common.utils.AsimpleCache.ACache;
import com.loorain.live.common.utils.AsimpleCache.CacheConstants;
import com.loorain.live.common.utils.Constants;
import com.loorain.live.common.utils.OtherUtils;
import com.loorain.live.model.UserInfo;
import com.loorain.live.model.UserInfoCache;
import com.loorain.live.presenter.ipresenter.ILoginPresenter;


/**
 * @author loorain
 * @time 2017/5/20  上午10:31
 * @desc ${TODD}
 */


public class LoginPresenter extends ILoginPresenter implements IMLogin.IMLoginListener {

    private ILoginView mILoginView;
    private final IMLogin mImLogin= IMLogin.getInstance();

    public LoginPresenter(ILoginView lginView) {
        mILoginView = lginView;
    }

    @Override
    public void start() {

    }

    @Override
    public void finish() {

    }

    @Override
    public boolean checkNormalLogin(String username, String password) {
        if(OtherUtils.isUsernameVaild(username)) {
            if(OtherUtils.isPasswordValid(password)) {
                if(OtherUtils.isNetworkAvailable(mILoginView.getContext())) {
                    return true;
                } else {
                    mILoginView.showMsg("当前无网络连接");
                }
            } else {
                mILoginView.showMsg("密码不符合规范");
            }
        } else {
            mILoginView.showMsg("用户名不符合规范");
        }
        return false;
    }

    @Override
    public void normalLogin(String username, String password, int loginType) {
        if(checkNormalLogin(username, password)) {
            final LoginRequest request = new LoginRequest(RequestComm.login, username, password, loginType);
            AsyncHttp.instance().postJson(request, new AsyncHttp.IHttpListener() {
                @Override
                public void onStart(int requestId) {
                    mILoginView.showLoading();
                }

                @Override
                public void onSuccess(int requestId, Response response) {
                    if(response.status == RequestComm.SUCCESS) {
                        UserInfo info = (UserInfo) response.data;
                        if(TextUtils.isEmpty(info.sigId)) {
                            mILoginView.normalLoginFailed(response.msg);
                            return;
                        }

                        UserInfoCache.saveCache(mILoginView.getContext(),info);
                        mImLogin.setIMLoginListener(LoginPresenter.this);
                        mImLogin.imLogin(info.userId,info.sigId);

                        ACache.get(mILoginView.getContext()).put(CacheConstants.LOGIN_USERNAME, username);
                        ACache.get(mILoginView.getContext()).put(CacheConstants.LOGIN_PASSWORD, password);
                    } else {
                        mILoginView.normalLoginFailed(response.msg);
                        mILoginView.dismissLoading();
                    }
                }

                @Override
                public void onFailure(int requestId, int httpStatus, Throwable error) {
                    mILoginView.dismissLoading();
                    mILoginView.normalLoginFailed(error.getMessage());
                }
            });
        }
    }

    @Override
    public boolean checkPhoneLogin(String phone, String verifyCode) {
        if (OtherUtils.isPhoneNumValid(phone)) {
            if (OtherUtils.isVerifyCodeValid(verifyCode)) {
                if (OtherUtils.isNetworkAvailable(mILoginView.getContext())) {
                    return true;
                } else {
                    mILoginView.showMsg("当前无网络连接");
                }
            } else {
                mILoginView.showMsg("密码过短");
            }
        } else {
            mILoginView.showMsg("用户名不符合规范");
        }
        mILoginView.dismissLoading();
        return false;
    }

    //验证码登录
    @Override
    public void phoneLogin(final String phone, String verifyCode) {
        if (checkPhoneLogin(phone, verifyCode)) {
            PhoneLoginRequest request = new PhoneLoginRequest(1200, phone, verifyCode);
            AsyncHttp.instance().postJson(request, new AsyncHttp.IHttpListener() {
                @Override
                public void onStart(int requestId) {
                    mILoginView.showLoading();
                }

                @Override
                public void onSuccess(int requestId, Response response) {
                    if (response.status == RequestComm.SUCCESS) {
                        ACache.get(mILoginView.getContext()).put(CacheConstants.LOGIN_USERNAME, phone);
                        mILoginView.phoneLoginSuccess();
                    } else {
                        mILoginView.pnoneLoginFailed(response.msg);
                    }
                    mILoginView.dismissLoading();
                }

                @Override
                public void onFailure(int requestId, int httpStatus, Throwable error) {
                    mILoginView.verifyCodeError("网络异常");
                    mILoginView.dismissLoading();
                }
            });
        }
    }

    @Override
    public void onSuccess() {
        UserInfoMgr.getInstance().setUserId(ACache.get(mILoginView.getContext()).getAsString(Constants.USER_ID), new IUserInfoMgrListener() {
            @Override
            public void onQueryUserInfo(int error, String errorMsg) {

            }

            @Override
            public void onSetUserInfo(int error, String errorMsg) {
                if(0 != error) {
                    mILoginView.showMsg("设置 User 失败");
                }
            }
        });
        UserInfoMgr.getInstance().setUserInfo();
        mILoginView.showMsg("登录成功");
        mImLogin.removeIMLoginListener();
        mILoginView.dismissLoading();
        mILoginView.normalLoginSuccess();

    }

    @Override
    public void onFailure(int code, String msg) {
        mILoginView.showMsg(msg);
    }
}
