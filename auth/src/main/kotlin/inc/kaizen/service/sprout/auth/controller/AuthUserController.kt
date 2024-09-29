package inc.kaizen.parichaya.api

import inc.kaizen.service.sprout.auth.model.data.AuthUser
import inc.kaizen.service.sprout.auth.service.AuthUserService
import inc.kaizen.service.sprout.base.controller.IController
import inc.kaizen.service.sprout.base.extension.closureWithReturn
import inc.kaizen.service.sprout.base.service.IService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@RestController
class AuthUserController: IController<AuthUser> {

    @Autowired
    lateinit var authUserService: AuthUserService

//    @Autowired
//    lateinit var tokenService: TokenService

    @Autowired
    private lateinit var passwordEncoder: PasswordEncoder

    @PostMapping("/user/signup")
    fun register(@RequestBody user: AuthUser): ResponseEntity<Any> {
        return closureWithReturn {
            user.secret = passwordEncoder.encode(user.password)
            return@closureWithReturn authUserService.create(user)
        }
    }

    @PostMapping("/user/login")
    fun login(authentication: Authentication): ResponseEntity<Any> {
        return closureWithReturn {
            return@closureWithReturn authUserService.generateToken(authentication)
        }
    }

    override fun create(@RequestBody user: AuthUser): ResponseEntity<Any> {
        return closureWithReturn {
            return@closureWithReturn authUserService.create(user)
        }
    }

    // Check the authority to access the user
    @DeleteMapping("/persons/{id}")
    override fun deleteById(@PathVariable("id") id: String): ResponseEntity<Any> {
        return closureWithReturn {
            return@closureWithReturn authUserService.deleteById(id)
        }
    }

    @PutMapping("/persons/update")
    override fun update(@RequestBody user: AuthUser): ResponseEntity<Any> {
        return closureWithReturn {
            return@closureWithReturn authUserService.update(user)
        }
    }

    // Check the authority to access the user
    @GetMapping("/persons/{id}")
    override fun findById(@PathVariable("id") id: String): ResponseEntity<Any> {
        return closureWithReturn {
            return@closureWithReturn authUserService.findById(id)
        }
    }

    // Check the authority to access the user
    @GetMapping("/persons")
    override fun findAll(
        @RequestParam(
            name="page",
            required = false,
            defaultValue = IService.DEFAULT_PAGE_NUMBER
        ) page: Int,
        @RequestParam(
            name="size",
            required = false,
            defaultValue = IService.DEFAULT_PAGE_SIZE
        ) pageSize: Int
    ): ResponseEntity<Any> {
        return closureWithReturn {
            return@closureWithReturn authUserService.findAll(page, pageSize)
        }
    }

    @PostMapping("/persons/photo")
    fun uploadResume(@RequestParam("file") file: MultipartFile): ResponseEntity<Any> {
        return closureWithReturn {
            return@closureWithReturn authUserService.uploadFile(file)
        }
    }
}