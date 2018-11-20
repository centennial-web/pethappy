package ca.pethappy.pethappy.android.ui.base;

import android.support.v7.app.AppCompatActivity;

import ca.pethappy.pethappy.android.App;

public abstract class BaseActivity extends AppCompatActivity {
    private App app;

    protected App getApp() {
        if (app == null) {
            app = (App) getApplication();
        }
        return app;
    }
}
