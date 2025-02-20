package org.example.asbe.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

    public List<String> getRolesFromToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(jwtSecret) // Cần thay secretKey bằng giá trị thực tế
                .parseClaimsJws(token)
                .getBody();

        return claims.get("roles", List.class); // Giả sử roles được lưu dưới dạng danh sách trong JWT
    }

    public UsernamePasswordAuthenticationToken getAuthentication(String token) {
        String username = getUsernameFromToken(token);
        List<String> roles = getRolesFromToken(token); // Hàm này cần triển khai

        if (username != null) {
            List<GrantedAuthority> authorities = new ArrayList<>();
            for (String role : roles) {
                authorities.add(new SimpleGrantedAuthority(role));
            }
            return new UsernamePasswordAuthenticationToken(username, null, authorities);
        }
        return null;
    }
}
