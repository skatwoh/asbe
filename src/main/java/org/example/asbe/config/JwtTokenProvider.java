package org.example.asbe.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;

@Component
public class JwtTokenProvider {

    @Value("${app.auth.tokenSecret}")
    private String jwtSecret;

    @Value("${app.auth.tokenExpirationMsec}")
    private long jwtExpiration;

    // Tạo JWT từ username
    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpiration))
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }

    // Lấy username từ JWT
    public String getUsernameFromToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }

    // Kiểm tra token có hợp lệ không (có hết hạn không, chữ ký có đúng không)
    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            // Nếu token không hợp lệ hoặc hết hạn, sẽ xảy ra exception
            return false;
        }
    }

    // Lấy Authentication từ token (thường là username)
    public UsernamePasswordAuthenticationToken getAuthentication(String token) {
        String username = getUsernameFromToken(token);
        if (username != null) {
            return new UsernamePasswordAuthenticationToken(username, null, new ArrayList<>());
        }
        return null;
    }
}
