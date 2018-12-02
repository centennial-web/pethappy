package ca.pethappy.pethappy.android.ui.profile;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import ca.pethappy.pethappy.android.App;
import ca.pethappy.pethappy.android.R;
import ca.pethappy.pethappy.android.models.User;
import ca.pethappy.pethappy.android.models.backend.Card;
import ca.pethappy.pethappy.android.ui.base.fragments.BaseFragment;
import ca.pethappy.pethappy.android.utils.task.SimpleTask;

public class ProfileFragment extends BaseFragment {
    public ProfileFragment() {
        // Required empty public constructor
    }

    // Components
    private TextView fullNameTxt;
    private TextView emailTxt;
    private TextView cellPhoneTxt;

    private TextView addressTxt;
    private TextView addressComplementTxt;
    private TextView buzzerTxt;

    private TextView cardNumberTxt;
    private TextView expiresTxt;
    private TextView nameOnCardTxt;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);

        // Components references
        fullNameTxt = rootView.findViewById(R.id.fullNameTxt);
        emailTxt = rootView.findViewById(R.id.emailTxt);
        cellPhoneTxt = rootView.findViewById(R.id.cellPhoneTxt);

        addressTxt = rootView.findViewById(R.id.addressTxt);
        addressComplementTxt = rootView.findViewById(R.id.addressComplementTxt);
        buzzerTxt = rootView.findViewById(R.id.buzzerTxt);

        cardNumberTxt = rootView.findViewById(R.id.cardNumberTxt);
        expiresTxt = rootView.findViewById(R.id.expiresTxt);
        nameOnCardTxt = rootView.findViewById(R.id.nameOnCardTxt);

        App app = getApp();
        new SimpleTask<Void, UserProfile>(
                ignored -> {
                    User user = app.userServices.getUser();
                    if (user != null) {
                        List<Card> cards = app.userServices.getUserCards();
                        return new UserProfile(user, cards);
                    }
                    return null;
                },
                userProfile -> {
                    if (userProfile != null) {
                        // Person
                        fullNameTxt.setText(userProfile.user.firstName + " " + userProfile.user.lastName);
                        emailTxt.setText(userProfile.user.email);
                        cellPhoneTxt.setText(userProfile.user.cellPhone);
                        // Address
                        addressTxt.setText((TextUtils.isEmpty(userProfile.user.unit) ? "" : userProfile.user.unit + "-") + userProfile.user.address);
                        addressComplementTxt.setText(userProfile.user.city + " " + userProfile.user.province + " " + userProfile.user.postalCode);
                        if (TextUtils.isEmpty(userProfile.user.buzzer)) {
                            buzzerTxt.setVisibility(View.GONE);
                        } else {
                            buzzerTxt.setVisibility(View.VISIBLE);
                            buzzerTxt.setText("Buzzer " + userProfile.user.buzzer);
                        }
                        // Card
                        // TODO: 02/12/18 create a list to show all the cards
                        if (userProfile.cards.size() > 0) {
                            Card card = userProfile.cards.get(0);
                            cardNumberTxt.setText(card.number);
                            expiresTxt.setText("Expires in " + card.expMonth + "/" + card.expYear);
                            nameOnCardTxt.setText(card.nameOnCard);
                        }
                    } else {
                        showEmpty();
                    }
                },
                error -> {
                    showEmpty();
                    Toast.makeText(getContext(), "Error loading user profile. Details: " + error.getMessage(),
                            Toast.LENGTH_SHORT).show();
                }
        ).execute((Void) null);

        return rootView;
    }

    private void showEmpty() {
        // Person
        fullNameTxt.setText("");
        emailTxt.setText("");
        cellPhoneTxt.setText("");
        // Address
        addressTxt.setText("");
        addressComplementTxt.setText("");
        buzzerTxt.setVisibility(View.GONE);
        // Card
        cardNumberTxt.setText("");
        expiresTxt.setText("");
        nameOnCardTxt.setText("");
    }

    static class UserProfile {
        User user;
        List<Card> cards;

        UserProfile(User user, List<Card> cards) {
            this.user = user;
            this.cards = cards;

            if (this.cards == null) {
                this.cards = new ArrayList<>();
            }
        }
    }
}
