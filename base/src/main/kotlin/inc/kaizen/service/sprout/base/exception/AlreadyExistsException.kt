package inc.kaizen.service.sprout.base.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(value = HttpStatus.FOUND)
class AlreadyExistsException(exception: String) : RuntimeException(exception)