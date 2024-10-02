package inc.kaizen.service.sprout.auth.configuration

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration
import java.security.interfaces.RSAPrivateKey
import java.security.interfaces.RSAPublicKey

@Configuration
@ConfigurationProperties(prefix = "rsa")
open class RsaKeyProperty {
    lateinit var publicKey: RSAPublicKey
    lateinit var privateKey: RSAPrivateKey
}