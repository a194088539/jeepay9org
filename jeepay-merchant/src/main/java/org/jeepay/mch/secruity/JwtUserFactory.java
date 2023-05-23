package org.jeepay.mch.secruity;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.jeepay.core.entity.MchInfo;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public final class JwtUserFactory {

    private JwtUserFactory() {
    }

    public static JwtUser create(String userName, MchInfo mchInfo) {
        return new JwtUser(
                mchInfo.getMchId(),
                mchInfo.getName(),
                mchInfo.getType(),
                mchInfo.getStatus(),
                userName,
                mchInfo.getPassword(),
                mchInfo.getEmail(),
                mapToGrantedAuthorities(mchInfo.getRole()),
                mchInfo.getLastPasswordResetTime()
        );
    }

    private static List<GrantedAuthority> mapToGrantedAuthorities(String role) {
        String[] roles = role.split(":");
        List<String> authorities = Arrays.asList(roles);
        return authorities.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }
}