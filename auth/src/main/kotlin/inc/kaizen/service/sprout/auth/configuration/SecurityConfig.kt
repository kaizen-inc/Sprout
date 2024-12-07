package inc.kaizen.service.sprout.auth.configuration

import com.nimbusds.jose.jwk.JWK
import com.nimbusds.jose.jwk.JWKSet
import com.nimbusds.jose.jwk.RSAKey
import com.nimbusds.jose.jwk.source.ImmutableJWKSet
import com.nimbusds.jose.jwk.source.JWKSource
import com.nimbusds.jose.proc.SecurityContext
import inc.kaizen.service.sprout.auth.service.AuthUserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.Customizer.withDefaults
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configurers.HttpBasicConfigurer
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.oauth2.jwt.*
import org.springframework.security.web.SecurityFilterChain
import java.util.Optional


@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
open class SecurityConfig {

    @Autowired
    private lateinit var authUserManager: AuthUserService

    @Autowired
    private lateinit var keyProperty: RsaKeyProperty

    @Value("\${springdoc.api-docs.path:#{null}}")
    private lateinit var apiDocsPath: Optional<String>

    @Value("\${springdoc.swagger-ui.path:#{null}}")
    private lateinit var swaggerUiPath: Optional<String>

    @Bean
    open fun authProvider(passwordEncoder: PasswordEncoder): DaoAuthenticationProvider {
        val authProvider = DaoAuthenticationProvider()
        authProvider.setUserDetailsService(authUserManager)
        authProvider.setPasswordEncoder(passwordEncoder)
        return authProvider
    }

    @Bean
    @Throws(java.lang.Exception::class)
    open fun filterChain(http: HttpSecurity): SecurityFilterChain {
        return http
            .csrf { csrf -> csrf.disable() }
            .cors { cors -> cors.disable() }
            .authorizeHttpRequests { authorize ->
                val apiDocs = if (apiDocsPath.isPresent) "${apiDocsPath}/**" else "/v3/api-docs/**"
                val apiDocsYaml = if (apiDocsPath.isPresent) "${apiDocsPath}.yaml" else "/v3/api-docs.yaml"
                authorize
                    .requestMatchers("/ping").permitAll()
                    .requestMatchers("/user/**").permitAll()
                    .requestMatchers(apiDocs).permitAll()
                    .requestMatchers(apiDocs).permitAll()
                    .requestMatchers(apiDocsYaml).permitAll()
                    .requestMatchers("/swagger-ui/**").permitAll()

                if (swaggerUiPath.isPresent)
                    authorize.requestMatchers("${swaggerUiPath}/**").permitAll()
                authorize.anyRequest().authenticated()
            }
            .sessionManagement { sessionManagement ->
                sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            }
            .oauth2ResourceServer { it.jwt(withDefaults()) }
            .userDetailsService(authUserManager)
            .httpBasic { withDefaults<HttpBasicConfigurer<HttpSecurity>>() }
            .build()
    }

    @Bean
    open fun jwtDecoder(): JwtDecoder {
        return NimbusJwtDecoder.withPublicKey(keyProperty.publicKey).build()
    }

    @Bean
    open fun jwtEncoder(): JwtEncoder {
        val jwk: JWK = RSAKey.Builder(keyProperty.publicKey).privateKey(keyProperty.privateKey).build()
        val jwks: JWKSource<SecurityContext> = ImmutableJWKSet(JWKSet(jwk))
        return NimbusJwtEncoder(jwks)
    }

//    @Bean
//    open fun registeredClientRepository(jdbcTemplate: JdbcTemplate): JdbcRegisteredClientRepository {
//        return JdbcRegisteredClientRepository(jdbcTemplate)
//    }
}