package ca.pethappy.server.security.services;

import ca.pethappy.server.models.Role;
import ca.pethappy.server.models.User;
import ca.pethappy.server.security.models.TokenUserDetails;
import ca.pethappy.server.services.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class JpaDetailsService implements UserDetailsService {
    private final UsersService usersService;
    private final TokenService tokenService;

    @Autowired
    public JpaDetailsService(UsersService usersService, TokenService tokenService) {
        this.usersService = usersService;
        this.tokenService = tokenService;
    }

    @Override
    public UserDetails loadUserByUsername(final String emailOrUsername) throws UsernameNotFoundException {
        // Search by email then username
        User user = usersService.findByEmailFetchRoles(emailOrUsername);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }

        // Create UserDetails. I already have a User class, so I had to use the full package name
        return getUserDetails(user);
    }

    private TokenUserDetails getUserDetails(User user) {
        return new TokenUserDetails(
                user.getEmail(),
                (user.getFirstName() + " " + user.getLastName()).trim(),
                user.getEmail(),
                user.getPassword(),
                tokenService.encode(user),
                user.isActive(),
                getUserGrantedAuthorities(user.getRoles())
        );
    }

    private List<GrantedAuthority> getUserGrantedAuthorities(Set<Role> userRoles) {
        Set<GrantedAuthority> roles = new HashSet<>();
        userRoles.forEach((role) -> roles.add(new SimpleGrantedAuthority(role.getName())));
        return new ArrayList<>(roles);
    }
}
