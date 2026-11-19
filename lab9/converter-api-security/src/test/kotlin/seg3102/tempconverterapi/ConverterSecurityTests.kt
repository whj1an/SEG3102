// haojian wang 300411829, Lab9
package seg3102.tempconverterapi

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@SpringBootTest
@AutoConfigureMockMvc
class ConverterSecurityTests {

    @Autowired
    lateinit var mockMvc: MockMvc

    @Test
    fun `request without credentials is rejected`() {
        mockMvc.perform(get("/temperature-converter/celsius-fahrenheit/100"))
            .andExpect(status().isUnauthorized)
    }

    @Test
    fun `request with wrong password is rejected`() {
        mockMvc.perform(get("/temperature-converter/celsius-fahrenheit/100").with(httpBasic("user1", "wrong")))
            .andExpect(status().isUnauthorized)
    }

    @Test
    fun `user1 can convert celsius to fahrenheit`() {
        mockMvc.perform(get("/temperature-converter/celsius-fahrenheit/100").with(httpBasic("user1", "pass1")))
            .andExpect(status().isOk)
            .andExpect(content().string("212.0"))
    }

    @Test
    fun `user2 can convert fahrenheit to celsius`() {
        mockMvc.perform(get("/temperature-converter/fahrenheit-celsius/212").with(httpBasic("user2", "pass2")))
            .andExpect(status().isOk)
            .andExpect(content().string("100.0"))
    }
}
