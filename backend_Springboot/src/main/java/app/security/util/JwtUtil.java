package app.security.util;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.security.Key;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;

import java.util.stream.Collectors;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String SECRET_KEY;

    @Value("${jwt.expiration}")
    private long jwtExpirationMs;

    private static final Logger LOGGER = LoggerFactory.getLogger(JwtUtil.class);

    private Set<String> invalidTokens = new HashSet<>();

    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = Map.of(
                "roles", userDetails.getAuthorities().stream()
                        .map(grantedAuthority -> grantedAuthority.getAuthority())
                        .collect(Collectors.toList())
        );

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    // obtención de los claims
    private Claims getAllClaimsFromToken(String token) {
        Claims claims;
        try {
            JwtParser parser = Jwts.parser()
                    .setSigningKey(getSigningKey())
                    .build();
            claims = parser.parseClaimsJws(token).getBody();
        } catch (Exception e) {
            LOGGER.error("Could not get all claims Token from passed token");
            claims = null;
        }
        return claims;
    }

    private Key getSigningKey() {
        byte[] keyBytes = Base64.getDecoder().decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    // recuperación del nickname desde el token
    public String getUsernameFromToken(String token) {
        System.out.println (token);
        return getClaimFromToken(token, Claims::getSubject);
    }

    public boolean isTokenValid(String token) {
//        return !isTokenExpired(token);
        return !isTokenExpired(token) && !isTokenInvalidated(token);
    }

    // si queremos obtener un claim específico
    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        if (token == null || token.isEmpty()) {
            throw new IllegalArgumentException("Token is null or empty");
        }

        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    // verificar si el token ha expirado
    public Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    // obtener la fecha de expiración del token
    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    // invalidación de token tras logout
    public void invalidateToken(String token) {
        invalidTokens.add(token);
    }

    // comprobación de invalidación de token
    private boolean isTokenInvalidated(String token) {
        return invalidTokens.contains(token);
    }
}