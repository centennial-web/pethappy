package ca.pethappy.pethappy.android.ui.settings;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.Toast;

import ca.pethappy.pethappy.android.App;
import ca.pethappy.pethappy.android.R;
import ca.pethappy.pethappy.android.models.forms.UserSettings;
import ca.pethappy.pethappy.android.ui.base.fragments.BaseFragment;
import ca.pethappy.pethappy.android.ui.base.fragments.OnFragmentInteractionListener;
import ca.pethappy.pethappy.android.utils.task.SimpleTask;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SettingsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SettingsFragment extends BaseFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    // Components
    private Switch use2faSwitch;

    public SettingsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SettingsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SettingsFragment newInstance(String param1, String param2) {
        SettingsFragment fragment = new SettingsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_settings, container, false);

        // Components
        use2faSwitch = rootView.findViewById(R.id.use2faSwitch);
        use2faSwitch.setOnClickListener(v -> saveSettings());

        // Load settings and update user interface
        loadUserSettings();

        return rootView;
    }

    private void loadUserSettings() {
        // Get App for the tasks (you shoudn't get inside the tasks)
        App app = getApp();

        new SimpleTask<Void, UserSettings>(
                ignored -> app.userServices.userSettings(),
                userSettings -> {
                    // Update components
                    use2faSwitch.setChecked(userSettings.use2fa);
                },
                error -> Toast.makeText(getContext(), "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show()
        ).execute((Void) null);
    }

    private void saveSettings() {
        // Get App for the tasks (you shoudn't get inside the tasks)
        App app = getApp();

        // Create user setting object
        UserSettings userSettings = new UserSettings();
        userSettings.id = app.getUserInfo().id;
        userSettings.use2fa = use2faSwitch.isChecked();

        // Save
        new SimpleTask<Void, Boolean>(
                ignored -> app.userServices.updateSettings(userSettings),
                ignored -> {},
                error -> Toast.makeText(getContext(), "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show()
        ).execute((Void) null);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
}
