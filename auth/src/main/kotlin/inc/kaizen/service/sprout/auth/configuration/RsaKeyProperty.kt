package inc.kaizen.service.sprout.auth.configuration

import org.springframework.boot.context.properties.ConfigurationProperties
import java.security.interfaces.RSAPrivateKey
import java.security.interfaces.RSAPublicKey

@ConfigurationProperties(prefix = "rsa")
data class RsaKeyProperty(
    val publicKey: RSAPublicKey,
    val privateKey: RSAPrivateKey
)