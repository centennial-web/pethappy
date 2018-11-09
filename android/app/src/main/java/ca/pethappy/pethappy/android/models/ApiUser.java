package ca.pethappy.pethappy.android.models;

import java.util.List;
import java.util.Map;

public class ApiUser {
    public String username;
    public String token;
    public String fullName;
    public String email;
    public List<Map<String, String>> authorities;

    public ApiUser() {
    }
}
