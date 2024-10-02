package inc.kaizen.service.sprout.auth.service

import inc.kaizen.service.sprout.auth.extension.currentUserId
import inc.kaizen.service.sprout.auth.extension.getFileExtension
import inc.kaizen.service.sprout.auth.model.data.AuthUser
import inc.kaizen.service.sprout.auth.repository.AuthUserRepository
import inc.kaizen.service.sprout.base.exception.AlreadyExistsException
import inc.kaizen.service.sprout.base.service.IService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationEventPublisher
import org.springframework.context.MessageSource
import org.springframework.context.i18n.LocaleContextHolder
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.security.core.Authentication
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.oauth2.jwt.JwtClaimsSet
import org.springframework.security.oauth2.jwt.JwtEncoder
import org.springframework.security.oauth2.jwt.JwtEncoderParameters
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.stream.Collectors

@Service
class AuthUserService: UserDetailsService, IService<AuthUser, String> {

    private val uploadDirectory: Path = Paths.get("resumes")

    @Autowired
    private lateinit var publisher: ApplicationEventPublisher

    @Autowired
    private lateinit var messageSource: MessageSource

    @Autowired
    private lateinit var userRepository: AuthUserRepository

    @Autowired
    private lateinit var entityService: AuthUserEntityService

    override fun loadUserByUsername(username: String): UserDetails {
        return userRepository
            .findById(username)
            .map { person -> User.withUsername(person.username)
                .password(person.password)
                .roles("USER")
                .build()
            }
            .orElseThrow { Exception("No user found for username $username") }
    }

    override fun create(user: AuthUser): AuthUser {
        if (!userRepository.existsById(user.username)) {
            return entityService.convert(userRepository.save(entityService.convert(user)))
        } else {
            val foundMessage = messageSource
                .getMessage("Exception.found",
                    arrayOf(user.username, AuthUser::class.java.simpleName),
                    "Exception occurred",
                    LocaleContextHolder.getLocale())
            throw AlreadyExistsException(foundMessage!!)
        }
    }

    // Check the authority to access the user
    override fun deleteById(id: String) {
        if(userRepository.existsById(id)) {
            val message = messageSource
                .getMessage("Exception.noSuchElementException",
                    arrayOf(id),
                    "Exception occurred",
                    LocaleContextHolder.getLocale())
            throw NoSuchElementException(message)
        }
        userRepository.deleteById(id)
    }

    // Check the authority to access the user
    override fun update(user: AuthUser): AuthUser {
        if (userRepository.existsById(user.username)) {
            return entityService.convert(userRepository.save(entityService.convert(user)))
        } else {
            val message = messageSource
                .getMessage("Exception.noSuchElementException",
                    arrayOf(user.username),
                    "Exception occurred",
                    LocaleContextHolder.getLocale())
            throw NoSuchElementException(message)
        }
    }

    // Check the authority to access the user
    override fun findById(id: String): AuthUser? {
        return userRepository
            .findById(id)
            .map { entityService.convert(it) }
            .orElseGet {
                throw Exception("Person not found")
            }
    }

    // Check the authority to access the users
    override fun findAll(
        page: Int,
        pageSize: Int
    ): Page<AuthUser> {
        val pageable = PageRequest.of(page, pageSize)
        return userRepository.findAll(pageable).map { entityService.convert(it) }
    }

    fun changePassword(oldPassword: String?, newPassword: String?) {
        val userId = currentUserId()
        val userEntity = userRepository.findById(userId).orElseThrow {
            val message = messageSource.getMessage(
                "Exception.noSuchElementException",
                arrayOf(userId),
                "Exception occurred",
                LocaleContextHolder.getLocale()
            )
            NoSuchElementException(message)
        }

        if (oldPassword != null && newPassword != null) {
            if (userEntity.password == oldPassword) {
                userEntity.secret = newPassword
                userRepository.save(userEntity)
            } else {
                val message = messageSource.getMessage(
                    "Exception.invalidOldPassword",
                    null,
                    "Invalid old password",
                    LocaleContextHolder.getLocale()
                )
                throw IllegalArgumentException(message)
            }
        } else {
            val message = messageSource.getMessage(
                "Exception.nullPassword",
                null,
                "Password cannot be null",
                LocaleContextHolder.getLocale()
            )
            throw IllegalArgumentException(message)
        }
    }

    fun uploadFile(file: MultipartFile) {
        val userId = currentUserId()
        val fileName: String = userId.plus(".").plus(file.getFileExtension())
        val filePath = uploadDirectory.resolve(fileName)
        Files.copy(file.inputStream, filePath)
    }
}