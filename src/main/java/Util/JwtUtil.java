package Util;

import Service.RevokedTokenService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Date;

@Component
public class JwtUtil {
    private String secretKey = "1TjXchw5FloESb63Kc+DFhTARvpWL4jUGCwfGWxuG5SIf/1y/LgJxHnMqaF6A/ij";
    @Autowired
    private  RevokedTokenService revokedTokenService;

    // Tạo JWT từ username và authorities
    public String generateToken(String username, Collection<? extends GrantedAuthority> authorities,int tokenVersion) {
        return Jwts.builder()
                .setSubject(username)
                .claim("roles", authorities.stream().map(GrantedAuthority::getAuthority).toList())
                .claim("tokenVersion", tokenVersion)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 2))  // Token có hiệu lực trong 1p
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    public String generateRefreshToken(String username, Collection<? extends GrantedAuthority> authorities,int tokenVersion) {
        return Jwts.builder()
                .setSubject(username)
                .claim("roles", authorities.stream().map(GrantedAuthority::getAuthority).toList())
                .claim("tokenVersion", tokenVersion)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 5))  // Token có hiệu lực trong 10p
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    // Lấy username từ JWT
    public String extractUsername(String token) {
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public Integer extractTokenVersion(String token) {
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody()
                .get("tokenVersion", Integer.class);
    }

    // Kiểm tra xem token có hợp lệ không
    public boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    // Lấy ngày hết hạn từ token
    public Date extractExpiration(String token) {
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody()
                .getExpiration();
    }


    public boolean isMathTokenVersion(String token,Integer tokenVersion){
        int tokenVersionJwt = extractTokenVersion(token);
        return tokenVersionJwt == tokenVersion;
    }

    public boolean isRevokedToken(String token) throws IllegalAccessException {
        if(revokedTokenService.findByToken(token)){
            throw new IllegalAccessException("Token đã bị thu hồi!");
        }
        return true;
    }

    public boolean validateToken(String token, String username,Integer tokenVersion) throws IllegalAccessException {
        return (username.equals(extractUsername(token))
                && !isTokenExpired(token)
                && isMathTokenVersion(token,tokenVersion)
                && isRevokedToken(token)
        );
    }
}
