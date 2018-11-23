package ca.pethappy.pethappy.android.models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DecodedToken {
    public Long id;
    public String firstName;
    public String lastName;
    public List<String> roles;
    public String iss;
    public String email;
    public Date exp;
    public Date iat;

    public DecodedToken() {
        this.roles = new ArrayList<>();
    }
}
