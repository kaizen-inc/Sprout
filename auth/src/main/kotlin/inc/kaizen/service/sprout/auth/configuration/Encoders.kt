package inc.kaizen.service.sprout.auth.configuration

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder

@Configuration
open class Encoders {

    @Bean
    open fun userPasswordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder(8)
    }
}