package adammistal.sourcebrowser.ui.base;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;


public abstract class BaseFragment extends Fragment {

    protected void navigate(int rootID, Fragment newFragment, boolean addToBackstack) {
        navigate(getActivity().getSupportFragmentManager(), rootID, newFragment, addToBackstack);
    }

    protected void navigateChildFragment(int rootID, Fragment newFragment, boolean addToBackstack) {
        navigate(getChildFragmentManager(), rootID, newFragment, addToBackstack);
    }

    protected void navigate(FragmentManager fm, int rootID, Fragment newFragment, boolean addToBackstack) {
        FragmentTransaction ft = fm
                .beginTransaction();
        ft.replace(rootID, newFragment);
        if (addToBackstack) {
            ft.addToBackStack(null);
        }
        ft.commit();
    }

    public void clearBackStack(FragmentManager manager) {
        while (manager.getBackStackEntryCount() > 0) {
            manager.popBackStackImmediate();
        }
        manager.executePendingTransactions();
    }

    public boolean backStackIsEmpty(FragmentManager supportFragmentManager) {
        return supportFragmentManager.getBackStackEntryCount() == 0;
    }

    protected void navigateBack() {
        getActivity().onBackPressed();
    }

    protected void toast(String message) {
        if (!TextUtils.isEmpty(message)) {
            Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
        }
    }

    public  void hideKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager)
                getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            if (view != null) {
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            } else if (getActivity().getCurrentFocus() != null) {
                imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
                getActivity().getCurrentFocus().clearFocus();
            }
        }
    }

    public  void showKeyboard(View view) {
        if (view != null) {
            view.requestFocus();
        }
        InputMethodManager imm = (InputMethodManager)
                getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
        }
    }
}
