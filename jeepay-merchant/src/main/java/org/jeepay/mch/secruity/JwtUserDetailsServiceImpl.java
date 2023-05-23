package org.jeepay.mch.secruity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.jeepay.core.entity.MchInfo;
import org.jeepay.mch.user.service.UserService;

@Service
public class JwtUserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        MchInfo mchInfo = userService.findByLoginName(username);
        if (mchInfo == null) {
            //throw new UsernameNotFoundException(String.format("No user found with username '%s'.", username));
            return null;
        } else {
            return JwtUserFactory.create(username, mchInfo);
        }
    }
}
