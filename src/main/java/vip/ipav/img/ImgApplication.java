package vip.ipav.img;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import vip.ipav.img.conf.SimpleFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@SpringBootApplication
@EnableZuulProxy
public class ImgApplication {

    public static void main(String[] args) {
        SpringApplication.run(ImgApplication.class, args);
    }

    private CorsConfiguration buildConfig() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.addAllowedOrigin("*");
        corsConfiguration.addAllowedHeader("*");
        corsConfiguration.addAllowedMethod("*");
        return corsConfiguration;

    }

    /**
     * 跨域过滤器
     * @return
     */
    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", buildConfig());
        return new CorsFilter(source);
    }

    @Bean
    public SimpleFilter simpleFilter() {
        return new SimpleFilter();
    }

    @EnableWebSecurity
    static class WebSecurityConfig extends WebSecurityConfigurerAdapter {
        @Override
        protected void configure(HttpSecurity http) throws Exception {
            //Howard 2019-03-20,错误的做法：
            //http.csrf().disable();//这样会直接取消验证账号密码了
            http.csrf().disable().authorizeRequests()
                    .anyRequest()
                    .authenticated()
                    .and()
                    .formLogin();
            //post时security跨域检测关闭
            http.csrf().ignoringAntMatchers("/**");
        }

        @Override
        public void configure(WebSecurity web) throws Exception {
            //web.ignoring().antMatchers("/**");
            // AuthenticationTokenFilter will ignore the below paths
            web.ignoring()
                    .antMatchers(
                            HttpMethod.POST,
                            "/api/**",
                            "/upload"
                    )
                    .and()
                    .ignoring()
                    .antMatchers(
                            HttpMethod.GET,
                            "/index.html",
                            "/",
                            "/sg/**",
                            "/favicon.ico",
                            "/**/*.css",
                            "/**/*.js"
                    );
        }
    }

}
