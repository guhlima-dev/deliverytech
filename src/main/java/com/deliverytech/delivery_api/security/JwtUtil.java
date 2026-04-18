package com.deliverytech.delivery_api.security;

import com.deliverytech.delivery_api.model.Usuario;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Service
public class JwtUtil {

    private static final String SECRET_KEY = "Delegate-Recount-Undaunted-Depletion9-Alike-Stinger-Cosigner-Dispatch";

    private static final long EXPIRATION = 1000 * 60 * 60 * 24;

    private Key getSignKey(){
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));
    }

    public String generateToken(Usuario usuario){

        return Jwts.builder()
                .setSubject(usuario.getEmail())
                .claim("userID", usuario.getId())
                .claim("role", usuario.getRole().name())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION))
                .signWith(getSignKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public Claims extractClaims(String token){
        return  Jwts.parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public String extractEmail(String token){
        return extractClaims(token).getSubject();
    }

    public boolean isTokenValid(String token, String email){
        try {
            Claims claims = extractClaims(token);
            return claims.getSubject().equals(email)
                    && !claims.getExpiration().before(new Date());

        }catch (Exception e){
            return false;
        }
    }

    public boolean isTokenExpired(String token){
        return extractClaims(token).getExpiration().before(new Date());
    }

    public String extractRoles(String token) {
        return extractClaims(token)
                .get("roles", String.class);
    }

}



