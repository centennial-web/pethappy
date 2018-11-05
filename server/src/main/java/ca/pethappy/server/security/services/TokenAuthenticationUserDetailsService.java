package ca.pethappy.server.security.services;

import ca.pethappy.server.security.models.TokenUserDetails;
import com.auth0.jwt.exceptions.InvalidClaimException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
public class TokenAuthenticationUserDetailsService
        implements AuthenticationUserDetailsService<PreAuthenticatedAuthenticationToken> {
    private final TokenService tokenService;

    @Autowired
    public TokenAuthenticationUserDetailsService(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @Override
    public UserDetails loadUserDetails(PreAuthenticatedAuthenticationToken authentication) throws UsernameNotFoundException {
        if (authentication.getPrincipal() != null && authentication.getPrincipal() instanceof String && authentication.getCredentials() instanceof String) {
            DecodedJWT token;
            try {
                token = tokenService.decode((String) authentication.getPrincipal());
                if (token == null) {
                    throw new InvalidClaimException("Token is invalid");
                }
            } catch (InvalidClaimException ex) {
                throw new UsernameNotFoundException("Error validating token", ex);
            }
            return new TokenUserDetails(
                    token.getSubject(),                         // email
                    token.getClaim("fullName").asString(),      // fullName = firstname + lastname
                    token.getClaim("email").asString(),         // email
                    (String) authentication.getCredentials(),   // password
                    token.getToken(),                           // token
                    true,                                       // active
                    token.getClaim("roles")                     // roles
                            .asList(String.class)
                            .stream()
                            .map(SimpleGrantedAuthority::new)
                            .collect(Collectors.toList())
            );
        } else {
            throw new UsernameNotFoundException("Nao foi possivel pegar o dados do usuario '" + authentication.getPrincipal() + "'");
        }
    }
}
