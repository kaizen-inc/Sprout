package inc.kaizen.service.sprout.base.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class PingController {

    @GetMapping("/ping")
    fun ping(/*principal: Principal*/): ResponseEntity<String> {
        /* ${principal.name}*/
        val message = "{ \"message\":\"Hi there, welcome!\"}"
        return ResponseEntity.accepted().body(message)
    }
}