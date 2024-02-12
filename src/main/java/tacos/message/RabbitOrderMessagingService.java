package tacos.message;

import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tacos.model.TacoOrder;
@Service
public class RabbitOrderMessagingService implements OrderMessagingService {
    private RabbitTemplate rabbit;
    @Autowired
    public RabbitOrderMessagingService(RabbitTemplate rabbit) {
        this.rabbit = rabbit;
    }
    //собственный конвертер
//    public void sendOrder(TacoOrder order) {
//        MessageConverter converter = rabbit.getMessageConverter();
//        MessageProperties props = new MessageProperties();
//        Message message = converter.toMessage(order, props);
//        rabbit.send("tacocloud.order", message);
//    }


    //постпроцессор в данном методе добавляет заголовок
    public void sendOrder(TacoOrder order) {
        rabbit.convertAndSend("tacocloud.order", order, new MessagePostProcessor() {
            @Override
            public Message postProcessMessage(Message message) throws AmqpException {
                MessageProperties props = message.getMessageProperties();
                props.setHeader("X_ORDER_SOURCE", "WEB");
                return message;
            }
        });
    }

}
