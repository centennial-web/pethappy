package ca.pethappy.pethappy.android.ui.profile;

import android.os.Bundle;

import ca.pethappy.pethappy.android.R;
import ca.pethappy.pethappy.android.ui.base.BaseAuthenticatedActivity;

public class ProfileActivity extends BaseAuthenticatedActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
    }
}
