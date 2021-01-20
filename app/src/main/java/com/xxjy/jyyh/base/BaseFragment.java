package com.xxjy.jyyh.base;

import androidx.fragment.app.Fragment;

/**
 * @author power
 * @date 12/1/20 1:25 PM
 * @project RunElephant
 * @description:
 */
public abstract class BaseFragment extends Fragment {

    protected BaseActivity getBaseActivity() {
        if (getActivity() instanceof BaseActivity) {
            return (BaseActivity) getActivity();
        }
        return null;
    }

    public void showToast(String str) {
        if (getBaseActivity() == null) return;
        getBaseActivity().showToast(str);
    }

    public void showToastInfo(String str) {
        if (getBaseActivity() == null) return;
        getBaseActivity().showToastInfo(str);
    }

    public void showToastWarning(String str) {
        if (getBaseActivity() == null) return;
        getBaseActivity().showToastWarning(str);
    }

    public void showToastError(String str) {
        if (getBaseActivity() == null) return;
        getBaseActivity().showToastError(str);
    }

    public void showToastSuccess(String str) {
        if (getBaseActivity() == null) return;
        getBaseActivity().showToastSuccess(str);
    }
}
