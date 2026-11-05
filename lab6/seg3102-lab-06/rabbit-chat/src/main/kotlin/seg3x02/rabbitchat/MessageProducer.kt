package seg3x02.rabbitchat

import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Component
import kotlin.system.exitProcess

@Component
class MessageProducer(val template: RabbitTemplate): CommandLineRunner {
    @Value("\${chat.alias}")
    val USER: String=""

    override fun run(vararg args: String?) {
        println("**** You are connected to Simple Chat ****")
        println("**** Enter #BYE to terminate          ****")
        println("******************************************")
        println()
        var msg = readLine()
        while (msg != null && msg != "#BYE") {
            template.convertAndSend("chatfan", "", "$USER: $msg")
            msg = readLine()
        }
        println("Shutting Down ...")
        exitProcess(0)
    }
}
