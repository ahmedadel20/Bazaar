package org.bazaar.giza.auth.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.bazaar.giza.auth.exception.AuthException;
import org.bazaar.giza.auth.repo.UserRolesRepo;
import org.bazaar.giza.constant.ErrorMessage;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtService {
    public final String SECRET;
    private AuthenticationManager authenticationManager;

    private final UserRolesRepo userRolesRepo;

    public JwtService(AuthenticationManager authenticationManager, UserRolesRepo userRolesRepo) {
        this.SECRET = "JXspT5HWeAtHqD7z1Z9yDFR8wS0QhzjD0Nc9+wrlgHk=";
        this.authenticationManager = authenticationManager;
        this.userRolesRepo = userRolesRepo;
    }

    public String generateToken(String userName) {
        Map<String, Object> claims = new HashMap<>();

        claims.put("bazaarUserId", userRolesRepo.findByUser_Email(userName).get().getUserId());
        return createToken(claims, userName);
    }

    private String createToken(Map<String, Object> claims, String userName) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userName)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 30))
                .signWith(getSignKey(), SignatureAlgorithm.HS256).compact();
    }

    private Key getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    public String authenticateAndGetToken(String username, String password) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password));
        if (authentication.isAuthenticated()) {
            return generateToken(username);
        } else {
            throw new AuthException(ErrorMessage.INCORRECT_EMAIL_OR_PASSWORD);
        }
    }
}
