package tacos.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

import static org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher;

@Configuration
@EnableGlobalMethodSecurity(securedEnabled = true)
public class SecurityConfig {
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    MvcRequestMatcher.Builder mvc(HandlerMappingIntrospector introspector) {
        return new MvcRequestMatcher.Builder(introspector);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, MvcRequestMatcher.Builder mvc) throws Exception {
        http.authorizeHttpRequests(authz -> authz
                    //для классики
                    .requestMatchers(
                            mvc.pattern("/design"),
                            mvc.pattern("/orders"),
                            mvc.pattern("/orders/current")
                    ).hasRole("USER")
                    //для API
                    .requestMatchers(
                            antMatcher(HttpMethod.POST, "/api/ingredients")
                    ).hasAuthority("SCOPE_writeIngredients")
                    .requestMatchers(
                            antMatcher(HttpMethod.DELETE, "/api/ingredients/**")
                    ).hasAuthority("SCOPE_deleteIngredients")
                    .requestMatchers(mvc.pattern("/"), mvc.pattern("/**")).permitAll()
                )
                .formLogin(login -> login
                        .loginPage("/login")
                        .defaultSuccessUrl("/design", true)
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/")
                )
                .oauth2ResourceServer((oauth2) -> oauth2.jwt(Customizer.withDefaults()));
        ;
        try {
            return http.build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
