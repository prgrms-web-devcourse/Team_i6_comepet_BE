package com.pet.common.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
public final class Jwt {
    private static final String ACCOUNT_ID = "accountId";
    private static final String ROLES = "roles";

    private final String issuer;
    private final String clientSecret;
    private final int expirySeconds;
    private final Algorithm algorithm;
    private final JWTVerifier jwtVerifier;

    public Jwt(String issuer, String clientSecret, int expirySeconds) {
        this.issuer = issuer;
        this.clientSecret = clientSecret;
        this.expirySeconds = expirySeconds;
        this.algorithm = Algorithm.HMAC512(clientSecret);
        this.jwtVerifier = JWT.require(algorithm).withIssuer(issuer).build();
    }

    public String sign(Claims claims) {
        Date now = new Date();
        JWTCreator.Builder builder = JWT.create();
        builder.withIssuer(issuer);
        builder.withIssuedAt(now);
        if (expirySeconds > 0) {
            builder.withExpiresAt(new Date(now.getTime() + expirySeconds * 1_000L));
        }
        builder.withClaim(ACCOUNT_ID, claims.accountId);
        builder.withArrayClaim(ROLES, claims.roles);
        return builder.sign(algorithm);
    }

    public Claims verify(String token) throws JWTVerificationException {
        return new Claims(jwtVerifier.verify(token));
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Claims {
        private Long accountId;
        private String[] roles;
        private Date issuedAt;
        private Date expiration;

        public Claims(DecodedJWT decodedJWT) {
            Claim accountId = decodedJWT.getClaim(ACCOUNT_ID);
            if (!accountId.isNull()) {
                this.accountId = accountId.asLong();
            }
            Claim roles = decodedJWT.getClaim(ROLES);
            if (!roles.isNull()) {
                this.roles = roles.asArray(String.class);
            }
            this.issuedAt = decodedJWT.getIssuedAt();
            this.expiration = decodedJWT.getExpiresAt();
        }

        public static Claims from(Long accountId, String[] roles) {
            Claims claims = new Claims();
            claims.accountId = accountId;
            claims.roles = roles;
            return claims;
        }

        public Map<String, Object> asMap() {
            Map<String, Object> map = new HashMap<>();
            map.put("accountId", accountId);
            map.put("roles", roles);
            map.put("iat", iat());
            map.put("exp", exp());
            return map;
        }

        private long iat() {
            return issuedAt != null ? issuedAt.getTime() : -1;
        }

        private long exp() {
            return expiration != null ? expiration.getTime() : -1;
        }

        private void eraseIat() {
            issuedAt = null;
        }

        private void eraseExp() {
            expiration = null;
        }
    }

}