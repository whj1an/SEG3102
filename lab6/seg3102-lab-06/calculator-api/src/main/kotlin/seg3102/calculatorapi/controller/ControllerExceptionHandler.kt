// Haojian Wang, 300411829
package seg3102.calculatorapi.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException

@RestControllerAdvice
class ControllerExceptionHandler {

    @ExceptionHandler(ArithmeticException::class)
    fun handleArithmetic(ex: ArithmeticException): ResponseEntity<String> =
        ResponseEntity.badRequest().body("Unable to process request: ${ex.message}")

    @ExceptionHandler(MethodArgumentTypeMismatchException::class)
    fun handleBadNumber(ex: MethodArgumentTypeMismatchException): ResponseEntity<String> =
        ResponseEntity.badRequest().body("Unable to process request: '${ex.value}' is not a valid number")

    @ExceptionHandler(Exception::class)
    fun handleException(ex: Exception): ResponseEntity<String> =
        ResponseEntity.badRequest().body("Unable to process request")
}
