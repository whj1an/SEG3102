// Haojian Wang, 300411829
package seg3102.calculatorapi

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@SpringBootTest
@AutoConfigureMockMvc
class CalculatorControllerTests(@Autowired val mockMvc: MockMvc) {

    @Test
    fun add() {
        mockMvc.perform(get("/calculator/add/2/3"))
            .andExpect(status().isOk).andExpect(content().string("5.0"))
    }

    @Test
    fun subtract() {
        mockMvc.perform(get("/calculator/subtract/10/4"))
            .andExpect(status().isOk).andExpect(content().string("6.0"))
    }

    @Test
    fun multiply() {
        mockMvc.perform(get("/calculator/multiply/2.5/4"))
            .andExpect(status().isOk).andExpect(content().string("10.0"))
    }

    @Test
    fun divide() {
        mockMvc.perform(get("/calculator/divide/9/2"))
            .andExpect(status().isOk).andExpect(content().string("4.5"))
    }

    @Test
    fun divideByZeroIsBadRequest() {
        mockMvc.perform(get("/calculator/divide/1/0"))
            .andExpect(status().isBadRequest)
    }

    @Test
    fun nonNumericIsBadRequest() {
        mockMvc.perform(get("/calculator/add/abc/1"))
            .andExpect(status().isBadRequest)
    }

    @Test
    fun openApiDocsAvailable() {
        mockMvc.perform(get("/v3/api-docs"))
            .andExpect(status().isOk)
    }
}
