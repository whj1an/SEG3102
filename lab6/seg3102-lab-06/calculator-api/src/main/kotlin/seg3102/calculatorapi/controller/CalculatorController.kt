// Haojian Wang, 300411829
package seg3102.calculatorapi.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@CrossOrigin("http://localhost:4200")
@RequestMapping("calculator")
@Tag(name = "Calculator", description = "Basic arithmetic operations on two numbers")
class CalculatorController {

    @Operation(summary = "Add two numbers", description = "Returns number1 + number2")
    @GetMapping("/add/{number1}/{number2}")
    fun add(
        @Parameter(description = "First operand") @PathVariable number1: Double,
        @Parameter(description = "Second operand") @PathVariable number2: Double
    ): Double = number1 + number2

    @Operation(summary = "Subtract two numbers", description = "Returns number1 - number2")
    @GetMapping("/subtract/{number1}/{number2}")
    fun subtract(
        @Parameter(description = "First operand") @PathVariable number1: Double,
        @Parameter(description = "Second operand") @PathVariable number2: Double
    ): Double = number1 - number2

    @Operation(summary = "Multiply two numbers", description = "Returns number1 * number2")
    @GetMapping("/multiply/{number1}/{number2}")
    fun multiply(
        @Parameter(description = "First operand") @PathVariable number1: Double,
        @Parameter(description = "Second operand") @PathVariable number2: Double
    ): Double = number1 * number2

    @Operation(
        summary = "Divide two numbers",
        description = "Returns number1 / number2. Responds 400 Bad Request when number2 is 0."
    )
    @GetMapping("/divide/{number1}/{number2}")
    fun divide(
        @Parameter(description = "Dividend") @PathVariable number1: Double,
        @Parameter(description = "Divisor (must not be 0)") @PathVariable number2: Double
    ): Double {
        if (number2 == 0.0) throw ArithmeticException("Division by zero")
        return number1 / number2
    }
}
