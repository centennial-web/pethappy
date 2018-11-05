package ca.pethappy.server.security.filters;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;

import javax.servlet.http.HttpServletRequest;

public class TokenAuthenticationFilter extends AbstractPreAuthenticatedProcessingFilter {
    private static final Logger logger = LoggerFactory.getLogger(TokenAuthenticationFilter.class);
    private static final String HEAD_BEARER = "X-Token";

    @Override
    protected Object getPreAuthenticatedPrincipal(HttpServletRequest request) {
        logger.debug("Obtem o principal do token");
        return request.getHeader(HEAD_BEARER);
    }

    @Override
    protected Object getPreAuthenticatedCredentials(HttpServletRequest request) {
        logger.debug("Obtem o principal do token pelas credenciais");
        return request.getHeader(HEAD_BEARER);
    }

    @Override
    @Autowired
    public void setAuthenticationManager(AuthenticationManager authenticationManager) {
        super.setAuthenticationManager(authenticationManager);
    }
}
