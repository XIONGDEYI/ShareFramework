package com.ray.balloon.presenter;

import com.corelibs.api.ManagerFactory;
import com.corelibs.api.ResponseTransformer;
import com.corelibs.base.BaseRxPresenter;
import com.corelibs.utils.Tools;
import com.ray.balloon.R;
import com.ray.balloon.model.bean.BaseData;
import com.ray.balloon.model.manager.RegitserManager;
import com.ray.balloon.subcriber.ResponseSubscriber;
import com.ray.balloon.view.login.RegisterView;

/**
 * Created by Administrator on 2016/3/1.
 */
public class RegisterPresenter extends BaseRxPresenter<RegisterView> {
    private RegitserManager manager;

    @Override
    protected void onViewAttached() {
        manager = ManagerFactory.getFactory().getManager(RegitserManager.class);
    }

    public void registerSuccess(final String username, final String password) {
        if (!isUserInputValidate(username, password)) return;

        getView().showLoadingDialog();
        manager.register(username, password).
                compose(new ResponseTransformer<>(this.<BaseData>bindLifeCycle())).
                subscribe(new ResponseSubscriber<BaseData>(getView()) {

                    @Override
                    public void success(BaseData baseData) {
                        getView().registerSuccess();
                    }

                    @Override
                    public void operationError(BaseData baseData, int status, String message) {
                        getView().showToastMessage(message);
                    }
                });
    }

    private boolean isUserInputValidate(String username, String password) {
        if (!Tools.validatePhone(username)) {
            getView().showToastMessage(getString(R.string.login_username_no_invalidate));
            return false;
        }

        if (stringIsNull(password) || password.length() < 6 || password.length() > 16) {
            getView().showToastMessage(getString(R.string.login_password_invalid));
            return false;
        }

        return true;
    }
}
