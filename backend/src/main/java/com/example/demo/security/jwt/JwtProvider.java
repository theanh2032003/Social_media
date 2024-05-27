package com.example.demo.security.jwt;

import java.security.Key;
import java.util.Base64.Decoder;
import java.util.Date;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.example.demo.model.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtProvider {

    @Value("${jwt.jwtSecrect}")
    private String jwtSecret;

    @Value("${jwt.jwtExpiration}")
    private int jwtExprestion;

    public String createJwt(Authentication authentication) {
        User user = (User) authentication.getPrincipal();

        Date now = new Date();

        Date end = new Date(now.getTime()  + jwtExprestion);

        return Jwts.builder()
                .setIssuedAt(now)
                .setExpiration(end)
                .setSubject(String.valueOf(user.getId()))
                .signWith( Key(), SignatureAlgorithm.HS256)
                .compact();
    }

    private Key Key() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }

    public Long getIdByJwtToken(String token) {
        Claims claim = Jwts.parserBuilder()
                .setSigningKey(Key())
                .build()
                .parseClaimsJws(token)
                .getBody();
        return  Long.parseLong(claim.getSubject()) ;
    }

    public boolean ValidationJwtToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(Key())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            return true;
        } catch (MalformedJwtException ex) {
            throw new MalformedJwtException("Invalid JWT token");
        } catch (ExpiredJwtException ex) {
//            throw new ExpiredJwtException("Expired JWT token");
        } catch (UnsupportedJwtException ex) {
            throw new UnsupportedJwtException("Unsupported JWT token");
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException("JWT claims string is empty.");
        }
        return false;
    }

    public String parseJwtToken(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if(header != null && header.startsWith("Bearer ")) {
            return header.substring(7);
        }
        return null;
    }

    public Long getIdFromHttpRequest(HttpServletRequest request){
        String jwtToken = parseJwtToken(request);
        if(jwtToken==null){
            throw  new IllegalArgumentException("jwt token is null");
        }
        return getIdByJwtToken(jwtToken);
    }
}
