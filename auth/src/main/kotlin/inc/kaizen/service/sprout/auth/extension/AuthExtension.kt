package inc.kaizen.service.sprout.auth.extension

import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.multipart.MultipartFile

fun MultipartFile.getFileExtension(): String {
    val originalFileName = this.originalFilename ?: return ""
    val dotIndex = originalFileName.lastIndexOf('.')
    require(dotIndex != -1 && dotIndex < originalFileName.length - 1) { "The file name is invalid!" }
    return originalFileName.substring(dotIndex + 1)
}

fun currentUserId(): String {
    return SecurityContextHolder.getContext().authentication.name
}