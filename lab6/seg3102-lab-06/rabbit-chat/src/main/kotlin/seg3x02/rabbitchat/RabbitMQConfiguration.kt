package seg3x02.rabbitchat

import org.springframework.amqp.core.*
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class RabbitMQConfiguration {
    @Bean
    fun queue(): AnonymousQueue {
        return AnonymousQueue()
    }

    @Bean
    fun exchange(): FanoutExchange {
        return FanoutExchange("chatfan")
    }

    @Bean
    fun binding(exchange: FanoutExchange,
                queue: Queue
    ): Binding {
        return BindingBuilder.bind(queue)
            .to(exchange)
    }
}
