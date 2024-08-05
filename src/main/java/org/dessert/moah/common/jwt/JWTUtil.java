package org.dessert.moah.common.jwt;

import io.jsonwebtoken.Jwts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;

@Component
public class JWTUtil {
    private static final Logger logger = LoggerFactory.getLogger(JWTUtil.class);
    private SecretKey secretKey;


    public JWTUtil(@Value("${jwt.secret.key}") String secret) {

        this.secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), Jwts.SIG.HS256.key()
                                                                                                  .build()
                                                                                                  .getAlgorithm());
//        logger.info("Secret Key: {}", Base64.getEncoder()
//                                            .encodeToString(secretKey.getEncoded()));


    }


    public String getUsername(String token) {

        return Jwts.parser()
                   .verifyWith(secretKey)
                   .build()
                   .parseSignedClaims(token)
                   .getPayload()
                   .get("email", String.class);
    }

    public String getRole(String token) {

        return Jwts.parser()
                   .verifyWith(secretKey)
                   .build()
                   .parseSignedClaims(token)
                   .getPayload()
                   .get("role", String.class);
    }

    public Boolean isExpired(String token) {

        return Jwts.parser()
                   .verifyWith(secretKey)
                   .build()
                   .parseSignedClaims(token)
                   .getPayload()
                   .getExpiration()
                   .before(new Date());
    }

    public String getCategory(String token) {

        return Jwts.parser()
                   .verifyWith(secretKey)
                   .build()
                   .parseSignedClaims(token)
                   .getPayload()
                   .get("category", String.class);
    }

//    public String getEmail(String token) {
//
//        return Jwts.parser()
//                   .verifyWith(secretKey)
//                   .build()
//                   .parseSignedClaims(token)
//                   .getPayload()
//                   .get("email", String.class);
//    }

    public String createJwt(String category, String role,String email, Long expiredMs) {

        return Jwts.builder()
                   .claim("category", category)
//                   .claim("username", username)
                   .claim("role", role)
                   .claim("email", email)
                   .issuedAt(new Date(System.currentTimeMillis()))
                   .expiration(new Date(System.currentTimeMillis() + expiredMs))
                   .signWith(secretKey)
                   .compact();
    }
}
