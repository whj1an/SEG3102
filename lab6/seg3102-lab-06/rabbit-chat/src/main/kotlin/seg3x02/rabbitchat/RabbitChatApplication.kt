package seg3x02.rabbitchat

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class RabbitChatApplication

fun main(args: Array<String>) {
	runApplication<RabbitChatApplication>(*args)
}
