package ca.pethappy.server.security.models;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

public class TokenUserDetails extends User {
    private String token;
    private String fullName;
    private String email;

    public TokenUserDetails(String username, String fullName, String email, String password, String token,
                            boolean active, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, active, true, true, true, authorities);
        this.fullName = fullName;
        this.token = token;
        this.email = email;
    }

    public String getToken() {
        return token;
    }

    public String getFullName() {
        return fullName;
    }

    public String getEmail() {
        return email;
    }
}
