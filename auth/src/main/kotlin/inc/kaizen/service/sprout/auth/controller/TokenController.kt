package inc.kaizen.service.sprout.auth.controller

import inc.kaizen.service.sprout.auth.service.UserTokenService
import inc.kaizen.service.sprout.base.extension.closureWithReturn
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/token")
class TokenController {//: IController<Token> {

    @Autowired
    private lateinit var tokenService: UserTokenService

    @GetMapping("/regenerate")
    fun generateToken(@RequestParam extendedInformation: String): ResponseEntity<Any> {
        return closureWithReturn {
            val token = tokenService.allocateToken(extendedInformation)
            return@closureWithReturn token.key
        }
    }

    @GetMapping("/verify")
    fun verifyToken(@RequestParam tokenKey: String): ResponseEntity<Any> {
        return closureWithReturn {
            val token = tokenService.verifyToken(tokenKey)
            return@closureWithReturn "Token is valid with extended information: ${token.extendedInformation}"
        }
    }
}