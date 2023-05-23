package org.jeepay.manage.secruity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Date;

public class JwtUser implements UserDetails {
    private Long id;
    private String name;
    private Byte status;
    private String username;
    private String password;
    private final Collection<? extends GrantedAuthority> authorities;
    private final Date lastPasswordResetDate;
    private Byte isSuperAdmin;

    public JwtUser(
            Long id,
            String name,
            Byte status,
            String username,
            String password,
            Collection<? extends GrantedAuthority> authorities,
            Date lastPasswordResetDate,
            Byte isSuperAdmin) {
        this.id = id;
        this.name = name;
        this.status = status;
        this.username = username;
        this.password = password;
        this.authorities = authorities;
        this.lastPasswordResetDate = lastPasswordResetDate;
        this.isSuperAdmin = isSuperAdmin;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @JsonIgnore
    public Long getId() {
        return id;
    }

    @JsonIgnore
    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isEnabled() {
        return true;
    }

    @JsonIgnore
    public Date getLastPasswordResetDate() {
        return lastPasswordResetDate;
    }

    public String getName() {
        return name;
    }

    public Byte getStatus() {
        return status;
    }

    public Byte getIsSuperAdmin() {
        return isSuperAdmin;
    }

    /**
     * 根据Authentication获取当前登录用户信息
     * @return
     */
    public static JwtUser getCurrentJWTUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication == null) return null;
        try {
            return (JwtUser) authentication.getPrincipal();
        } catch (Exception e) {
            return null;
        }
    }

    public static String getCurrentUserName(){
        JwtUser user = getCurrentJWTUser();
        return user == null ? "匿名用户" : user.getName();
    }

    public static Long getCurrentUserId(){
        JwtUser user = getCurrentJWTUser();
        return user == null ? 0L : user.getId();
    }

}
