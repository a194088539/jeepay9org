package org.jeepay.manage.secruity;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.jeepay.core.common.util.CookieUtil;
import org.jeepay.core.common.util.MyLog;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@SuppressWarnings("SpringJavaAutowiringInspection")
@Component
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Value("${jwt.cookie}")
    private String tokenCookie;

    private MyLog _log = MyLog.getLog(JwtAuthenticationTokenFilter.class);

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain) throws ServletException, IOException {
        String authToken = CookieUtil.getCookieByName(request, tokenCookie);
        if(StringUtils.isBlank(authToken)) {
            authToken = request.getParameter("access_token");
        }

        // 支持跨域开发,临时指定
        //if(authToken == null) authToken = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJqbWRoYXBweSIsImNyZWF0ZWQiOjE1MTc2NjczODYyMTEsImlkIjoxLCJleHAiOjE1MTgyNzIxODZ9.Jhn7M0eaqlZM2Hp9_7b1T7wR8Dbc5b9z3yETkN4w9DJcUglQeYzAUxQxvyVE-l-hkwOYzKPuzBliD8u-eWC3fw";

        if (authToken != null) {
            String username = jwtTokenUtil.getUsernameFromToken(authToken);
            _log.debug("checking authentication {}", username);
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                // 如果我们足够相信token中的数据，也就是我们足够相信签名token的secret的机制足够好
                // 这种情况下，我们可以不用再查询数据库，而直接采用token中的数据
                // 我们还是通过Spring Security的 @UserDetailsService 进行了数据查询
                // 但简单验证的话，你可以采用直接验证token是否合法来避免昂贵的数据查询
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
                if (userDetails != null && jwtTokenUtil.validateToken(authToken, userDetails)) {
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(
                            request));
                    _log.debug("authenticated user " + username + ", setting security context");
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        }
        chain.doFilter(request, response);
    }

}
