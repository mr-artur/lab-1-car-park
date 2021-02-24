package ua.kpi.fict.carpark.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import ua.kpi.fict.carpark.entity.enums.Role;
import ua.kpi.fict.carpark.service.UserService;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserService userService;

    private final PasswordEncoder passwordEncoder;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .csrf().disable()
            .authorizeRequests()
                .antMatchers("/css/**", "/js/**").permitAll()
                .antMatchers("/register", "/login").anonymous()
                .antMatchers("/superadmin/**").hasRole(Role.ROLE_SUPERADMIN.getNameWithoutPrefix())
                .antMatchers("/admin/**")
                    .hasAnyRole(Role.ROLE_ADMIN.getNameWithoutPrefix(), Role.ROLE_SUPERADMIN.getNameWithoutPrefix())
                .antMatchers("/driver/**").hasRole(Role.ROLE_DRIVER.getNameWithoutPrefix())
            .anyRequest()
                .authenticated()
                .and()
            .formLogin()
                .loginPage("/login")
                .permitAll()
                .successForwardUrl("/success")
                .and()
            .logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .permitAll();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService).passwordEncoder(passwordEncoder);
    }
}
