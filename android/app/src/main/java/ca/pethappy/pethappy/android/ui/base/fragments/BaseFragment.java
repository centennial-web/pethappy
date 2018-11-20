package ca.pethappy.pethappy.android.ui.base.fragments;

import android.support.v4.app.Fragment;

import ca.pethappy.pethappy.android.App;

public class BaseFragment extends Fragment {
    private App app;

    protected App getApp() {
        if (app == null) {
            if (getActivity() != null) {
                app = (App) getActivity().getApplication();
                return app;
            }
        }
        return app;
    }
}
