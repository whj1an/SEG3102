// haojian wang 300411829, Lab9
package seg3102.tempconverterapi.security

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.provisioning.InMemoryUserDetailsManager
import org.springframework.security.web.SecurityFilterChain

@Configuration
@EnableWebSecurity
class SecurityConfig {

    // Every request (including the temperature conversion endpoints) must be
    // authenticated with HTTP Basic. No login form, no session, no CSRF (stateless REST API).
    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .csrf { csrf -> csrf.disable() }
            .sessionManagement { session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            }
            .authorizeHttpRequests { auth -> auth.anyRequest().authenticated() }
            .httpBasic(Customizer.withDefaults())
        return http.build()
    }

    // In-memory users: user1/pass1 and user2/pass2
    @Bean
    fun userDetailsService(passwordEncoder: PasswordEncoder): UserDetailsService {
        val user1 = User.withUsername("user1")
            .password(passwordEncoder.encode("pass1"))
            .roles("USER")
            .build()
        val user2 = User.withUsername("user2")
            .password(passwordEncoder.encode("pass2"))
            .roles("USER")
            .build()
        return InMemoryUserDetailsManager(user1, user2)
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder = BCryptPasswordEncoder()
}
