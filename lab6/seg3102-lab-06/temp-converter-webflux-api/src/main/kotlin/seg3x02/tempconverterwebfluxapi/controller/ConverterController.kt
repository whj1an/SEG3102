package seg3x02.tempconverterwebfluxapi.controller

import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController
@CrossOrigin("http://localhost:4200")
@RequestMapping("temperature-converter")
class ConverterController {
    @GetMapping("/celsius-fahrenheit/{celsius}")
    fun getFahrenheit(@PathVariable celsius: Double) = Mono.just(((celsius * 9) / 5 + 32))

    @GetMapping("/fahrenheit-celsius/{fahrenheit}")
    fun getCelsius(@PathVariable fahrenheit: Double) = Mono.just(((fahrenheit - 32) * 5) / 9)
}
