package ca.pethappy.server.security.services;

import ca.pethappy.server.models.Role;
import ca.pethappy.server.models.User;
import ca.pethappy.server.security.models.TokenProperties;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.apache.el.parser.Token;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Service
public class TokenService {
    private static final Logger logger = LoggerFactory.getLogger(Token.class);

    private final TokenProperties tokenProperties;
    private final String issuer;
    private final Algorithm algorithm;
    private final JWTVerifier jwtVerifier;

    @Autowired
    public TokenService(TokenProperties tokenProperties, @Value("${spring.application.name}") String issuer) {
        this.tokenProperties = tokenProperties;
        this.issuer = issuer;
        this.algorithm = Algorithm.HMAC256(tokenProperties.getSecret());
        this.jwtVerifier = JWT.require(algorithm).acceptExpiresAt(0).build();
    }

    String encode(User user) {
        LocalDateTime now = LocalDateTime.now();
        try {
            return JWT.create()
                    .withIssuer(issuer)
                    .withSubject(user.getEmail())
                    .withIssuedAt(Date.from(now.atZone(ZoneId.systemDefault()).toInstant()))
                    .withExpiresAt(Date.from(now.plusSeconds(tokenProperties.getMaxAgeSeconds()).atZone(ZoneId.systemDefault()).toInstant()))
                    .withArrayClaim("roles", user.getRoles().stream().map(Role::getName).toArray(String[]::new))
                    .withClaim("email", user.getEmail())
                    .withClaim("firstName", user.getFirstName())
                    .withClaim("lastName", user.getLastName())
                    .withClaim("id", user.getId())
                    .sign(algorithm);
        } catch (JWTCreationException ex) {
            logger.error("Cannot properly create token", ex);
            throw new TokenCreationException("Cannot properly create token", ex);
        }
    }

    DecodedJWT decode(String token) {
        try {
            return this.jwtVerifier.verify(token);
        } catch (Throwable ex) {
            logger.error("Erro ao decodificar token. Detalhes: {}", ex.getMessage());
            return null;
        }
    }
}
