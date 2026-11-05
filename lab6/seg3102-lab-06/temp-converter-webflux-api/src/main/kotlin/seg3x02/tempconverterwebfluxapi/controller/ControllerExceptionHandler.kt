package seg3x02.tempconverterwebfluxapi.controller

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice
import reactor.core.publisher.Mono

@RestControllerAdvice
class ControllerExceptionHandler {
    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleException(ex: Exception): Mono<ResponseEntity<String>> {
        return Mono.just(ResponseEntity.badRequest().body("Unable to process request"))
    }
}
