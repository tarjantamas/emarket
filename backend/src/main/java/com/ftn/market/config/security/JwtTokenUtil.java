package com.ftn.market.config.security;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtTokenUtil implements Serializable {

  @Value("${jwt.secret}")
  private String secret;

  public static final long JWT_TOKEN_VALIDITY = 5 * 60 * 60;

  public String getUsernameFromToken(final String token) {
    return getClaimFromToken(token, Claims::getSubject);
  }

  public static String getTenantFromToken(final HttpServletRequest request) {
    final String requestTokenHeader = request.getHeader("Authorization");
    final String jwtToken = requestTokenHeader.substring(7);
    return (String) Jwts.parser().setSigningKey("secret-key").parseClaimsJws(jwtToken).getBody().get("tenant");
  }

  public Date getExpirationDateFromToken(final String token) {
    return getClaimFromToken(token, Claims::getExpiration);
  }

  public <T> T getClaimFromToken(final String token, final Function<Claims, T> claimsResolver) {
    final Claims claims = getAllClaimsFromToken(token);
    return claimsResolver.apply(claims);
  }

  private Claims getAllClaimsFromToken(final String token) {
    return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
  }

  private Boolean isTokenExpired(final String token) {
    final Date expiration = getExpirationDateFromToken(token);
    return expiration.before(new Date());
  }

  public String generateToken(final JwtUser userDetails) {
    final Claims claims = Jwts.claims().setSubject(userDetails.getUsername());
    claims.put("roles", userDetails.getAuthorities());
    claims.put("user_id", userDetails.getId());
    return doGenerateToken(claims, userDetails.getUsername());
  }

  private String doGenerateToken(final Map<String, Object> claims, final String subject) {
    return Jwts.builder()
      .setClaims(claims)
      .setSubject(subject)
      .setIssuedAt(new Date(System.currentTimeMillis()))
      .setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY * 100000))
      .signWith(SignatureAlgorithm.HS512, secret)
      .compact();
  }

  public Boolean validateToken(final String token, final UserDetails userDetails) {
    final String username = getUsernameFromToken(token);
    return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
  }
}
