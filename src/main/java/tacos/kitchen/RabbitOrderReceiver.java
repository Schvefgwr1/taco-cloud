package tacos.kitchen;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import tacos.message.CustomMessageConverterTacoOrders;
import tacos.model.TacoOrder;

@Component
public class RabbitOrderReceiver {
    private RabbitTemplate rabbit;
    private MessageConverter converter;

    @Autowired
    public RabbitOrderReceiver(RabbitTemplate rabbit) {
        this.rabbit = rabbit;
        this.converter = new CustomMessageConverterTacoOrders(
                new ObjectMapper(),
                new Jackson2JsonMessageConverter()
        );
    }

    public TacoOrder receiveOrder() {
        Message message = rabbit.receive("tacoorders", 30000);
        return message != null
                ? (TacoOrder) converter.fromMessage(message)
                : null;
    }

    //решение с автоматическим преобразованием типов но без тайм аута
//    public TacoOrder receiveOrder() {
//        return rabbit.receiveAndConvert("tacoorders",
//                new ParameterizedTypeReference<TacoOrder>() {
//                }
//        );
//    }
}