package team.weacsoft.system.security;

import org.slf4j.MDC;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;
import team.weacsoft.common.utils.JwtUtil;
import team.weacsoft.user.entity.UserInfo;
import team.weacsoft.user.service.IUserInfoService;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * @author GreenHatHG
 */

public class JwtRequestFilter extends OncePerRequestFilter {

    private IUserInfoService userInfoService;

    public JwtRequestFilter(IUserInfoService userInfoService) {
        this.userInfoService = userInfoService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        final String token = JwtUtil.getJwtFromHttpServletRequest(request);
        if (SecurityContextHolder.getContext().getAuthentication() == null) {
            final String id = JwtUtil.getIdFromRequest(request);
            UserInfo userInfo = findById(id);
            if(JwtUtil.verify(token, id, userInfo.getPassword())){
                MDC.put("userTableId", id);
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                        null, null, getAuthorities(userInfo));
                usernamePasswordAuthenticationToken
                        .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                //最基本的对象是SecurityContextHolder，存储着应用程序当前安全上下文的详细信息，其中包括当前使用该应用程序的主体的详细信息
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }
        }
        chain.doFilter(request, response);
    }

    private Set<SimpleGrantedAuthority> getAuthorities(UserInfo userInfo){
        Set<SimpleGrantedAuthority> authorities = new HashSet<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_" + String.valueOf(userInfo.getRole())));
        return authorities;
    }

    private UserInfo findById(String id){
        UserInfo userInfo = userInfoService.getById(id);
        if(userInfo == null){
            throw new UsernameNotFoundException("查无此人");
        }
        return userInfo;
    }
}
