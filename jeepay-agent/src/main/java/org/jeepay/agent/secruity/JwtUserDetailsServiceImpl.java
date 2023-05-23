package org.jeepay.agent.secruity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.jeepay.agent.user.service.UserService;
import org.jeepay.core.entity.AgentInfo;
import org.jeepay.core.entity.MchInfo;

@Service
public class JwtUserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        AgentInfo agentInfo = userService.findByLoginName(username);
        if (agentInfo == null) {
            //throw new UsernameNotFoundException(String.format("No user found with username '%s'.", username));
            return null;
        } else {
            return JwtUserFactory.create(username, agentInfo);
        }
    }
}
