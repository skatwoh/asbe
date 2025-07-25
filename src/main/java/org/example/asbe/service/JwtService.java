package org.example.asbe.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;


@Component
public class JwtService {

    @Value("${app.auth.tokenSecret}")
    private String SECRET;

    @Value("${app.auth.tokenExpirationMsec}")
    private long jwtExpiration;

    // Generate token with given user name
    public String generateToken(String userName, String email, List<String> roles, Integer userId) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, userName, email, roles, userId);
    }

    // Create a JWT token with specified claims and subject (user name)
    private String createToken(Map<String, Object> claims, String userName, String email, List<String> roles, Integer userId) {
        // Thêm vai trò vào claims
        claims.put("roles", roles);
        claims.put("username", userName);
        claims.put("email", email);
        claims.put("id", userId);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpiration)) // Token valid for 30 minutes
                .signWith(getSignKey(), SignatureAlgorithm.HS256)
                .compact();
    }


    // Get the signing key for JWT token
    private Key getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    // Extract the username from the token
//    public String extractUsername(String token) {
//        return extractClaim(token, Claims::getSubject);
//    }

    public String extractUsername(String token) {
        return extractAllClaims(token).get("username", String.class);
    }

    // Extract the expiration date from the token
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    // Extract a claim from the token
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    // Extract all claims from the token
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // Check if the token is expired
    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    // Validate the token against user details and expiration
    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
}

