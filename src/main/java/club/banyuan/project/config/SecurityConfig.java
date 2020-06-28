package club.banyuan.project.config;

import club.banyuan.project.common.api.CommonResult;
import cn.hutool.json.JSONUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.IOException;
import java.io.PrintWriter;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final static String LOGIN_PAGE_URI = "/studyroom/index";
    private final static String FORBIDDEN_PAGE_URI = "/forbidden";

    @Autowired
    DataSource dataSource;

//    @Autowired
//    private UserDetailsService userDetailsService;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        //下面这两行配置表示在内存中配置了两个用户
//        auth.inMemoryAuthentication()
//                .withUser("banyuan").roles("admin").password("$2a$10$/EEnczwYgcWMlmH1o2TqFuJyTTXJciig2G.Vx0fninU/6D2HKoOXy")
//                .and()
//                .withUser("zhangsan").roles("user").password("$2a$10$/EEnczwYgcWMlmH1o2TqFuJyTTXJciig2G.Vx0fninU/6D2HKoOXy")
//                .and()
//                .withUser("lisi").roles("user").password("$2a$10$/EEnczwYgcWMlmH1o2TqFuJyTTXJciig2G.Vx0fninU/6D2HKoOXy");

        auth.jdbcAuthentication()
                .dataSource(dataSource)
                .usersByUsernameQuery("select username,password,enabled from user WHERE username=?")
                .authoritiesByUsernameQuery("select username,role as authority from authorities where username=?")
                .passwordEncoder(new BCryptPasswordEncoder());

        // 增加自定义的UserDetailService
        // userDetailsAuthenticationProvider.setUserDetailsService(userDetailsService);
        // 设置一个Provider
        //auth.authenticationProvider(userDetailsAuthenticationProvider);
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        String[] allowList = {
                "/actuator/**",
                "/static/**",
                "/**.js",
                "/**.css",
                "/**.jpg",
                "/**.jpeg",
                "/**.png",
                "/**.gif",
                "/studyroom/**",
                "/api/user/weather",
                "/api/user/register",
                "/api/user/login"
        };

        http.authorizeRequests()//开启登录配置
                .antMatchers("/hello").hasAuthority("user")//表示访问 /hello 这个接口，需要具备 admin 这个角色
                .antMatchers("/admin").hasAuthority("admin")//表示访问 /admin 这个接口，需要具备 admin 这个角色
                .antMatchers(allowList).permitAll()
                .anyRequest().authenticated()//表示剩余的其他接口，登录之后就能访问


                .and()
                .formLogin()
                //定义登录页面，未登录时，访问一个需要登录之后才能访问的接口，会自动跳转到该页面
                .loginPage(LOGIN_PAGE_URI)
                //登录处理接口
                .loginProcessingUrl("/doLogin")
                //定义登录时，用户名的 key，默认为 username
                .usernameParameter("username")
                //定义登录时，用户密码的 key，默认为 password
                .passwordParameter("password")
                //登录成功的处理器
                .successHandler(new AuthenticationSuccessHandler() {
                    @Override
                    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
                        response.sendRedirect("/");
                    }
                })
//                .failureHandler(new AuthenticationFailureHandler() {
//                    @Override
//                    public void onAuthenticationFailure(HttpServletRequest req, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
//                        response.sendRedirect(LOGIN_PAGE_URI);
//                    }
//                })
                .permitAll()//和表单登录相关的接口统统都直接通过

                .and()
                .logout()
                .logoutUrl("/logout")
                .logoutSuccessHandler(new LogoutSuccessHandler() {
                    @Override
                    public void onLogoutSuccess(HttpServletRequest req, HttpServletResponse resp, Authentication authentication) throws IOException, ServletException {
                        resp.setContentType("application/json;charset=utf-8");
                        PrintWriter out = resp.getWriter();
                        out.write("退出成功");
                        out.flush();

//                        CommonResult.success("退出成功");
                    }
                })
                .permitAll()
                .and()
                .httpBasic()
                .and()
                .csrf().disable()

                .exceptionHandling()
                .accessDeniedHandler(new AccessDeniedHandler() {
                    @Override
                    public void handle(HttpServletRequest httpServletRequest, HttpServletResponse response, AccessDeniedException e) throws IOException, ServletException {
                        if (httpServletRequest.getRequestURI().contains("/api/")) {
                            // 是接口
                            response.setHeader("Access-Control-Allow-Origin", "*");
                            response.setHeader("Cache-Control", "no-cache");
                            response.setCharacterEncoding("UTF-8");
                            response.setContentType("application/json");
                            response.getWriter().println(JSONUtil.parse(CommonResult.forbidden(e.getMessage())));
                            response.getWriter().flush();
                        } else {
                            // 是页面
                            response.sendRedirect(FORBIDDEN_PAGE_URI);
                        }
                    }
                })
                .authenticationEntryPoint(new AuthenticationEntryPoint() {
                    @Override
                    public void commence(HttpServletRequest httpServletRequest, HttpServletResponse response, AuthenticationException e) throws IOException, ServletException {
                        if (httpServletRequest.getRequestURI().contains("/api/")) {
                            // 是接口
                            response.setHeader("Access-Control-Allow-Origin", "*");
                            response.setHeader("Cache-Control", "no-cache");
                            response.setCharacterEncoding("UTF-8");
                            response.setContentType("application/json");
                            response.getWriter().println(JSONUtil.parse(CommonResult.unauthorized(e.getMessage())));
                            response.getWriter().flush();
                        } else {
                            // 是页面
                            response.sendRedirect(LOGIN_PAGE_URI);
                        }
                    }
                });
    }
}