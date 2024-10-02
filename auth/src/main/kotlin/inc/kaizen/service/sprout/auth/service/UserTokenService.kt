package inc.kaizen.service.sprout.auth.service

import inc.kaizen.service.sprout.auth.extension.authentication
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.token.DefaultToken
import org.springframework.security.core.token.Token
import org.springframework.security.core.token.TokenService
import org.springframework.security.oauth2.jwt.JwtClaimsSet
import org.springframework.security.oauth2.jwt.JwtEncoder
import org.springframework.security.oauth2.jwt.JwtEncoderParameters
import org.springframework.stereotype.Service
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.stream.Collectors

@Service
class UserTokenService: TokenService {

    @Autowired
    private lateinit var encoder: JwtEncoder

    override fun allocateToken(
        extendedInformation: String?,
    ): Token {
//        // Implement token allocation logic here
//        // This is a placeholder implementation
//        if (extendedInformation == null || extendedInformation.isEmpty()) {
//            throw IllegalArgumentException("Extended information cannot be null or empty")
//        }

        val authentication = authentication()

        val scope = authentication
            .authorities
            .stream()
            .map { it.authority }
            .collect(Collectors.joining(" "))

        val now = Instant.now()
        val claimsSet = JwtClaimsSet
            .builder()
            .issuer("self")
            .issuedAt(now)
            .expiresAt(now.plus(1, ChronoUnit.HOURS))
            .subject(authentication.name)
            .claim("scope", scope)
            .build()

        val tokenValue = encoder.encode(JwtEncoderParameters.from(claimsSet)).tokenValue
        return DefaultToken(tokenValue, now.epochSecond, extendedInformation)
    }

    override fun verifyToken(key: String?): Token {
        // Implement token verification logic here
        // This is a placeholder implementation
        if (key == null || key.isEmpty()) {
            throw IllegalArgumentException("Token key cannot be null or empty")
        }

        // Assuming we have a method to decode and verify the token
        val decodedToken = decodeAndVerifyToken(key)
        return decodedToken
    }

    private fun decodeAndVerifyToken(key: String): Token {
        // Placeholder for actual token decoding and verification logic
        // This should include checking the token signature, expiration, etc.
        // For now, we return a dummy token
        return DefaultToken(key, Instant.now().epochSecond, "dummy")
    }
}