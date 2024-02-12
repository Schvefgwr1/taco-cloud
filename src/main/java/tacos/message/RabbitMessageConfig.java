package tacos.message;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMessageConfig {

    @Bean
    public Jackson2JsonMessageConverter messageConverterJackson() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public CustomMessageConverterTacoOrders messageConverter() {
        return new CustomMessageConverterTacoOrders(
                new ObjectMapper(),
                messageConverterJackson()
        );
    }
}
