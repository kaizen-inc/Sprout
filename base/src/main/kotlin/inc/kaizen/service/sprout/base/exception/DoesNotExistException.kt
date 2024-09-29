package inc.kaizen.service.sprout.base.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(value = HttpStatus.NOT_FOUND)
class DoesNotExistException(exception: String) : RuntimeException(exception)