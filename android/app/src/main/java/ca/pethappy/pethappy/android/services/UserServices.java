package ca.pethappy.pethappy.android.services;

import java.io.IOException;
import java.util.List;

import ca.pethappy.pethappy.android.App;
import ca.pethappy.pethappy.android.models.User;
import ca.pethappy.pethappy.android.models.backend.Card;
import ca.pethappy.pethappy.android.models.forms.UserSettings;
import retrofit2.Response;

public class UserServices {
    private final App app;

    public UserServices(App app) {
        this.app = app;
    }

    public UserSettings userSettings() throws IOException {
        Response<UserSettings> response = app.endpoints.userSettings(app.getUserInfo().id).execute();
        UserSettings body;
        if (response.isSuccessful() && (body = response.body()) != null) {
            return body;
        }
        throw new IOException("Couldn't find user");
    }

    public UserSettings userSettings(Long userId) throws IOException {
        Response<UserSettings> response = app.endpoints.userSettings(userId).execute();
        UserSettings body;
        if (response.isSuccessful() && (body = response.body()) != null) {
            return body;
        }
        throw new IOException("Couldn't find user");
    }

    public Boolean updateSettings(UserSettings userSettings) throws IOException {
        Response<Boolean> response = app.endpoints.updateSettings(userSettings).execute();
        Boolean body;
        if (response.isSuccessful() && (body = response.body()) != null) {
            return body;
        }
        throw new IOException("Couldn't update settings");
    }

    public User getUser() throws IOException {
        Response<User> response = app.endpoints.getUser(app.getUserInfo().id).execute();
        User body;
        if (response.isSuccessful() && (body = response.body()) != null) {
            return body;
        }
        throw new IOException("Couldn't find user");
    }

    public List<Card> getUserCards() throws IOException {
        Response<List<Card>> response = app.endpoints.getUserCards(app.getUserInfo().id).execute();
        List<Card> body;
        if (response.isSuccessful() && (body = response.body()) != null) {
            return body;
        }
        throw new IOException("Couldn't find user");
    }
}
