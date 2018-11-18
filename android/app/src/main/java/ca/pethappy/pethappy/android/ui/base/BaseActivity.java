package ca.pethappy.pethappy.android.ui.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import ca.pethappy.pethappy.android.App;

public abstract class BaseActivity extends AppCompatActivity {
    protected App app;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        app = (App) getApplication();
    }
}
