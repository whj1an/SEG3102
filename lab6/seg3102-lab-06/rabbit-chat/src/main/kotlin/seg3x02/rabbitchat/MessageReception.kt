package seg3x02.rabbitchat

import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
class MessageReception {
    @Value("\${chat.alias}")
    val USER: String=""

    @RabbitListener(queues = ["#{queue.name}"])
    fun receive(msg: String) {
        val user = msg.split(":")[0]
        if (!this.USER.equals(user)) println(msg)
    }
}
