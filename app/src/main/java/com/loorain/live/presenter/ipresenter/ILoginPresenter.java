package com.loorain.live.presenter.ipresenter;

import com.loorain.live.base.BasePresenter;
import com.loorain.live.base.BaseView;

/**
 * @author loorain
 * @time 2017/5/20  上午10:24
 * @desc ${TODD}
 */


public abstract class ILoginPresenter implements BasePresenter {

    public abstract boolean checkNormalLogin(String username, String password);

    public abstract void normalLogin(String username, String password, int loginType);

    public abstract boolean checkPhoneLogin(String phone, String password);

    public abstract void phoneLogin(String phone, String password);

    public interface ILoginView extends BaseView{

        void normalLoginSuccess();
        void normalLoginFailed(String errorMsg);

        void phoneLoginSuccess();
        void pnoneLoginFailed(String errorMsg);

        void usernaeError(String errorMsg);
        void phoneError(String errorMsg);
        void passwordError(String errorMsg);
        void verifyCodeError(String errorMsg);
    }

}
