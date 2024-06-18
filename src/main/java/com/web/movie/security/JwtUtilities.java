package com.web.movie.security;

import com.web.movie.entity.UserAuthentication;
import com.web.movie.repository.UserAuthenticationRepository;
import io.jsonwebtoken.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.function.Function;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtUtilities {
    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private Long jwtExpiration;

    private final UserAuthenticationRepository userAuthenticationRepository;

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Claims extractAllClaims(String token) {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public Boolean validateToken(String token, UserDetails userDetails) {

        if (token == null) {
            return false;
        }

        final String email = extractUsername(token);
        final UserAuthentication userAuthentication = userAuthenticationRepository.findByUser_Email(email);

        if (userAuthentication == null) {
            return false;
        }

        String userToken = userAuthentication.getToken();
        Date userExpiredAt = userAuthentication.getExpiredAt();


        return (email.equals(userDetails.getUsername()) && !isTokenExpired(token) && !isTokenExpired(userExpiredAt) && token.equals(userToken));
    }


    public Boolean isTokenExpired(String token) {

        return extractExpiration(token).before(new Date());
    }

    public Boolean isTokenExpired(Date date) {
        return date.before(new Date());
    }

    public String generateToken(String email, List<String> roles) {

        return Jwts.builder()
                .setSubject(email)
                .claim("role", roles)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(Date.from(Instant.now().plus(jwtExpiration, ChronoUnit.SECONDS)))
                .signWith(SignatureAlgorithm.HS256, secret).compact();
    }

    public boolean validateToken(String token) {

        try {
            Jwts.parserBuilder()
                    .setSigningKey(secret)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (SignatureException e) {
            System.out.println("Invalid JWT signature.");
            log.info("Invalid JWT signature.");
            log.trace("Invalid JWT signature trace: {}", e);
        } catch (MalformedJwtException e) {
            System.out.println("Invalid JWT token.");
            log.info("Invalid JWT token.");
            log.trace("Invalid JWT token trace: {}", e);
        } catch (ExpiredJwtException e) {
            System.out.println("Expired JWT token.");
            log.info("Expired JWT token.");
            log.trace("Expired JWT token trace: {}", e);
        } catch (UnsupportedJwtException e) {
            System.out.println("Unsupported JWT token.");
            log.info("Unsupported JWT token.");
            log.trace("Unsupported JWT token trace: {}", e);
        } catch (IllegalArgumentException e) {
            System.out.println("JWT token compact of handler are invalid.");
            log.info("JWT token compact of handler are invalid.");
            log.trace("JWT token compact of handler are invalid trace: {}", e);
        }
        return false;
    }

    public String getToken(HttpServletRequest httpServletRequest) {
        final String bearerToken = httpServletRequest.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7, bearerToken.length());
        } // The part after "Bearer "
        return null;
    }
}
