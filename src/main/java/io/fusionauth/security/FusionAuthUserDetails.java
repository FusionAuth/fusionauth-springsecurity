package io.fusionauth.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.common.OAuth2AccessToken;

/**
 * @author Tyler Scott
 */
public class FusionAuthUserDetails implements UserDetails {
  private static final long serialVersionUID = 1L;

  public JsonNode claims;

  public OAuth2AccessToken token;

  public String userId;

  public String username;

  private List<SimpleGrantedAuthority> roles = new ArrayList<>();

  public FusionAuthUserDetails(JsonNode claims, OAuth2AccessToken token) {
    userId = claims.get("sub").asText();
    if (claims.has("email")) {
      username = claims.get("email").asText();
    }
    if (claims.has("roles") && claims.get("roles").isArray()) {
      for (JsonNode role : claims.get("roles")) {
        this.roles.add(new SimpleGrantedAuthority(role.asText()));
      }
    }

    this.claims = claims;
    this.token = token;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return roles;
  }

  @Override
  public String getPassword() {
    return null;
  }

  @Override
  public String getUsername() {
    return username;
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }
}
